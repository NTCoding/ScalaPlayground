import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{Matchers, FreeSpec}

import scala.annotation.tailrec

class DjikstraSpec extends FreeSpec with Matchers {
  import Djikstra.Vertex

  val examples = Table(
    ("Graph", "Source", "Shortest path weights", "Shortest path previous nodes"),
    (Array(Vertex(0, 1, 5), Vertex(0, 2, 3)), 0, Array(0, 5, 3), Array(Djikstra.noNode, 0, 0))
    // Larger graphs
    // Source is not 0
  )

  "Finds the shortest path from source to each non-source node" in {
    forAll(examples) { (graph, source, shortestPathWeights, shortestPathPreviousNodes) =>
      assert(Djikstra(graph, source).weights === shortestPathWeights)
      assert(Djikstra(graph, source).previousNodes === shortestPathPreviousNodes)
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
    findShortestPaths(Array(Vertex(0, 0, 0)) ++ graph, initialiseShortestPaths(graph))

  private def initialiseShortestPaths(graph: Array[Vertex]) =
    ShortestPaths(Array(0) ++ (1 to graph.length).map(_ => Djikstra.infinity), (0 to graph.length).map(_ => Djikstra.noNode).toArray)

  @tailrec
  private def findShortestPaths(unchecked: Array[Vertex], shortestPaths: ShortestPaths): ShortestPaths =
    unchecked.isEmpty match {
      case true => shortestPaths
      case false =>
        val shortestPathVertex = unchecked.map(v => (v, shortestPaths.weights(v.start))).sortBy(-_._2).head._1
        val adjacent = unchecked.filter(_.start == shortestPathVertex.end)
        val sps = relax(shortestPathVertex, adjacent, shortestPaths)
        findShortestPaths(unchecked.tail, sps)
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

