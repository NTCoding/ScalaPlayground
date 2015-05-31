package chapter3

sealed trait List[+A]

case object Nil extends List[Nothing]

case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
	
	def apply[A](as: A*): List[A] = {
		if (as.isEmpty) Nil
		else Cons(as.head, apply(as.tail: _*))
	}

	def drop[A](l: List[A], n: Int): List[A] =  n match {
		case 0 =>
			l
		case _ =>
			drop(tail(l), n - 1)
	}

	def dropWhile[A](l: List[A], p: A => Boolean): List[A] = l match {
		case Nil =>
			Nil
		case Cons(h, t) =>
			if (p(h)) dropWhile(t, p) else t
	}

	def tail[A](l: List[A]) = l match {
		case Nil =>
			Nil
		case Cons(h, t) =>
			t
	}

	def setHead[A](l: List[A], h: A) = l match {
		case Nil =>
			Cons(h, Nil)

		case Cons(_, t) =>
			Cons(h, t)
	}

	def init[A](l: List[A]): List[A] = l match {
		case Nil =>
			Nil

		case Cons(h, Nil) =>
			Nil

		case Cons(h, t) =>
			Cons(h, init(t))
	}

	def length[A](as: List[A]) = foldRight(as, 0)((x, y) => y + 1)

	def appendViaFoldRight[A](xs: List[A], x: A): List[A] = foldRight(xs, List(x))(Cons(_, _))

	def foldRight[A, B](xs: List[A], z: B)(f: (A, B) => B): B = xs match {
		case Nil =>
			z

		case Cons(h, t) =>
			f(h, foldRight(t, z)(f))
	}

	def foldLeft[A,B](xs: List[A], z: B)(f: (A, B) => B): B = xs match {
		case Nil =>
			z
		case Cons(h, t)	=>
		  val newState = f(h, z)
		  foldLeft(t, newState)(f)
	}

	def plus1(xs: List[Int]): List[Int] = {
		def x(input: List[Int],  output: List[Int]): List[Int] = input match {
			case Nil =>
				output
			case Cons(h, t) =>
				x(t, Cons(h + 1, output))
		}
		x(xs, Nil)
	}

}

sealed trait Tree[+A]
case class Leaf[A](a: A) extends Tree[A]
case class Branch[A](a: Tree[A], b: Tree[A]) extends Tree[A]

object Tree {

	def size[A](t: Tree[A]): Int = t match {
		case Leaf(t2) =>
			1
		case Branch(t3, t4) =>
			1 + size(t3) + size(t4)
	}

	def depth[A](t: Tree[A]): Int = t match {
		case Leaf(_) => 1
		case Branch(t1, t2) => (1 + depth(t1)).max(1 + depth(t2))
	}

	def max(t: Tree[Int]): Int = {
		def max2(x: Tree[Int], y: Tree[Int]): Int = (x, y) match {
			case (Leaf(i), Leaf(j)) =>
				i.max(j)
			
			case (Leaf(i), Branch(t1, t2)) =>
				i.max(max2(t1, t2))
			
			case (Branch(t1, t2), Leaf(i)) =>
				i.max(max2(t1, t2))

			case (Branch(t1, t2), Branch(t3, t4)) =>
				max2(t1, t2).max(max2(t3, t4))
		}

		t match {
			case Leaf(i) =>
				i

			case Branch(t1, t2)	=>
				max2(t1, t2)
		}	
	}
}