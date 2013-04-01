package thermal_analyser_tests

import thermal_analyser._	
import org.junit._
import junit.framework.Assert._

class Formatter_Test {

	@Test
	def formats_1_result_as_x_y_score_score {

		val result = List((4, 3, 18))
		val formattedResult = new SimpleResultFormatter().format(result)

		assertEquals("(4,3 score:18)", formattedResult)
	}

	@Test
	def formats_3_results_as_sequence_of_x_y_score {

		val result = List( (2, 2, 18), (3, 3, 5), (5, 3, 1) )
		val formattedResult = new SimpleResultFormatter().format(result)

		assertEquals("(2,2 score:18)(3,3 score:5)(5,3 score:1)", formattedResult)
	}
}