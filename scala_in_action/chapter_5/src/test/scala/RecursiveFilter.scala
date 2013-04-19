
import annotation.tailrec
import org.junit.Test
import junit.framework.Assert._

class RecursiveFilter {

    @Test
    def removes_duplicate_elements_with_a_recursive_function {
        val listWithDuplicates = List(1,2, 2, 3, 3, 3, 5, 9, 10, 10, 10)
        val listWithUniques = List(1, 2, 3, 5, 9, 10)

        val result = filter_recursive(listWithDuplicates)

        assertEquals(result, listWithUniques)
    }

    @tailrec
    final def filter_rec(list: List[Int], filtered: Set[Int]): Set[Int] = {
        list match {
            case Nil => filtered
            case head :: tail => filter_rec(tail, filtered + head)
        }
    }

    def filter_recursive(list: List[Int]) = {
        filter_rec(list, Set()).toList.sorted
    }
}
