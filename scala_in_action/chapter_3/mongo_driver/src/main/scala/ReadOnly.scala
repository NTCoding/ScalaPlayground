package mongo_driver

import com.mongodb.{DBCollection => MongoDBCollection }
import com.mongodb.DBObject

trait ReadOnly {

	val underlying: MongoDBCollection

	def name = underlying getName
	def fullName = underlying getFullName
	def find(doc: DBObject) = underlying find doc
	def findOne(doc: DBObject) = underlying findOne doc
	def findOne = underlying findOne
	def getCount(doc: DBObject) = underlying getCount doc

}