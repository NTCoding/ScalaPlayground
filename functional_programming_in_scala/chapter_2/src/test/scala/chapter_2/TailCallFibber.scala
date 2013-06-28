package chapter_2

import annotation.tailrec
import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class TailCallFibber extends FreeSpec with MustMatchers {

    "the 0th fib is 0" in {
        fib(0) must equal(0)
    }

    "the 5th fibonacci number is 5" in {
        fib(5) must equal(5)
    }

    "the 14th fib is 233" in {
        fib(14) must equal(377)
    }


    def fib(n: Int) = {

        @tailrec
        def fib(n: Int, iteration: Int, numbers: Array[Int]) : Int = {
            if (n <= 1) return n // could remove this and have only pattern match but it looks worse
            iteration match {
                case end if end >= n => numbers.last + numbers.reverse.tail.head
                case otherwise => fib(n, iteration + 1, numbers :+ (numbers.last + numbers.reverse.tail.head))
            }
        }

       fib(n, 2, Array(0, 1))
    }
}
