package thermal_analyser_test

import thermal_analyser._	
import org.mockito.Mockito._
import org.mockito.stubbing._
import org.junit._
import junit.framework.Assert._

class Analysert_Test {

	val numberOfResults = 4
	val grid = new Grid(List(List(1, 2, 3)))
	val formattedResult = "(99, 100 score:2)"
	val calcResults = List((1, 1, 25), (1, 2, 26))
	var result = "not important here"
	
	val parser = mock(classOf[ArgsParser])
	val calc = mock(classOf[ScoreCalculator])
	val formatter = mock(classOf[ResultFormatter])
	val analyser = new Analyser(parser, calc, formatter)
	
	@Before
	def when_given_valid_input {

		val input = "1 2 3 4 5 6 7 8 9"

		when(parser.parse(input)).thenReturn((numberOfResults, grid))
		when(calc.calculate(numberOfResults, grid)).thenReturn(calcResults)
		when(formatter.format(calcResults)).thenReturn(formattedResult)

		result = analyser.analyse(input)
	}

	@Test
	def uses_arguement_parser_and_score_builder_to_retrieve_top_n_items_then_formatter_to_generate_output {

		assertEquals(formattedResult, result)
	}
		

}