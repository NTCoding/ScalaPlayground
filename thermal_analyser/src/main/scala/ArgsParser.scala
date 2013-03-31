package thermal_analyser

trait ArgsParser {
	
	def parse(sizeAndGrid: String) : (Int, Grid)
}