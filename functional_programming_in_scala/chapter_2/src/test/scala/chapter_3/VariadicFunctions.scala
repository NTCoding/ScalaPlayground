package chapter_3

import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class VariadicFunctions extends FreeSpec with MustMatchers {

    "when I pass in 6 numbers it returns them as a single string" in {
        variadico(1, 2, 3) must equal("123")
    }

    "when I pas in 10 numbers it returns them as a single string" in {
        variadico(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) must equal("12345678910")
    }

    def variadico(nums: Int*) = nums mkString ""
}
