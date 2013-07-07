import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.{Field, Document}
import org.apache.lucene.index.{Term, IndexReader, IndexWriter}
import org.apache.lucene.search.{TermQuery, IndexSearcher}
import org.apache.lucene.store.RAMDirectory
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfter, FreeSpec, MustMatchers}

class Indexing_tests extends FreeSpec with MustMatchers with BeforeAndAfterEach {

    var dir: RAMDirectory = _
    def writer = new IndexWriter(dir, new WhitespaceAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED)
    var wr: IndexWriter = _

    val ids = Array("1", "2")
    val unindexed = Array("Netherlands", "Italy")
    val unstored = Array("Amsterdam has lots of bridges", "Venice has lots of canals")
    val text = Array("Amsterdam", "Venice")

    override def beforeEach{
        dir = new RAMDirectory

        val wri = writer
        for (i <- 0 to ids.length - 1) {
            val doc = new Document
            doc add new Field("id", ids(i), Field.Store.YES, Field.Index.NOT_ANALYZED)
            doc add new Field("country", unindexed(i), Field.Store.YES, Field.Index.NO)
            doc add new Field("contents", unstored(i), Field.Store.NO, Field.Index.ANALYZED)
            doc add new Field("city", text(i), Field.Store.YES, Field.Index.ANALYZED)
            wri addDocument doc
        }
        wri close

        wr = writer
    }

    override def afterEach { wr close }

    "Number of documents writer finds matches the number added" in {
        wr.numDocs must equal(ids.length)
    }

    "MaxDoc and numDocs correspond with number of documents added" in {
        val reader = IndexReader open dir
        reader.maxDoc must equal(ids.length)
        reader.numDocs must equal(ids.length)
        reader close
    }

    "Deleting a document without optimizing then causes the writer to queue up deleted" in {
        wr deleteDocuments new Term("id", "1")
        wr commit

        wr.hasDeletions must equal(true)
        wr.maxDoc must equal(2)
        wr.numDocs must equal(1)
    }

    "But once the deletes are committed the documents are removed from the index" in {
        wr deleteDocuments new Term("id", "1")
        wr.optimize
        wr commit

        wr.maxDoc must equal(1)
        wr.numDocs must equal(1)
    }

    "Document fields show new value when updated, and not old value" in {
        val update = new Document
        update add new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED)
        update add new Field("country", "Netherlands", Field.Store.YES, Field.Index.NO)
        update add new Field("contents", "Den Haag has a lot of museums", Field.Store.NO, Field.Index.ANALYZED)
        update add new Field("city", "Den Haag", Field.Store.YES, Field.Index.ANALYZED)

        wr updateDocument(new Term("id", "1"), update)
        wr close

        getHitCount("city", "Amsterdam") must equal(0)
        getHitCount("city", "Den Haag") must equal(1)
    }

    def getHitCount(field: String, q: String): Int = {
        val searcher = new IndexSearcher(dir)
        val query = new TermQuery(new Term(field, q))
        val hitCount = searcher.search(query, 1).totalHits
        searcher.close()
        hitCount
    }
}
