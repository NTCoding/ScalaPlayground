import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.annotation.tailrec

class QuickSortSpec extends FreeSpec with Matchers {

  val examples = Table(
    ("unsorted", "sorted"),
    (Array(3, 2, 1), Array(1, 2, 3)),
    (Array(10, 20, 11, 15, 16, 13, 45, 44, 300), Array(10, 11, 13, 15, 16, 20, 44, 45, 300))
  )

  "Sorts array in ascending order" in {
    forAll(examples) { (unsorted, sorted) =>
      assert(QuickSort(unsorted) === sorted)
    }
  }

  "Handles empty array with empty array" in {
    assert(QuickSort(Seq.empty) === Seq.empty)
  }

  "Echoes single-item array" in {
    assert(QuickSort(Seq(5)) === Seq(5))
  }
}

/*
   An implementation of quick sort using minimal pre-existing functions
 */
object QuickSort {
  def apply(unsorted: Seq[Int]): Seq[Int] = unsorted.length match {
    case 0 => Seq.empty
    case 1 => unsorted
    case 2 => if (unsorted.head <= unsorted.last) unsorted else unsorted.reverse
    case _ =>
      val pivot = unsorted(unsorted.length / 2)
      val p = partition(unsorted, pivot)
      QuickSort(p.smaller) ++ p.equal ++ QuickSort(p.greater)
  }

  @tailrec
  private def partition(input: Seq[Int], pivot: Int, result: PartitionResult = PartitionResult.empty): PartitionResult =
    input.size match {
      case 0 => result
      case _ if input.head < pivot =>
        partition(input.tail, pivot, result.addSmaller(input.head))
      case _ if input.head == pivot =>
        partition(input.tail, pivot, result.addEqual(input.head))
      case _ =>
        partition(input.tail, pivot, result.addGreater(input.head))
    }

  object PartitionResult {
    def empty = PartitionResult(Seq.empty, Seq.empty, Seq.empty)
  }

  case class PartitionResult(smaller: Seq[Int], equal: Seq[Int], greater: Seq[Int]) {
    def addSmaller(x: Int) = this.copy(smaller = smaller :+ x)
    def addEqual(x: Int) = this.copy(equal = equal :+ x)
    def addGreater(x: Int) = this.copy(greater = greater :+ x)
  }
}
