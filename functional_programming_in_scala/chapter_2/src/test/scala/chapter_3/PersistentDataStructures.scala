package chapter_3

import org.scalatest.matchers.MustMatchers
import org.scalatest.FreeSpec
import chapter_3._
import chapter_3.XList._
import annotation.tailrec

class PersistentDataStructures extends FreeSpec with MustMatchers{

      "tail removes all list items in same order except the first" in {
          tail(XList(1, 2, 3, 4, 5, 6)) must equal(XList(2, 3, 4, 5, 6))
      }

      "drop removes the number of specified items" in {
          drop(4, XList(1, 2, 3, 4, 5, 6)) must equal(XList(5, 6))
      }

      "drop while only removes numbers matching a condition" in {
          dropWhile[Int](XList(1, 2, 3, 4, 5, 6))(f => f > 4) must equal(1, 2, 3, 4)
      }
}

sealed trait XList[+A] {}
case object XNil extends XList[Nothing]
case class Cons[+A](head: A, tail: XList[A]) extends XList[A]

object XList {

    def sum(ints: XList[Int]): Int = ints match {
        case XNil => 0
        case Cons(x,xs) => x + sum(xs)
    }

    def product(ds: XList[Double]): Double = ds match {
        case XNil => 1.0
        case Cons(0.0, _) => 0.0
        case Cons(x,xs) => x * product(xs)
    }

    def tail[A](ds: XList[A]) : XList[A] = ds match {
        case Cons(head, XNil) => XNil
        case Cons(head, Cons(t1, t2)) => Cons(t1, t2)
    }

    def drop(amount: Int, items: XList[Int]) : XList[Int] = amount match {
        case 0 => items
        case _ => drop(amount - 1, tail(items))
    }

    def dropWhile[A](l: XList[A])(f: A => Boolean): XList[A] = {

        @tailrec
        def dropWhile(toCheck: XList[A], toKeep: XList[A], dropItem: A => Boolean): XList[A] = toCheck match {
            case Cons(h, XNil) if !dropItem(h) => Cons(h, toKeep)
            case Cons(h, XNil) if dropItem(h) => toKeep
            case Cons(h, t) if !dropItem(h) => dropWhile(t, Cons(h, toKeep), dropItem)
            case Cons(h, t) if dropItem(h) => dropWhile(t, toKeep, dropItem)
        }

        dropWhile(l, XList[A](), f)
    }

    def apply[A](as: A*): XList[A] =
        if (as.isEmpty) XNil
        else Cons(as.head, apply(as.tail: _*))
}