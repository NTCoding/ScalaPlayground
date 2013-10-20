package chapter_3

import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.{Field, Document}
import org.apache.lucene.index.{Term, IndexWriter}
import org.apache.lucene.search.{PhraseQuery, IndexSearcher}
import org.apache.lucene.store.{RAMDirectory, Directory}
import org.scalatest._

class Phrase_query_test extends FreeSpec with MustMatchers with BeforeAndAfterEach {

    var dir: Directory = _
    var writer: IndexWriter = _
    var searcher: IndexSearcher = _

    override def beforeEach {
        dir = new RAMDirectory
        writer = new IndexWriter(dir, new WhitespaceAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED)

        val d = new Document
        d add new Field("field", "the quick brown fox jumped over the lazy dog",
                        Field.Store.YES, Field.Index.ANALYZED)
        writer addDocument d
        writer close

        searcher = new IndexSearcher(dir)
    }

    override def afterEach {
        searcher close()
        dir close()
    }

    private def matched(phrase: Array[String], slop: Int) = {
        val query = new PhraseQuery
        query setSlop slop
        phrase.foreach(s => query.add(new Term("field", s)))

        val matches = searcher search(query,  10)
        matches.totalHits > 0
    }

    "A slop value of 0 will only find a perfect match" in {
        matched(Array("quick", "fox"), 0) must equal(false)
    }

    "A slop value of 1 will find a close but not perfect match" in {
        matched(Array("quick", "fox"), 1) must equal(true)
    }
}
