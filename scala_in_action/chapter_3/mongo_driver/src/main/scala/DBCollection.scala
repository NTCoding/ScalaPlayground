package mongo_driver

import com.mongodb.{DBCollection => MongoDBCollection}

class DBCollection(override val underlying: MongoDBCollection) extends ReadOnly {

}