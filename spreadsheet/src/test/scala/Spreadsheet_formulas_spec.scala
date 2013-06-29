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
                sp.display must include("10                                                   12")
            }

        }


        "After putting a sum formula in a cell" - {
            sp.setCellValue("H", 9, SumFormula(("A",2), ("B", 2)))

            "The formula is returned as the value for that cell" in {
                sp.getCellValue("H", 9) must equal("=SUM(A2:B2)")
            }

            "But when printing the spreadsheet, the evaluation of the formula is shown" in {
                sp.display must include("9                                                   16.55")
            }
        }


        "After putting a min formula in a cell" - {
            sp.setCellValue("H", 8, MinFormula(("B", 1), ("C", 3)))

            "The formula is returned as the value for that cell" in {
                sp.getCellValue("H", 8) must equal("=MIN(B1:C3)")
            }

            "But when printing the spreadsheet, the evaluation of the formula is shown" in {
                sp.display must include("8                                                   1.1")
            }
        }


        "After putting a max formula in a cell" - {
            sp.setCellValue("H", 7, MaxFormula(("B", 1), ("C", 3)))

            "The formula is returned as the value for that cell" in {
                sp.getCellValue("H", 7) must equal("=MAX(B1:C3)")
            }

            "But when printing the spreadsheet, the evaluation of the formula is shown" in {
                sp.display must include("7                                                   29.0")
            }
        }

        // TODO - Add some tests to check ranges inside formulas - simply reuse current validation logic
    }
}
