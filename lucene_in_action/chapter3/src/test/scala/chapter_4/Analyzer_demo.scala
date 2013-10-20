package chapter_4

import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import org.apache.lucene.analysis.{TokenStream, StopAnalyzer, SimpleAnalyzer, WhitespaceAnalyzer}
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.TermAttribute
import java.io.StringReader

class Analyzer_demo extends FreeSpec with MustMatchers {

    "Analyze some text and see the tokens created" in {
        val analyzers = Array(
            new WhitespaceAnalyzer, new SimpleAnalyzer, new StopAnalyzer(Version.LUCENE_30),
            new StandardAnalyzer(Version.LUCENE_30)
        )

        val args = Array(
            "I'm gonna get you baby - yeahhh heah",
            "Carole's got a fat bum, oooh ooh yeh." ,
            "The quick brown fox jumped over the lazy dog",
            "XY&Z Corporation - xyz@example.com"
        )

        args foreach(a => {
            println(s"Analyzing: $a")
            analyzers foreach(ana => {
                val name = ana.getClass.getSimpleName
                println(s"   $name:")
                displayTokens(ana tokenStream ("contents", new StringReader(a)))
                println("")
            })
        })
    }

    def displayTokens(stream: TokenStream) {
        val term = stream addAttribute(classOf[TermAttribute])

        def printTokens(increment: Boolean) { increment match {
            case true => {
                print(s"[${term.term()}] ")
                printTokens(stream.incrementToken())
            }

            case false => println("------------")
        }}

        printTokens(stream.incrementToken())
    }

}
