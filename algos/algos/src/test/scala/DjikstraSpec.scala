import org.scalatest.{FreeSpec, Matchers}

import scala.annotation.tailrec
import scala.io.Source

class DjikstraSpec extends FreeSpec with Matchers {
  import Djikstra.Vertex

  "finds shortest path from source to each non-source node and penultimate node on path" - {

    "for graph with 3 nodes and only 1 path to each" in {
      val graph = Array(Vertex(0, 1, 5), Vertex(0, 2, 3))
      assert(Djikstra(graph, 0).weights === Array(0, 5, 3))
      assert(Djikstra(graph, 0).previousNodes === Array(Djikstra.noNode, 0, 0))
    }

    "for a 3-level deep graph with only 1 path to each node (a tree)" in {
      val graph = Array(
        Vertex(0, 1, 5), Vertex(1, 2, 4), Vertex(1, 3, 3), Vertex(0, 4, 6), Vertex(4, 5, 7), Vertex(4, 6, 25)
      )
      assert(Djikstra(graph, 0).weights === Array(0, 5, 9, 8, 6, 13, 31))
      assert(Djikstra(graph, 0).previousNodes === Array(Djikstra.noNode, 0, 1, 1, 0, 4, 4))
    }

    "for a 3-level deep graph with only 1 path to each node when the source is not 0" in {
      val graph = Array(
        Vertex(0, 4, 6), Vertex(4, 5, 7), Vertex(4, 6, 25), Vertex(1, 0, 5), Vertex(1, 3, 3), Vertex(2, 1, 4)
      )
      assert(Djikstra(graph, 2).weights === Array(9, 4, 0, 7, 15, 22, 40))
      assert(Djikstra(graph, 2).previousNodes === Array(1, 2, Djikstra.noNode, 1, 0, 4, 4))
    }
  }
}

object Djikstra {
  case class Vertex(start: Int, end: Int, weight: Int)

  case class ShortestPaths(weights: Array[Int], previousNodes: Array[Int]) {
    def update(node: Int, weight: Int, previousNode: Int) =
      copy(weights = weights.updated(node, weight), previousNodes.updated(node, previousNode))
  }

  val infinity = Integer.MAX_VALUE
  val noNode = -33

  def apply(graph: Array[Vertex], source: Int): ShortestPaths =
    findShortestPaths(graph :+ Vertex(source, source, 0), initialiseShortestPaths(graph, source))

  private def initialiseShortestPaths(graph: Array[Vertex], source: Int) =
    ShortestPaths(
      Array.fill(graph.length + 1)(Djikstra.infinity).updated(source, 0),
      Array.fill(graph.length + 1)(Djikstra.noNode)
    )

  @tailrec
  private def findShortestPaths(unchecked: Array[Vertex], shortestPaths: ShortestPaths): ShortestPaths =
    unchecked.isEmpty match {
      case true => shortestPaths
      case false =>
        val shortestPathVertex = unchecked.map(v => (v, shortestPaths.weights(v.end))).sortBy(_._2).head._1
        val adjacent = unchecked.filter(_.start == shortestPathVertex.end)
        val sps = relax(shortestPathVertex, adjacent, shortestPaths)
        findShortestPaths(unchecked.filterNot(_ == shortestPathVertex), sps)
    }

  @tailrec
  private def relax(vertex: Vertex, adjacentVertices: Seq[Vertex], shortestPaths: ShortestPaths): ShortestPaths =
    adjacentVertices.isEmpty match {
      case true => shortestPaths
      case false =>
        val totalPathWeight = shortestPaths.weights(vertex.end) + adjacentVertices.head.weight
        if (totalPathWeight < shortestPaths.weights(adjacentVertices.head.end)) {
          val sps = shortestPaths.update(adjacentVertices.head.end, totalPathWeight, vertex.end)
          relax(vertex, adjacentVertices.tail, sps)
        } else relax(vertex, adjacentVertices.tail, shortestPaths)
    }
}

