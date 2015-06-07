package chapter5

sealed trait Stream[+A] {
	import Stream._

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

	def forAll(p: A => Boolean): Boolean = this match {
		case Empty => false
		case Cons(h, t) => p(h()) && (t() match {
			case Empty => true
			case s: Stream[A] => s.forAll(p)
		})
	}

	def takeWhile(p: A => Boolean): Stream[A] = foldRight(Empty: Stream[A]) { (a, b) =>
		if(p(a)) Cons(() => a, () => b) else b
	}

	def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
		case Empty => z
		case Cons(h, t) => f(h(), t().foldRight(z)(f))
	}

	def headOption(): Option[A] = foldRight(None: Option[A]) { (a, b) => Some(a)	}

	def map[B](f: A => B): Stream[B] = {
    foldRight(empty[B]) { (h,t) => 
    	println(s"Mapping over: $h - $t")
    	cons(f(h), t)
	}}

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(empty[A]) { (h,t) =>
    	println(s"Filtering over: $h = $t")
      if (f(h)) cons(h, t) else t
    }
  }

  
}

case object Empty extends Stream[Nothing]

case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
	def cons[A](hd: => A, tl: => Stream[A]) = {
		lazy val head = hd
		lazy val tail = tl
		Cons(() => head, () => tail)
	}

	def apply[A](as: A*): Stream[A] = {
		if (as.isEmpty)	Empty
		else cons(as.head, apply(as.tail:_*))
	}

	def empty[A]: Stream[A] = Empty

	def constant[A](a: A): Stream[A] = {
		val x = cons(a)
		Cons(a, x)
	}
}