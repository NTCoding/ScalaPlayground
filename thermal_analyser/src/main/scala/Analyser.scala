package thermal_analyser

class Analyser(private val parser: ArgsParser, private val calc: ScoreCalculator, private val formatter: ResultFormatter) {

	def analyse(sizeAndGrid: String) = {
		val (size, grid) = parser.parse(sizeAndGrid)
		val scores = calc.calculate(size, grid)
		formatter.format(scores)
	}
	
}