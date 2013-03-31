package thermal_analyser

trait ScoreCalculator {
	
	def calculate(number: Int, grid: Grid) : List[(Int, Int, Int)]
}