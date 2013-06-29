import scala.collection.immutable.{IndexedSeq, HashMap}

class Spreadsheet {

    private val validColumns = Array("A", "B", "C", "D", "E", "F", "G", "H")
    private var data = HashMap[(String,Int), CellValue]()

    def getCellValue(column: String, row: Int): String = {
        data get(column, row) match {
            case Some(NumericValue(value)) => value.toString
            case Some(formula: Formula) => formula.toString
            case None => ""
        }
    }

    def getCellDisplayValue(column: String, row: Int): String = {
        data get(column, row) match {
            case Some(NumericValue(value)) => value.toString
            case Some(CountFormula(start, end)) => countRange(start, end).toString
            case Some(SumFormula(start, end)) => sumRange(start, end).toString
            case Some(MinFormula(start, end)) => minInRange(start, end).toString
            case Some(MaxFormula(start, end)) => maxInRange(start, end).toString
            case None => ""
        }
    }

    def setCellValue(column: String, row: Int, value: CellValue) {
        cellMustBeWithinGridRange(column, row)
        data += (column, row) -> value
    }

    def emptyCell(column: String, row: Int) {
        cellMustBeWithinGridRange(column, row)
        data -= ((column, row))
    }

    /*
      With more time I could make the display adjust to the size of the number of decimal places
      in each cell. For now I assumed max 2dp
     */
    def display = {
        val headers = validColumns map(_ + "      ") mkString ""
        val cellValues = for(r <- 1 to 10; c <- validColumns) yield getCellDisplayValue(c, r) padTo(7, " ") mkString("")
        val rowValues = cellValues grouped(8) map(_.mkString("")) toIndexedSeq
        val fullRows = for(i <- 0 to rowValues.length - 1) yield s"${i + 1}  ${rowValues(i)}"
        "   " ++ headers ++ sys.props("line.separator") ++ (fullRows mkString sys.props("line.separator"))
    }

    def getRange(start: (String, Int), end: (String, Int)) = {
        cellMustBeWithinGridRange(start._1, start._2)
        cellMustBeWithinGridRange(end._1, end._2)
        val startCol = validColumns.indexOf(start._1)
        val endCol = validColumns.indexOf(end._1)
        val startRow = start._2
        val endRow = end._2
        for (c <- startCol to endCol; r <- startRow to endRow) yield(validColumns(c), r, getCellValue(validColumns(c), r))
    }

    /*
        could put the maths functions in a new class - but that means 2 classes and a 3rd class to coordinate
        between them - unnecessary complexity imo
     */
    def sumRange(start: (String, Int), end: (String, Int)) = getNonEmptyValuesInRange(start, end).sum

    def countRange(start: (String, Int), end: (String, Int)) = getNonEmptyValuesInRange(start, end).length

    def maxInRange(start: (String, Int), end: (String, Int)) = getNonEmptyValuesInRange(start, end).max

    def minInRange(start: (String, Int), end: (String, Int)) = getNonEmptyValuesInRange(start, end).min

    private def getNonEmptyValuesInRange(start: (String, Int), end: (String, Int)) = getRange(start, end) filter(_._3 != "") map(_._3.toDouble)

    private def cellMustBeWithinGridRange(column: String, row: Int) {
        val isInRange = validColumns.contains(column) && row >= 1 && row <= 10
        if(!isInRange) throw new GridCellOutOfRange
    }
}

class GridCellOutOfRange extends Exception
