package types

object Variance {

  class Contra[+A]

  val c = new Contra[Int]
  val good: Contra[Any] = c
  //val bad: Contra[String] = c

  class Cov[-A]

  val cov = new Cov[Bbase]
  //val nowItsBad: Cov[Any] = cov // can't cast to subtype, even though it should be fine
  val butThisIsGood: Cov[CSub] = cov // can cast to subtype, even though it might break

  val x = List[_]()
  x ++ List(1)

  val oijy = List[X forSome { type X <: String }]()
  oijy :+ 1
  oijy :+ new Object()

}

class Bbase
class CSub extends Bbase
