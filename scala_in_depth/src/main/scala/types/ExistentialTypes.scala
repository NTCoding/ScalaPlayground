package types

object ExistentialTypes {

  def exyZegz[A[_]](x: A[Int]) { println(x.toString) }

  exyZegz(List(1, 2, 4)) // allowed because List[Int] - 1 type parameter

  //exyZegz(Map[Int, Int](1, 1)) //not allowed because 2 type params
}
