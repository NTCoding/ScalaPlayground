package mongo_driver

import com.mongodb.{DB => MongoDB}
import scala.collection.convert.Wrappers._

class DB private(val underlying: MongoDB) {

	def collectionNames = {
		val names = new JSetWrapper(underlying.getCollectionNames)
		for(n <- names) yield n
	}

	private def collection(name: String) = underlying.getCollection(name)

	def readOnlyCollection(name: String) = {
		new DBCollection(collection(name)) with Memoizer
	}

	def administrableCollection(name: String) = {
		new DBCollection(collection(name)) with Adminstrable with Memoizer
	}

	def updatableCollection(name: String) = {
		new DBCollection(collection(name)) with Updatable with Memoizer
	}
}

object DB {
	def apply(underlying: MongoDB) = new DB(underlying)
}