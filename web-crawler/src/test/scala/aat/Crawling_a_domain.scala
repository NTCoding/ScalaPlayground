package aat

import java.net.URL

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.scalatest.{FreeSpec, Matchers}
import scala.collection.JavaConverters._

import scala.annotation.tailrec

class Crawling_a_domain extends FreeSpec with Matchers {
  import TestData._

  "For a website with multiple linked pages containing assets and external links" - {
    val client = new StubWebClient()
    client.stub(s"$domain", loadHtml("homepage.html"))
    client.stub(s"$domain/page1", loadHtml("page1.html"))
    client.stub(s"$domain/page2", loadHtml("page2.html"))
    client.stub(s"$domain/page3", loadHtml("page3.html"))

    "when crawling from the root URL" - {
      val sitemap = WebCrawler(client.underlying).crawl(domain)

      "a flat sitemap of the website is produced showing links to other pages, static assets and external pages" in {
        assert(sitemap.pages === pagesForTestData)
      }

      "the crawler does not crawl any external pages (those on a different domain)" in {
        assert(sitemap.pages.exists(p => externalUrlsInTestDataPages.contains(p.url)) === false)
      }

      "the crawler does not crawl stylesheets or javascript files" in {
        assert(sitemap.pages.exists(p => testDataAssetUrls.contains(p.url)) === false)
      }
    }
  }

  object TestData {
    val domain = "http://www.ntcoding.co.uk"

    val pagesForTestData = Seq(homepage, page1, page2, page3)
    lazy val homepage = Page(domain, Seq(page1.url, page2.url), homepageAssets, Seq.empty)
    lazy val page1 = Page(s"$domain/page1", Seq(page2.url), page1Assets, Seq("http://localhost"))
    lazy val page2 = Page(s"$domain/page2", Seq(domain, page3.url), page2Assets, Seq("http://localhost/page1", "http://localhost/page2"))
    lazy val page3 = Page(s"$domain/page3", Seq.empty, page3Assets, Seq.empty)

    lazy val homepageAssets = Seq(s"$domain/main.css", s"$domain/main.js", s"$domain/image1.png")
    lazy val page1Assets = Seq(s"$domain/main.css", s"$domain/page1.css", s"$domain/main.js", s"$domain/image1.png")
    lazy val page2Assets = Seq(s"$domain/main.css", s"$domain/main.js")
    lazy val page3Assets = Seq(s"$domain/main.css", s"$domain/page3.css", s"$domain/page3.js", s"$domain/image2.png")

    def loadHtml(filename: String) = io.Source.fromInputStream(getClass.getResourceAsStream(s"/$filename")).mkString

    val externalUrlsInTestDataPages = Seq(
      "http://localhost/",
      "http://localhost/page1",
      "http://localhost/page2"
    )

    val testDataAssetUrls = Seq(
      s"$domain/main.css",
      s"$domain/page1.css",
      s"$domain/page3.css",
      s"$domain/main.js",
      s"$domain/page3.js",
      s"$domain/image1.png",
      s"$domain/image2.png"
    )
  }
}

object WebCrawler {
  type Html = String
  type Url = String
  type WebClient = String => Option[Html]
}

import aat.WebCrawler._

case class WebCrawler(client: WebClient) {
  def crawl(url: Url): Sitemap = {
    Sitemap(crawl("http://" + new URL(url).getAuthority, Seq(url), Seq.empty))
  }

  @tailrec
  private def crawl(domain: String, toCheck: Seq[Url], checked: Seq[Page]): Seq[Page] = toCheck.headOption  match {
    case None => checked
    case Some(url) =>
    client(url).map(html => extractLinksFrom(Jsoup.parse(html), domain, url)) match {
        case Some(page) => crawl(domain, toCheck.tail ++ page.internalLinks.filterNot(hasBeenChecked(_, checked)), checked :+ page)
        case None => crawl(domain, toCheck.tail, checked)
      }
  }

  private def extractLinksFrom(html: Document, domain: String, url: Url): Page = {
    val links = html.select("a")
    val (internal, external) = links.asScala.partition(l => l.attr("href").startsWith(domain) || (!l.attr("href").startsWith("http://")))
    Page(url, internal.map(l => parseLinkUrl(l, domain)), parseAssets(html), external.map(l => parseLinkUrl(l, domain)))
  }

  private def parseAssets(html: Document): Seq[Url] = Seq.empty

  private def parseLinkUrl(link: Element, domain: Url): String = link.attr("href") match {
    case absolute if absolute.startsWith("http://") => absolute
    case relative => s"$domain$relative"
  }

  private def hasBeenChecked(url: Url, checked: Seq[Page]) = checked.map(_.url).contains(url)
}



class StubWebClient {
  private var stubs: Map[String, Html] = Map.empty

  def stub(url: Url, html: Html) { stubs = stubs.updated(url, html) }

  def underlying: WebClient = url => stubs.get(url)
}

case class Sitemap(pages: Seq[Page])

case class Page(url: Url, internalLinks: Seq[Url], assets: Seq[Url], externalLinks: Seq[Url])


