package mongo_driver

import scala.collection.mutable._
import com.mongodb._

trait Memoizer extends ReadOnly {

	val history = Map[Int, DBObject]()

	override def findOne = {
		history.getOrElseUpdate(-1, { super.findOne })
	}

	override def findOne(doc: DBObject) = {
		history.getOrElseUpdate(doc.hashCode, { super.findOne(doc) })
	}
}