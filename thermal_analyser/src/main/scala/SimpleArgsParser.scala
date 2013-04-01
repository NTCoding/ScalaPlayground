package thermal_analyser

class SimpleArgsParser extends ArgsParser {
	
	def parse(sizeAndGrid: String) = {
		val chars = sizeAndGrid.split(" ")
		val numberOfResults = chars(0).toInt
		val gridSize = chars(1).toInt
		(numberOfResults, new Grid(buildRows(gridSize, chars)))
	}

	def buildRows(gridSize: Int, numbers: Array[String]) = {
		var rows = List[List[Int]]()
		var list = List[Int]()

		println("numbers: " + numbers.mkString(","))
		
		for (i <- 2 to numbers.size - 1) {
			println("i: " + i + " list size: " + list.size + " rows: " + rows.size)
			if (list.size == gridSize) {
				rows = rows :+ list
				list = List(numbers(i).toInt)
			} 
			else {
				list = list :+ numbers(i).toInt
			}
		}
		rows :+ list
	}
}