package chapter_5

import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.apache.lucene.search._
import org.apache.lucene.store.{FSDirectory, Directory}
import org.apache.commons.lang.StringUtils._
import java.io.{File, PrintStream}
import java.text.DecimalFormat
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.standard.StandardAnalyzer


class Sorting_example extends FreeSpec with MustMatchers {

    def displayResults(query: Query, sort: Sort, dir: Directory) {
        val searcher = new IndexSearcher(dir)
        searcher.setDefaultFieldSortScoring(true, false)

        val results = searcher.search(query, null, 20, sort)
        println(s"Results for: $query sorted by: $sort")
        println(s"${rightPad("Title", 30)} ${rightPad("pubmonth", 10)}"
        + s"${center("id", 4)} ${center("score", 15)}")

        val out = new PrintStream(System.out, true, "UTF-8")
        val sf = new DecimalFormat("0.######")
        results.scoreDocs foreach(sd => {
            val doc = searcher doc(sd.doc)
            println(s"${rightPad(abbreviate(doc get "title", 29), 30)}"
             + s"${rightPad(doc get "pubmonth", 10)}" + s"${center("" + sd.doc, 4)}"
             + s"${leftPad(sf format sd.score, 12)}"
            )
            out.println(s"     ${doc.get("category")}")
        })
    }

    "When querying for 'java OR action'" - {
        val parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30))
        val query = new BooleanQuery()
        query add (new MatchAllDocsQuery, BooleanClause.Occur.SHOULD)
        query add (parser parse("java OR action"), BooleanClause.Occur.SHOULD)
        val dir = FSDirectory.open(new File("/mnt/repos/lia2e/build/index"))

        "With a relevance sort" in {
            displayResults(query, Sort.RELEVANCE, dir)
        }

        "With an index order sort" in {
            displayResults(query, Sort.INDEXORDER, dir)
        }

        "With a pubmonth sort" in {
            displayResults(query, new Sort(new SortField("pubmonth", SortField.INT, true)), dir)
        }

        "With a category sort" in {
            displayResults(query, new Sort(new SortField("category", SortField.STRING)), dir)
        }

    }
}
