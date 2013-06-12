import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class Partial1HardTest extends FreeSpec with MustMatchers {

    "partial1 allows a function to be created that prepends an integer to a char and returns a string" in {
        val resultFunction = partial1[Int, Char, String](2, (i, c) => f"$i $c")
        resultFunction('a') must equal("2 a")
        resultFunction('d') must equal("2 d")
        resultFunction('z') must equal("2 z")
    }

    def partial1[A, B, C](a: A, f: (A,B) => C): B => C = {
        (Y: B) => f(a, Y)
    }
}
