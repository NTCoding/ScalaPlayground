package chapter_3

import java.io.File
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class Explain_test extends FreeSpec with MustMatchers {

    "Query an index full of data and print the score explanation" in {
        val dir = FSDirectory.open(new File("/mnt/repos/lia2e/build/index"))
        val searcher = new IndexSearcher(dir)

        val parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer)
        val query = parser.parse("Thief OR Memory")

        val docs = searcher.search(query, 10)
        println(s"Number of search results: ${docs.totalHits}")
        docs.scoreDocs.foreach (doc => {
            println("                      ")
            val d = searcher.doc(doc.doc)
            println(s"title: ${d get "title"}")
            val explanation = searcher.explain(query, doc.doc)
            println(s"explanation: $explanation")
        })
    }

}
