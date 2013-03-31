package thermal_analyser

trait ResultFormatter {
	
	def format(result: List[(Int, Int, Int)]) : String
}