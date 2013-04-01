package thermal_analyser

class SimpleScoreCalculator extends ScoreCalculator {
	
	def calculate(number: Int, grid: Grid) = {
		scoresFor(grid)
			.sortBy(_._3)
			.take(number)
	}

	def scoresFor(grid: Grid) = {
		var scores = List[(Int, Int, Int)]()
		for (row <- 0 to grid.rows.size - 1) {
			for (col <- 0 to grid.rows(row).size - 1) {
				scores = scores :+ (row + 1, col + 1, 1)
			}
		}
		scores
	}
}