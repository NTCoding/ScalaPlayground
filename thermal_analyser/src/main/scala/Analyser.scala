package thermal_analyser

class Analyser(private val parser: ArgsParser, private val calc: ScoreCalculator, private val formatter: ResultFormatter) {

	def analyse(sizeAndGrid: String) = {
		val (numberOfResults, grid) = parser.parse(sizeAndGrid)
		val scores = calc.calculate(numberOfResults, grid)
		formatter.format(scores)
	}
	
}