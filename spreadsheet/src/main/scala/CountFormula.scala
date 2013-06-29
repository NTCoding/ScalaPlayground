
case class CountFormula(start: (String, Int), end: (String, Int)) extends Formula{

    def name: String = "COUNT"

}


case class NumericValue(value: Double) extends CellValue


sealed trait Formula extends CellValue {

    def name: String
    def start: (String, Int)
    def end: (String, Int)

    override def toString = s"=$name(${start._1}${start._2}:${end._1}${end._2})"

}


sealed trait CellValue


object ImplicitFormulaConversions {

    implicit def doubleToNumericValue(d: Double) = NumericValue(d)
}