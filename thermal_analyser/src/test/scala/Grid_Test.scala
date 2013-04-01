package thermal_analyser_tests

import thermal_analyser._
import org.junit._
import junit.framework.Assert._

class Grid_Test {

	@Test
	def max_x_and_max_y_are_0_for_1x1_grid {

		val g = new Grid(List(List(1)))

		assertEquals(0, g.maxX)
		assertEquals(0, g.maxY)
	}

	@Test
	def max_x_and_max_y_are_1_for_2x2_grid {

		val g = new Grid(List( List(1, 1), List(1, 1) ))

		assertEquals(1, g.maxX)
		assertEquals(1, g.maxY)
	}

	@Test
	def max_x_and_max_y_are_10_for_11x11_grid {

		val rows = (1 to 11).map(i => List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)).toList

		val g = new Grid(rows)

		assertEquals(10, g.maxX)
		assertEquals(10, g.maxY)
	}
}