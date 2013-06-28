package chapter_3

import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers
import annotation.tailrec

class Folding extends FreeSpec with MustMatchers {

    "you can calculate the length of a list using fold right" in {
        List(1, 2, 3, 4, 5, 6, 7).foldRight(0)((item, total) => total + 1) must equal(7)
    }

    "you can also calculate the length of a list using a tail-recursive fold-left" in {
        foldLeft(List(1, 2, 3, 4, 5, 6, 7), 0)((total, item) => total + 1) must equal(7)
    }

    @tailrec
    private def foldLeft[A,B](l: List[A], z: B)(f: (B, A) => B): B = l match {
        case Nil => z
        case list => foldLeft(list.tail, f(z, list.head))(f)
    }
}
