import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class FunctionCompositionTest extends FreeSpec with MustMatchers {

    "compose allows a code builder to build codes from ints and chars" in {
        val resultFunc = compose[List[Int], List[Double], String](x =>  x mkString " ", y => y map (_ + 1.5))
        resultFunc(List(1, 2, 3)) must equal("2.5 3.5 4.5")
    }

    def compose[A, B, C](f: B => C, g: A => B): A => C = {
        (x: A) => f(g(x))
    }

    "compose syntactic sugar also works the same as above" in {
        val resultFunc = composeWith[List[Int], List[Double], String](x =>  x mkString " ", y => y map (_ + 1.5))
        resultFunc(List(1, 2, 3)) must equal("2.5 3.5 4.5")
    }

    def composeWith[A, B, C](f: B => C, g: A => B): A => C = {
        f compose g
    }
}
