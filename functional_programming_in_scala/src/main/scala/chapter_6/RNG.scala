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

  def doubleViaMap: Rand[Double] = {
    map(_.nextInt)(x => between0and1(x.toDouble))
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

 def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] =
    rng => {
      val (a, r1) = f(rng)
      g(a)(r1) // We pass the new state along
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

object State {

  def unit[S, A](a: A): State[S, A] =  State(s => (a, s))

  def sequence[S,A](fs: List[State[S,A]]): State[S, List[A]] = {
    val l: List[A] = List.empty
    State(s => {
      fs.foldLeft((l,s)) { (ns: (List[A], S), nv: State[S,A]) =>
        val (na,nns) = nv.run(ns._2)
        (ns._1 :+ na,nns)
      }
    })
  }


  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get // Gets the current state and assigns it to `s`.
    _ <- set(f(s)) // Sets the new state to `f` applied to `s`.
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
}

case class State[S, +A](run: S => (A,S)) {

  def map[B](f: A => B): State[S,B] = {
    State(s => {
      val (a, ns) = run(s)
      (f(a), ns)
    })
  }

  def map2[B,C](sb: State[S,B])(f: (A,B) => C): State[S,C] = {
    State(s => {
      val (nva, nsa) = run(s)
      val (nvb, nsb) = sb.run(nsa)
      (f(nva,nvb), nsb)
    })
  }

  def flatMap[B](f: A => State[S,B]): State[S,B] = {
    State(s => {
      val (a,ns) = run(s)
      val sb = f(a)
      sb.run(ns)
    })
 }

}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

object Candy {
  def update = (i: Input) => (s: Machine) =>
    (i, s) match {
      case (_, Machine(_, 0, _)) => s
      case (Coin, Machine(false, _, _)) => s
      case (Turn, Machine(true, _, _)) => s
      case (Coin, Machine(true, candy, coin)) =>
        Machine(false, candy, coin + 1)
      case (Turn, Machine(false, candy, coin)) =>
        Machine(true, candy - 1, coin)
    }
}

case class Machine(locked: Boolean, candies: Int, coins: Int) {
  
  def apply(i: Input) = i match {
    case Coin => insertCoin
    case Turn => turnKnob
  }
  
  private def insertCoin: Machine = if (candies == 0) this else locked match {
    case true if candies >= 1 => unlock
    case _ => this
  }

  private def unlock: Machine = this.copy(locked = false)

  private def turnKnob: Machine = if (candies == 0) this else locked match {
    case false => dispenseAndLock
    case true => this
  }

  private def dispenseAndLock: Machine = this.copy(locked = true, candies = candies - 1)
  
}

/*
object machine {
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = {
    val st = State.unit[Machine, (Int, Int)]((0, 0))
    inputs.foldLeft(st) { (nv: Input, ns: (State[Machine, (Int, Int)])) =>
      ns.map(machine => {
        val newMac = machine(nv)
        (newMac.candies, newMac.coins)
      })
    }
  }
}
*/
