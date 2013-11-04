package types

import javax.security.auth.callback.Callback

object TypeParamsHigherKindedTypes {

  //def eat[A](food: A) <- A is the type parameter (generic type)

  type Callback[T] = Function1[T, Unit] // Callback is the higher kinded type

  val cb: Callback[String] = y => println(y)


  type SpecialPowers[A] = Map[String, A]
  case class Power(name: String)

  val powers: SpecialPowers[Power] = Map("power1" -> new Power("mega blast"))

  def methodWithLotsOfTypeParams[A,B,C,D](tupez: (A,B,C,D)) { println("Yeeloo") }

  type ISimplifyABCDTuples[A] = (A, String, String, String)

  val t: ISimplifyABCDTuples[String] = ("yay", "woo", "moo", "meow")

  methodWithLotsOfTypeParams(t)

}
