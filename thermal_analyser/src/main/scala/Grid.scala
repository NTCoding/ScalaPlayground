package thermal_analyser

class Grid(val rows: List[List[Int]]) { 

	def maxX = rows.head.size - 1

	def maxY = rows.size - 1
}