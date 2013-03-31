import mongo_driver._
import com.mongodb.BasicDBObject

object Test extends App {

	def client = new MongoClient()
	def db = client.createDB("mydb")	
	
	for (n <- db.collectionNames) println(n)

	val col = db.readOnlyCollection("test")
	println(col.name)

	val adminCol = db.administrableCollection("test")
	adminCol.drop

	val updatableCol = db.updatableCollection("test")

	val doc = new BasicDBObject()
	doc.put("name", "MongoDB")
	doc.put("type", "database")
	doc.put("count", 1)

	val info = new BasicDBObject()
	info.put("x", 203)
	info.put("y", 102)
	doc.put("info", info)
	updatableCol += doc

	println(updatableCol.findOne)

	updatableCol -= doc
	println(updatableCol.findOne)

	for(i <- 1 to 100) {
		updatableCol += new BasicDBObject("i", i)
	}

	val query = new BasicDBObject
	query.put("i", 71)
	val cursor = col.find(query)
	while(cursor.hasNext()) {
		println(cursor.next())
	}
}
