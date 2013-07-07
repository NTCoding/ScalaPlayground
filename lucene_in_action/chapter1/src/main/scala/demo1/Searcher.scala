package demo1

import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.standard.StandardAnalyzer

object Searcher {

    def main(args: Array[String]) {
        val indexDir = args(0)
        val q = args(1)

        println(s"Searching in dir: $indexDir for query: $q")

        search(indexDir, q)
    }

    def search(indexDir: String, q: String) {
        val dir = FSDirectory.open(new File(indexDir))
        val searcher = new IndexSearcher(dir)

        val parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30))
        val query = parser.parse(q)

        val start = System.currentTimeMillis
        val hits = searcher.search(query, 10)
        val end = System.currentTimeMillis

        println(s"Found ${hits.totalHits} hits in ${end - start} milliseconds")

        println("Hits contained in files: ")

        hits.scoreDocs.foreach(sd => println(searcher.doc(sd.doc).get("fullPath")))

        searcher close
    }

}
