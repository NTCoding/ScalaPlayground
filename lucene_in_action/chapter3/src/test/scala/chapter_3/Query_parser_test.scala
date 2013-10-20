package chapter_3

import java.io.File
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class Query_parser_test extends FreeSpec with MustMatchers {

    "After setting up a searcher on an index with data" - {
        val dir = FSDirectory.open(new File("/mnt/repos/lia2e/build/index"))
        val searcher = new IndexSearcher(dir)
        val parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer())

        "Plus/minus queries built by parsers still find matching documents" in {
            val query = parser parse "+JUNIT +ANT -MOCK"
            println(s"Complex query type: ${query.getClass}")
            val docs = searcher search(query, 10)
            val doc = searcher doc(docs.scoreDocs(0).doc)
            doc.get("title") must equal("Ant in Action")
        }

        "OR queries built by parsers still find matching documents" in {
            val query = parser parse "mock OR junit"
            val docs = searcher search(query, 10)
            println(s"or query type: ${query.getClass}")
            docs.totalHits must equal(2)
        }

        "_ cleanup" in {
            searcher.close()
            dir.close()
        }
    }
}
