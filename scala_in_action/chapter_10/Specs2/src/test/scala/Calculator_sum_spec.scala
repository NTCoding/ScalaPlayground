import org.specs2.mutable._
import Specs2_demo._

class Calculator_sum_spec extends Specification {

	"Calculator" should {

		"sum 2 numbers" in {
			
			val c = new Calculator
			var result = c.sum(1, 1)

			result must beEqualTo(2)
		}
	}

}