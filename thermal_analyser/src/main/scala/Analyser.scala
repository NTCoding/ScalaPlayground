package thermal_analyser

class Analyser(private val parser: ArgsParser, private val calc: ScoreCalculator, private val formatter: ResultFormatter) {

	def analyse(sizeAndGrid: String) = parser.parse(sizeAndGrid) match {

		case (size, grid) => formatter.format(calc.calculate(size, grid))

		case _ => "Error"	
	}
	
}