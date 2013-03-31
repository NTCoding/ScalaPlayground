package mongo_driver

import com.mongodb._

trait Updatable extends ReadOnly {
	
	def -= (doc: DBObject): Unit = underlying remove doc
	def += (doc: DBObject): Unit = underlying save doc
}