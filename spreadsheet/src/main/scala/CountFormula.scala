
case class CountFormula(start: (String, Int), end: (String, Int)) extends Formula {

    def name: String = "COUNT"

}

case class SumFormula(start: (String, Int), end: (String, Int)) extends Formula {

    def name = "SUM"
}

case class MinFormula(start: (String, Int), end: (String, Int)) extends Formula {

    def name = "MIN"
}

case class MaxFormula(start: (String, Int), end: (String, Int)) extends Formula {

    def name = "MAX"
}

sealed trait Formula extends CellValue {

    def name: String
    def start: (String, Int)
    def end: (String, Int)

    override def toString = s"=$name(${start._1}${start._2}:${end._1}${end._2})"

}

case class NumericValue(value: Double) extends CellValue

sealed trait CellValue

object ImplicitFormulaConversions {

    implicit def doubleToNumericValue(d: Double) = NumericValue(d)
}