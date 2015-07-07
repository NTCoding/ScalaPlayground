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

	def print() { Stream.print(this) }

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
    	cons(f(h), t)
	}}

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(empty[A]) { (h,t) =>
    	println(s"Filtering over: $h = $t")
      if (f(h)) cons(h, t) else t
    }
  }

  def mapViaUnfold[B](f: A => B): Stream[B] = 
 		unfold(this) {
 			case Empty =>
 				None
 			case Cons(h, t) =>
 			 Some((f(h()), t()))
 		}

 	def takeViaUnfold(n: Int): Stream[A] = 
 		unfold((this, n)) {
 			case (Cons(h, t), n) if n > 0 =>
 				Some(h(), (t(), n - 1))
 			case _ =>
 				None
 		}

 	def zipWithViaUnfold[B,C](a: Stream[B])(f: (A,B) => C): Stream[C] = {
 		unfold((this, a)) {
 			case (Cons(h1, t1), Cons(h2, t2)) =>
 				Some((f(h1(), h2()), (t1(), t2())))

 			case _ =>
 				None
 		}
 	}

 	def zipAllViaUnfold[B](s2: Stream[B]): Stream[(Option[A],Option[B])] = {
 		unfold((this, s2)){
 			case (Cons(h1, t1), Cons(h2, t2)) =>
 				Some(((Some(h1()), Some(h2())), (t1(), t2())))

 			case (Empty, Cons(h2, t2)) =>
 				Some(((None, Some(h2())),(Empty, t2())))

 			case (Cons(h1, t1), Empty) =>
 				Some(( (Some(h1()), None) ,(t1(), Empty)))

 			case (Empty, Empty) =>
 				None
 		}
 	}

 	def startsWith[B](s: Stream[B]): Boolean = {
 		unfold[Boolean, (Stream[A], Stream[B], Boolean)]((this, s, true)) {
 			case (_, _, false) =>
 				None

 			case (Cons(h1, t1), Cons(h2, t2), true) =>
 				if (h1() == h2()) 
 					Some((true, (t1(), t2(), true)))
 				else
 					Some((false, (Empty, Empty, false)))

 			case (Empty, Cons(_, _), true) =>
 				Some((false, (Empty, Empty, false)))

 			case (Cons(_, _), Empty, true) =>
 				None
 		}.forAll(_ == true)
 	}

 	def tails: Stream[Stream[A]] = {
 		unfold(this) {
 			case Cons(h, t) =>
 				Some((Cons(h, () => t().takeWhile(_ != Empty)), t()))

 			case Empty =>
 				None
 		}
 	}

 	def scanRight[B](s: B)(f: (A, B) => B): Stream[B] = {
 		tails.map { stream =>
 			stream.foldRight(stream) { (n,st) =>
 				f(n,st)
 			}
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
		def x: Stream[A] = cons(a, x)
		cons(a, x)
	}

	def from(n: Int): Stream[Int] = {
		Stream.constant(n)
	}

	def fibs(): Stream[Int] = {
		def nextFib(n1: Int, n2: Int): Stream[Int] = cons(n1 + n2, nextFib(n2, n1 + n2))
		cons(0, cons(1, cons(1, nextFib(1, 1))))
	}

	def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
		f(z).map(x => cons(x._1, unfold(x._2)(f))).getOrElse(empty)
	}

	def fibsViaUnfold(): Stream[Int] = {
		cons(0, cons(1, unfold((1, 1))({ s =>
			Some(s._1 + s._2, (s._2, s._1 + s._2))
		})))
	}

	def constantViaUnfold[A](a: A): Stream[A] = {
		unfold(a)(s => Some(s, s))
	}

	def fromViaUnfold(n: Int): Stream[Int] = {
		unfold(n)(s => Some(s, s + 1))
	}

	def print(s: Any) { s match {
		case Empty =>
				println("Empty")
		
		case Cons(h, t) =>
			Stream.print(h())
			t().print
		
		case x: Any =>
			println(x)
	}}

}