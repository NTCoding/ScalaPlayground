package chapter4

sealed trait Option[+A] {
	def map[B](f: A => B): Option[B] = this match {
		case None => 
			None
		case Some(a) =>
			Some(f(a))
	}

	def flatMap[B](f: A => Option[B]) = map(f) getOrElse None
	
	def getOrElse[B >: A](default: => B) = this match {
		case None =>
			default
		case Some(a) =>
			a
	}

	def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
		case None =>
			ob
		case s @ Some(_) =>
			s
	}

	def filter(f: A => Boolean): Option[A] = this match {
		case None =>
			None
		case x @ Some(a) =>
			if (f(a)) x else None
	}

}

case object None extends Option[Nothing] 
case class Some[+A](a: A) extends Option[A] 

object Option { 
	/*
		Variance is the mean of the squared difference between each value and the mean
	*/
	def variance(xs: Seq[Double]): Option[Double] = mean(xs).flatMap(m =>	mean(xs.map(x => math.pow(x - m, 2))))

	def mean(xs: Seq[Double]): Option[Double] = xs match {
		case x if(x.length) < 2 =>
			None
		case _ =>
			Some(xs.sum / xs.length)
	}

	def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = a.flatMap(a2 => b.map(b2 => f(a2, b2)))
}

sealed trait Either[+E, +A] {

	def map[B](f: A => B): Either[E, B] = this match {
		case l @ Left(error) =>
			l
		case Right(value) =>
			Right(f(value))
	}

}

case class Left[+E](e: E) extends Either[E, Nothing]
case class Right[+A](a: A) extends Either[Nothing, A]

object Try {
	def apply[A, E <: Exception](f: => A) = {
		try Right(f)
		catch { case e: E => Left(e) }
	}
}


