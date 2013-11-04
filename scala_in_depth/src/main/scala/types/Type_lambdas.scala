package types

object Type_lambdas {

  def iLikeTypeLambdas[A[_]](x: A[Int]) { println(x.getClass) }

  iLikeTypeLambdas[({type X[Y] = List[Int]})#X](List(1))
}
