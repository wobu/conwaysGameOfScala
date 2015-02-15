package gameoflife

import gameoflife.Universe._
import org.scalatest.{Matchers, FlatSpecLike}

class WorldSpec extends FlatSpecLike with Matchers {
  "Life on the World" should "go on" in {
    val seed = Seq(LivingCell(0, 1), LivingCell(1, 1), LivingCell(2, 1))
    val world = new World((2, 2), 2, seed = Some(seed))

    world.population should contain only (seed: _*)

    world.evolve()

    world.population should contain theSameElementsAs Seq(LivingCell(1, 2, age = 0), LivingCell(1, 1, age = 1), LivingCell(1, 0, age = 0))

    world.evolve()

    world.population should contain theSameElementsAs Seq(LivingCell(0, 1, age = 0), LivingCell(1, 1, age = 2), LivingCell(2, 1, age = 0))
  }
}
