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
	def parses_2x2_grid_with_2_results {

		val inputFor2x2GridWith2Results = "2 2 1 1 2 2"

		val (numberOfResults, grid) = new SimpleArgsParser().parse(inputFor2x2GridWith2Results)

		assertEquals(2, numberOfResults)
		assertEquals(List(List(1, 1), List(2, 2)), grid.rows)
	}

	@Test
	def parses_5x5_grid_with_10_results {

		val inputFor5x5GridWith10Results = "10 5 1 1 1 1 1 2 2 2 2 2 3 3 3 3 3 4 4 4 4 4 5 5 5 5 5"

		val (numberOfResults, grid) = new SimpleArgsParser().parse(inputFor5x5GridWith10Results)

		assertEquals(10, numberOfResults)

		val gridForInput = List( List(1, 1, 1, 1, 1), List(2, 2, 2, 2, 2), List(3, 3, 3, 3, 3), List(4, 4, 4, 4, 4), List(5, 5, 5, 5, 5) )
		assertEquals(gridForInput, grid.rows)
	}
}