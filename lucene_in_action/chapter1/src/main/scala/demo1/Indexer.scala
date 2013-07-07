package demo1

import org.apache.lucene.index._
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard._
import java.io.{FileReader, FileFilter, File}
import org.apache.lucene.document.{Field, Document}

object Indexer {

    def main(args: Array[String]) {
        val indexDir = args(0)
        val dataDir = args(1)
        println(s"About to index data from dir: $dataDir into dir: $indexDir")

        val start = System.currentTimeMillis()
        val indexer = Indexer(indexDir)

        var numIndexed = 0
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter())
        } finally {
            indexer.close()
        }

        val end = System.currentTimeMillis()

        println(s"Number of documents indexed: $numIndexed. Time taken: ${end - start} milliseconds")
    }

    def apply(indexDir: String) = {
        val dir = FSDirectory.open(new File(indexDir))
        val writer = new IndexWriter(dir, new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED)
        new Indexer(writer)
    }

    private class TextFilesFilter extends FileFilter {
        def accept(pathname: File) = pathname.getName.toLowerCase.endsWith(".txt")
    }
}

class Indexer(private val writer: IndexWriter) {

    def index(dataDir: String, filter: FileFilter) = {
        new File(dataDir).listFiles() foreach(f => {
            if (!f.isDirectory && !f.isHidden && f.exists && f.canRead && (filter == null || filter.accept(f)))  indexFile(f)
        })
        writer numDocs
    }

    private def indexFile(f: File) {
        println(s"Indexing: ${f.getCanonicalPath}")
        val doc = getDoc(f)
        writer addDocument doc
    }

    private def getDoc(f: File) = {
        val d = new Document
        d add new Field("contents", new FileReader(f))
        d add new Field("filename", f.getName, Field.Store.YES, Field.Index.NOT_ANALYZED)
        d add new Field("fullPath", f.getCanonicalPath, Field.Store.YES, Field.Index.NOT_ANALYZED)
        d
    }

    def close() {writer.close()}
}
