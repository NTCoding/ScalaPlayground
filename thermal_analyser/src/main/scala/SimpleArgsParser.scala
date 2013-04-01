package thermal_analyser

class SimpleArgsParser extends ArgsParser {
	
	def parse(sizeAndGrid: String) = {
		val chars = sizeAndGrid.split(" ")
		val numberOfResults = chars(0).toInt
		val gridSize = chars(1).toInt
		val gridValues = chars.splitAt(2)._2.map(_.toInt).toList
		(numberOfResults, new Grid(split(gridSize, List[List[Int]](), gridValues)))
	}

	def split(gridSize: Int, rows: List[List[Int]], remaining: List[Int]) : List[List[Int]] = {
		remaining.size match {
			case 0 => rows 
			case _ => split(gridSize, rows :+ remaining.splitAt(gridSize)._1, remaining.splitAt(gridSize)._2)
		}
	}
}