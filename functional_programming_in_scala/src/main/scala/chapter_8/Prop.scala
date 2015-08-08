package chapter8

import chapter6.{RNG, State}

object Prop {
  type SuccessCount = Int  
  type FailedCase = String
}

import Prop._

trait Prop { 
    def check: Either[(FailedCase, SuccessCount), SuccessCount]
    def && (p: Prop): Prop = ???
}

case class Gen[A](sample: State[RNG, A]) {
  def flatMap[B](f: A => Gen[B]): Gen[B] ={
    Gen(sample.flatMap(f(_).sample))
  }

  def listOfN(size: Int): Gen[List[A]] =
    Gen.listOfN(size, this)

  def listOfN(size: Gen[Int]): Gen[List[A]] =
    size flatMap (n => this.listOfN(n))
}

object Gen {
  def unit[A](a: => A): Gen[A] = {
    Gen(State.unit(a))
  }

  def boolean: Gen[Boolean] = Gen(State(s => {
    val (b,ns) = RNG.nonNegativeInt(s)
    (b % 2 == 1, ns)
  }))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = {
    Gen(State.sequence(List.fill(n)(g.sample)))
  }

  def choose(start: Int, stopExclusive: Int): Gen[Int] = {
    Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive-start)))
  }

  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = {
    boolean.flatMap(b => if (b) g1 else g2)
  }
}

sealed trait Result {
  def isFalsified: Boolean
}

case object Passed extends Result {
  def isFalsified = false
}

case class Falsified(failure: FailedCase, success: SuccessCount) extends Result {
  def isFalsified = true
}