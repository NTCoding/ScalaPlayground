package mongo_driver

trait Adminstrable extends ReadOnly {

	def drop: Unit = underlying drop
	def dropIndexes: Unit = underlying dropIndexes
}