package chapter5

sealed trait Stream[+A] {
	def toList(): List[A] = this match {
		case Empty =>
			List.empty
		case Cons(h, t) =>
			h() :: t().toList()
	}

	def take(n: Int): Stream[A] = this match {
		case Cons(h, t) if n > 0 =>
			Cons(h, () => t().take(n -1))
		case _ =>
			Empty
	}

	def drop(n: Int): Stream[A] = this match {
		case Empty =>
			Empty
		case _ if n == 0 =>
			this
		case Cons(h, t) =>
			t() match {
				case Empty =>
					Empty
				case Cons(h2, t2) =>
					Cons(h2, () => t2().drop(n - 1))
			}
	}

	def print() { this match {
		case Empty =>
			println("Empty")
		case Cons(h, t) =>
			println(h())
			t().print
	}}
}

case object Empty extends Stream[Nothing]

case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
	def cons[A](hd: => A, tl: => Stream[A]) = {
		lazy val head = hd
		lazy val tail = tl
		Cons(() => head, () => tail)
	}
}