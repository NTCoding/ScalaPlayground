package thermal_analyser

class SimpleResultFormatter extends ResultFormatter {
	
	def format(result: List[(Int, Int, Int)]) = (for((x, y, score) <- result) yield f"($x,$y score:$score)").mkString("")
}