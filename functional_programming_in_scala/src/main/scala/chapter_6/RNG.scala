package chapter6

object RNG {

  type Rand[+A] = RNG => (A, RNG)

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    rng.nextInt match {
      case (Int.MinValue, rng2) => nonNegativeInt(rng2)
      case (pos, rng2) if pos >= 1 => (pos, rng2)
      case (nonPos, rng2) => (-nonPos, rng2)
    }
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, rng2) = rng.nextInt
    val d = i.toDouble
    (between0and1(d), rng2)
  }

  def between0and1(dub: Double): Double = {
      println(s"Checking $dub")
      dub match {
        case small if (small < 0) => between0and1(-small)
        case big if (big > 1) => between0and1(big / 10)
        case good => good
      }
    }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (int, rng2) = rng.nextInt
    val (dub, rng3) = RNG.double(rng2)
    ((int, dub), rng3)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (dub, rng2) = RNG.double(rng)
    val (int, rng3) = rng2.nextInt
    ((dub, int), rng3)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (dub1, rng2) = RNG.double(rng)
    val (dub2, rng3) = RNG.double(rng2)
    val (dub3, rng4) = RNG.double(rng3)
    ((dub1, dub2, dub3), rng4)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    (1 to count).foldLeft((List.empty[Int], rng)) { (s, n) =>
      val (i, nxtRng) = s._2.nextInt
      (s._1 :+ i, nxtRng)
    }
  }

  def doubleViaMap(): Rand[Double] = {
    map(nonNegativeInt)(x => between0and1(x.toDouble))
  }

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
  }

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a, rnga2) = ra(rng)
      val (b, rngb2) = rb(rnga2)
      (f(a, b), rngb2)
    }
  }

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = {
    rng => {
      fs.foldLeft((List.empty: List[A], rng))( (s,v) => {
        val (a, nrng) = v(s._2)
        (s._1 :+ a, nrng)
      })
    }
  }

}

trait RNG {
  def nextInt: (Int, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
  val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
  val nextRNG = SimpleRNG(newSeed)
  val n = (newSeed >>> 16).toInt
  (n, nextRNG)
  }
}