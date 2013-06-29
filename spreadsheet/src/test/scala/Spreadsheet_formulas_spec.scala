import org.scalatest.FreeSpec
import org.scalatest.MustMatchers
import ImplicitFormulaConversions._

class Spreadsheet_formulas_spec extends FreeSpec with MustMatchers {


    "Using a spreadsheet with a number of populated ranges" - {
        val sp = new Spreadsheet
        sp.setCellValue("A", 1, 99.90)
        sp.setCellValue("A", 2, 13.22)
        sp.setCellValue("A", 3, 67.4)
        sp.setCellValue("B", 1, 1.1)
        sp.setCellValue("B", 2, 3.33)
        sp.setCellValue("B", 3, 29.0)
        sp.setCellValue("C", 1, 15.1)
        sp.setCellValue("C", 2, 9.90)
        sp.setCellValue("C", 3, 11.90)
        sp.setCellValue("D", 3, 99.90)
        sp.setCellValue("E", 2, 99.90)
        sp.setCellValue("E", 3, 99.90)

        "After Putting a count formula in a cell" - {
            sp.setCellValue("H", 10, CountFormula(("A", 1), ("E", 3)))

           "The formula is returned as the value for that cell" in {
                sp.getCellValue("H", 10) must equal("=COUNT(A1:E3)")
            }

            "But when printing the spreadsheet, the evaluation of the formula is shown" in {
                // this could be more robust, but comparing full output needs a bit more experimenting with
                sp.display must include("10                                                   3.0")
            }

        }

        "Putting a count formula in a cell with an invalid grid range causes an error" in {
            fail
        }
    }

    // sum formula

    // min formula

    // max formula
}
