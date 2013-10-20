package chapter_3

import java.io.File
import org.apache.lucene.index.Term
import org.apache.lucene.search.{TermQuery, Query, IndexSearcher}
import org.apache.lucene.store.FSDirectory
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class Basic_searching_test extends FreeSpec with MustMatchers {

    "After opening a searcher on an index that contains content with ant and junit" - {
        val dir = FSDirectory.open(new File("/mnt/repos/lia2e/build/index"))
        val searcher = new IndexSearcher(dir)

        "A search for ant finds the item containing Ant" in {
            val antQuery = new TermQuery(new Term("subject", "ant"))
            val docs = searcher.search(antQuery, 10)
            docs.totalHits must equal(1)
        }

        "A search for junit finds the items containing Junit" in {
            val junitQuery = new TermQuery(new Term("subject", "junit"))
            val docs = searcher.search(junitQuery, 10)
            docs.totalHits must equal(2)
        }

        "_ Cleanup" in {
            searcher close()
            dir close()
        }
    }
}
