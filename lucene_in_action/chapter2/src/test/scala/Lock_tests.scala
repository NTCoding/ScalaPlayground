import java.io.File
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.{LockObtainFailedException, FSDirectory}
import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class Lock_tests extends FreeSpec with MustMatchers {

    "After creating a temporary directory and opening a writer on it" - {
        val path = s"""${System.getProperty("java.io.tmpdir", "tmp")}${System.getProperty("file.separator")}index"""
        println(s"indexing into $path")
        val dir = FSDirectory.open(new File(path))

        val writer1 = new IndexWriter(dir, new SimpleAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED)

        "Attempting to open a second writer on the same directory causes a locked exception" in {
            val ex = intercept[LockObtainFailedException](new IndexWriter(dir, new SimpleAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED))
            ex.printStackTrace()
            writer1.close()
        }
    }
}
