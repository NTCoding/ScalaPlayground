import scala.collection.immutable.HashMap

class Spreadsheet {

    private val validColumns = Array("A", "B", "C", "D", "E", "F", "G", "H")
    private var data = HashMap[(String,Int), Double]()

    def getCellValue(column: String, row: Int): String = {
        data get(column, row) match {
            case Some(value) => value.toString
            case None => ""
        }
    }

    def setCellValue(column: String, row: Int, value: Double) {
        cellMustBeWithinGridRange(column, row)
        data += (column, row) -> value
    }

    def emptyCell(column: String, row: Int) {
        cellMustBeWithinGridRange(column, row)
        data -= ((column, row))
    }

    private def cellMustBeWithinGridRange(column: String, row: Int) {
        val isInRange = validColumns.contains(column) && row >= 1 && row <= 10
        if(!isInRange) throw new GridCellOutOfRange
    }
}

class GridCellOutOfRange extends Exception
