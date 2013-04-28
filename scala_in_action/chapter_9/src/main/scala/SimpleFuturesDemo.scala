import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

object SimpleFuturesDemo extends App {

  def main() {

    Future {
      println("Starting to execute future")
      Thread.sleep(10000)
      println("Future has awoken")
    }

    println("code has finished - will sleep for future")
    Thread.sleep(11000)
  }

  main()
}