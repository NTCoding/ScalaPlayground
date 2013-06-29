
case class CountFormula(start: (String, Int), end: (String, Int)) extends CellValue

case class NumericValue(value: Double) extends CellValue

sealed trait CellValue

object ImplicitFormulaConversions {

    implicit def doubleToNumericValue(d: Double) = NumericValue(d)
}