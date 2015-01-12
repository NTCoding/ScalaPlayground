
import com.codahale.jerkson.Json
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.{DateTimeComparator, DateTime}
import org.apache.spark.SparkContext._

object Demo {
  Logger.getRootLogger.setLevel(Level.ERROR)

  def main(args: Array[String]): Unit = {
    // this could also be an Amazon s3 file path. e.g: s3n://username:password@host/bucket/logfile.log.gz
    val testDataFilePath = this.getClass.getResource("sample_events.log").getPath

    val spark = createInMemorySparkInstance()
    val logs = spark.textFile(testDataFilePath)

    // reduce the working set first to improve performance
    // RDD is the Spark abstraction
    val pageViews: RDD[Event] = logs.filter(_.contains("pageview")) // may allow through some non-pageview events
                                    .map(Json.parse[Event])
                                    .filter(_.event == "pageview") // definitely filter out non-pageview events

    val userSessions = pageViews.groupBy(_.sessionId)
                                .filter(_._2.size > 0) // session must have 1 page view

    val sortedSessions = sortEachSessionChronologically(userSessions).filter(_._2.length > 0)

    val journeys: RDD[(String, Int)] = sortedSessions.map(s => (s._2.map(_.url.get).mkString(","), 1))

    // use second item in journey tuples to count occurences of that journey
    val commonJourneysWithTotals = journeys.reduceByKey((count1, count2) => count1 + count2)
                                           .sortBy(-_._2) // sort by count descending

    // print the results to the console
    commonJourneysWithTotals.take(10).foreach { j =>
      val journey = j._1.replace("http://", "") // easier to discern in console
      val count = j._2
      println(s"$count - $journey")
    }

    spark.stop()
  }

  private def createInMemorySparkInstance() = {
    // see spark documentation for explanation of thse: https://spark.apache.org/docs/1.1.1/configuration.html
    val conf = new SparkConf()
      .set("spark.storage.memoryFraction", "0.1")
      .set("spark.executor.memory", "3g")

    new SparkContext(master = "local", appName = "common_user_journey_example", conf)
  }

  private def sortEachSessionChronologically(userSessions: RDD[(Option[String], Iterable[Event])]) = {
    userSessions.map { session =>
      val sessionId = session._1
      val pageViews = session._2
      val chronologicalPageViews = pageViews.toSeq.sortBy(pv => DateTime.parse(pv.timestamp.get))(orderer)
      (sessionId, chronologicalPageViews.take(3)) // just using first 3 pageview for this example
    }
  }

  private def parseJourneys(sessions: RDD[(Option[String], Seq[Event])]) = {
    sessions.map { session =>      val journey = session
      (session._1, journey)
    }
  }

  val orderer = new DateTimeOrder
}

case class Event(event: String, url: Option[String], sessionId: Option[String], timestamp: Option[String])

class DateTimeOrder extends scala.math.Ordering[DateTime] {
  override def compare(x: DateTime, y: DateTime): Int = DateTimeComparator.getInstance.compare(x, y)
}
