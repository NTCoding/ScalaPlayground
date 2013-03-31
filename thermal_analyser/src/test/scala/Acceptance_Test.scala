package thermal_analyser_tests

import thermal_analyser._
import org.junit._
import junit.framework.Assert._

class Acceptance_Test {

	@Test
	def test_case_1 {

		val input = "1 5 5 3 1 2 0 4 1 1 3 2 2 3 2 4 3 0 2 3 3 2 1 0 2 4 3"
		val coordAndScore = new Analyser(new SimpleArgsParser(), new SimpleScoreCalculator(), new SimpleResultFormatter()).analyse(input)

		val correctCoordAndScoreForThisInput = "(3,3 score:26)"

		assertEquals(correctCoordAndScoreForThisInput, coordAndScore)
	}

	@Test
	def test_case_2 {

		val input = "3 4 2 3 2 1 4 4 2 0 3 4 1 1 2 3 4 4"
		val coordsAndScores = new Analyser(new SimpleArgsParser(), new SimpleScoreCalculator(), new SimpleResultFormatter()).analyse(input)

		val correctCoordsAndScoresForThisInput = "(1,2 score:27)(1,1 score:25)(2,2 score: 23)"

		assertEquals(correctCoordsAndScoresForThisInput, coordsAndScores)
	}
}