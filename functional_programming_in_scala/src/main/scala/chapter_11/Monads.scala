package chapter_11

trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] extends Functor[F] {
	def unit[A](a: => A): F[A]

	def flatMap[A,B](a: F[A])(f: A => F[B]): F[B]

	def map[A,B](ma: F[A])(f: A => B): F[B] =
    flatMap(ma)(a => unit(f(a)))

  def map2[A,B,C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] =
    flatMap(ma)(a => map(mb)(b => f(a, b)))

	def sequence[A](lma: List[F[A]]): F[List[A]] = {
		lma.foldRight(unit(List[A]()))((ma, mla) => map2(ma, mla)(_ :: _))
	}

	def replicateM[A](n: Int, ma: F[A]): F[List[A]] = {
		sequence(List.fill(n)(ma))
	}

	def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] = {
		val x = map2(unit(ms), sequence(ms.map(f)))(_ zip _)
		map(x)(_.filter(_._2).map(_._1))
	}

	def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] = {
		(a: A) => flatMap(f(a))(g)
	}

	def flatMapViaCompose[A,B](ma: F[A])(f: A => F[B]): F[B] = {
		compose((_: Unit) => ma, f)()
	}
}

case class Id[A](value: A) {
	def map[B](f: A => B) = Id(f(value))

	def flatMap[B](f: A => Id[B]) = f(value)
}

case class IdMonad[A] extends Monad[Id] {
	def unit[A](a: => A): Id[A] = Id(a)

	def flatMap[A, B](ma: Id[A])(f: A => Id[B]) = ma.flatMap(f)
}