package thermal_analyser_test

import thermal_analyser._
import org.junit._
import junit.framework.Assert._

class Args_parser_Test {

	@Test
	def parses_1x1_grid_with_1_result {

		val inputFor1x1GridWith1Result = "1 1 1"

		val (numberOfResults, grid) = new SimpleArgsParser().parse(inputFor1x1GridWith1Result)

		assertEquals(1, numberOfResults)
		assertEquals(List(List(1)), grid.rows)
	}

	@Test
	def parser_2x2_grid_with_2_results {

		val inputFor2x2GridWith2Results = "2 2 1 1 2 2"

		val (numberOfResults, grid) = new SimpleArgsParser().parse(inputFor2x2GridWith2Results)

		assertEquals(2, numberOfResults)
		assertEquals(List(List(1, 1), List(2, 2)), grid.rows)
	}

	// 3. 3 x 3 grid with 10 results

	// 4. 10 x 10 grid with 20 results
}