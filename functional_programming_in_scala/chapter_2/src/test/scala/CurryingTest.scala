import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class CurryingTest extends FreeSpec with MustMatchers{

      "curry allows a function to be built that takes just a first name, allowing different scores to be added" in {
          val resultFunc = curry[String, Int, String]((name, age) => f"$name scores: $age")
          val michaelsCurry = resultFunc("Michael")
          michaelsCurry(5) must equal("Michael scores: 5")
          michaelsCurry(1) must equal("Michael scores: 1")
          michaelsCurry(15) must equal("Michael scores: 15")
      }

      def curry[A, B, C](f: (A, B) => C) : A => B => C = (a: A) => (b: B) => f(a, b)
}
