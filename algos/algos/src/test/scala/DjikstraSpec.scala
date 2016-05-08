import org.scalatest.{FreeSpec, Matchers}

import scala.annotation.tailrec

class DjikstraSpec extends FreeSpec with Matchers {
  import Djikstra.Edge

  "finds shortest path from source to each non-source node and penultimate node on path" - {

    "for graph with 3 nodes and only 1 path to each" in {
      val graph = Array(Edge(0, 1, 5), Edge(0, 2, 3))
      assert(Djikstra(graph, 0).weights === Array(0, 5, 3))
      assert(Djikstra(graph, 0).previousNodes === Array(Djikstra.noNode, 0, 0))
    }

    "for a 3 level deep graph with only 1 path to each node (a tree)" in {
      val graph = Array(
        Edge(0, 1, 5), Edge(1, 2, 4), Edge(1, 3, 3), Edge(0, 4, 6), Edge(4, 5, 7), Edge(4, 6, 25)
      )
      assert(Djikstra(graph, 0).weights === Array(0, 5, 9, 8, 6, 13, 31))
      assert(Djikstra(graph, 0).previousNodes === Array(Djikstra.noNode, 0, 1, 1, 0, 4, 4))
    }

    "for a 3 level deep graph with only 1 path to each node when the source is not 0" in {
      val graph = Array(
        Edge(0, 4, 6), Edge(4, 5, 7), Edge(4, 6, 25), Edge(1, 0, 5), Edge(1, 3, 3), Edge(2, 1, 4)
      )
      assert(Djikstra(graph, 2).weights === Array(9, 4, 0, 7, 15, 22, 40))
      assert(Djikstra(graph, 2).previousNodes === Array(1, 2, Djikstra.noNode, 1, 0, 4, 4))
    }

    "for a 7 node graph with multiple paths to some nodes" in {
      val graph = Array(
        Edge(0, 1, 7), Edge(1, 2, 5), Edge(2, 5, 1), Edge(5, 6, 1),
        Edge(0, 3, 3), Edge(3, 4, 3), Edge(4, 2, 1), Edge(4, 6, 10)
      )
      assert(Djikstra(graph, 0).weights === Array(0, 7, 7, 3, 6, 8, 9))
      assert(Djikstra(graph, 0).previousNodes === Array(Djikstra.noNode, 0, 4, 0, 3, 2, 5))
    }
  }

  "mandates that all nodes are sequentially numbered starting from 0" in {
    intercept[Djikstra.NonSequentialNodeNumbers](Djikstra(Array(Edge(0, 1, 5), Edge(0, 3, 5)), 0))
  }
}

object Djikstra {
  val infinity = Integer.MAX_VALUE
  val noNode = -33

  def apply(graph: Array[Edge], source: Int): ShortestPaths = {
    val nodes = graph.map(_.end).distinct
    if ((nodes ++ Seq(source)).sorted.toSeq != (0 to nodes.max)) throw NonSequentialNodeNumbers()
    findShortestPaths(graph :+ Edge(source, source, 0), (0 to nodes.length).toArray, initialiseShortestPaths(nodes.length + 1, source))
  }

  private def initialiseShortestPaths(numberOfNodes: Int, source: Int) =
    ShortestPaths(
      Array.fill(numberOfNodes)(Djikstra.infinity).updated(source, 0),
      Array.fill(numberOfNodes)(Djikstra.noNode)
    )

  @tailrec
  private def findShortestPaths(graph: Seq[Edge], unchecked: Array[Int], shortestPaths: ShortestPaths): ShortestPaths =
    if (unchecked.isEmpty) shortestPaths else {
      val shortestPathVertex = unchecked.map(u => (u, shortestPaths.weights(u))).sortBy(_._2).head._1
      val adjacent = graph.filter(_.start == shortestPathVertex)
      val sps = relax(shortestPathVertex, adjacent, shortestPaths)
      findShortestPaths(graph, unchecked.filterNot(_ == shortestPathVertex), sps)
    }

  @tailrec
  private def relax(vertex: Int, adjacent: Seq[Edge], shortestPaths: ShortestPaths): ShortestPaths =
    if (adjacent.isEmpty) shortestPaths else {
      val totalPathWeight = shortestPaths.weights(vertex) + adjacent.head.weight
      if (totalPathWeight < shortestPaths.weights(adjacent.head.end)) {
        val sps = shortestPaths.update(adjacent.head.end, totalPathWeight, vertex)
        relax(vertex, adjacent.tail, sps)
      } else relax(vertex, adjacent.tail, shortestPaths)
    }

  case class Edge(start: Int, end: Int, weight: Int)

  case class ShortestPaths(weights: Array[Int], previousNodes: Array[Int]) {
    def update(node: Int, weight: Int, previousNode: Int) =
      copy(weights = weights.updated(node, weight), previousNodes.updated(node, previousNode))
  }

  case class NonSequentialNodeNumbers(msg: String = "") extends Exception(msg)
}

