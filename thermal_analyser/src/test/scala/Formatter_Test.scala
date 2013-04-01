package thermal_analyser_tests

import thermal_analyser._	
import org.junit._
import junit.framework.Assert._

class Formatter_Test {

	@Test
	def formats_1_result_as_x_y_score_score {

		val result = List((4, 3, 18))
		var formattedResult = new SimpleResultFormatter().format(result)

		assertEquals("(4,3 score:18)", formattedResult)
	}
}