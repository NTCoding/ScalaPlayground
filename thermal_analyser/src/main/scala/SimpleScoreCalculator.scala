package thermal_analyser

class SimpleScoreCalculator extends ScoreCalculator {
	
	def calculate(number: Int, grid: Grid) = {
		scoresFor(grid)
				.sortBy(_._3)
				.reverse
				.take(number)
	}

	def scoresFor(grid: Grid) = {
		var scores = List[(Int, Int, Int)]()
		for (row <- 0 to grid.maxX) {
			for (col <- 0 to grid.maxY) {
				scores = scores :+ (row , col, scoreFor(row, col, grid))
			}
		}
		scores
	}

	def scoreFor(row: Int, col: Int, grid: Grid) = {
		aboveLeft(row, col, grid) + aboveCentre(row, col, grid) + aboveRight(row, col, grid) + 
		left(row, col, grid) + score(row, col, grid) + right(row, col, grid) + 
		belowLeft(row, col, grid) + belowCentre(row, col, grid) + belowRight(row, col, grid)
	}

	def aboveLeft(row: Int, col: Int, grid: Grid) = scoreOrZero(row > 0 && col > 0, row - 1, col - 1, grid)

	def aboveCentre(row: Int, col: Int, grid: Grid) = scoreOrZero(row > 0, row - 1, col, grid)

	def aboveRight(row: Int, col: Int, grid: Grid) = scoreOrZero(row > 0 && col < grid.maxX, row - 1, col + 1, grid)

	def left(row: Int, col: Int, grid: Grid) = scoreOrZero(col > 0, row, col - 1, grid)

	def right(row: Int, col: Int, grid: Grid) = scoreOrZero(col < grid.maxX, row, col + 1, grid)

	def belowLeft(row: Int, col: Int, grid: Grid) = scoreOrZero(row < grid.maxY && col > 0, row + 1, col - 1, grid)

	def belowCentre(row: Int, col: Int, grid: Grid) = scoreOrZero(row < grid.maxY, row + 1, col, grid)

	def belowRight(row: Int, col: Int, grid: Grid) = scoreOrZero(row < grid.maxY && col < grid.maxX, row + 1, col + 1, grid)

	def scoreOrZero(condition: => Boolean, row: Int, col: Int, grid: Grid) = if (condition) score(row, col, grid) else 0

	def score(row: Int, col: Int, grid: Grid) = grid.rows(row)(col)
}