import org.scalatest.FreeSpec
import org.scalatest.MustMatchers

class Spreadsheet_maths_functions_spec extends FreeSpec with MustMatchers{

    "When a range of cells have been populated with values" - {
        val sp = new Spreadsheet
        sp.setCellValue("C", 2, 4)
        sp.setCellValue("C", 3, 34.3)
        sp.setCellValue("C", 4, 99)
        sp.setCellValue("C", 5, 28.1)
        sp.setCellValue("D", 2, 1.1)
        sp.setCellValue("D", 3, 2.2)
        sp.setCellValue("D", 4, 3.3)
        sp.setCellValue("D", 5, 4.4)

        "Multi-line ranges can be selected" in {
            val range = sp.getRange(("C", 2), ("D", 5))
            range must equal(Vector(("C", 2, "4.0"), ("C", 3, "34.3"), ("C", 4, "99.0"), ("C", 5, "28.1"), ("D", 2, "1.1"), ("D", 3, "2.2"), ("D", 4, "3.3"), ("D", 5, "4.4")))
        }

        "Out of bounds ranges cause an error" in {
            intercept[GridCellOutOfRange](sp.getRange(("A", 0), ("A", 1)))
        }

        "Ranges can be summed" in {
            sp.sumRange(("C", 2), ("D", 5)) must equal(176.4)
        }

        "Number of non-empty cells in a range is returned by count" in {
            sp.countRange(("C", 1), ("D", 6)) must equal(8)
        }

        "Maximum value in range is returned by max" in {
            sp.maxInRange(("C", 2), ("D", 5)) must equal(99)
        }

        "Minimum value in range is returned by min" in {
            sp.minInRange(("C", 2), ("D", 5)) must equal(1.1)
        }
    }
}
