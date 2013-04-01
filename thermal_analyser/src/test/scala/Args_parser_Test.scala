package thermal_analyser_test

import thermal_analyser._
import org.junit._
import junit.framework.Assert._

class Args_parser_Test {

	// 1. 1 x 1 grid with 1 result
	@Test
	def parses_1x1_grid_with_1_result {

		val inputFor1x1GridWith1Result = "1 1 1"

		val (numberOfResults, grid) = new SimpleArgsParser().parse(inputFor1x1GridWith1Result)

		assertEquals(1, numberOfResults)
		assertEquals(List(List(1)), grid.rows)
	}

	// 2. 2 x 2 grid with 4 results

	// 3. 3 x 3 grid with 10 results

	// 4. 10 x 10 grid with 20 results
}