package thermal_analyser_tests

import thermal_analyser._
import org.junit._
import junit.framework.Assert._

class Score_calculator_Test {
	
	@Test
	def calculates_top_1_score_from_1x1_grid {

		val grid = new Grid(List(List(5)))
		val scores = new SimpleScoreCalculator().calculate(1, grid)

		assertEquals(List((1, 1, 1)), scores)
	}

	// top 1 score from 2 x 2 grid

	// top 1 score from 5 x 5 grid

	// top 2 scores from 2 x 2

	// top 3 scores from 4 x 4 grid
}