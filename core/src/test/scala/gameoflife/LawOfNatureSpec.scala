package gameoflife

import org.scalatest.{Matchers, FlatSpecLike}
import org.scalatest.PartialFunctionValues._
import org.scalatest.Inside._
import gameoflife.Universe._

class LawOfNatureSpec extends FlatSpecLike with Matchers {
  "A living Cell" should "die on under population" in {
    val seed = Array(LivingCell(0, 0))

    val result = LawsOfNature.valueAt(LivingCell(1, 1), new World((1, 1), 1, seed = Some(seed)))

    result shouldBe a[Nothing]
    inside(result) { case Nothing(x, y) =>
      x should be(1)
      x should be(1)
    }
  }

  "A living Cell" should "die on overcrowding" in {
    val seed = Array(LivingCell(0, 0), LivingCell(1, 0), LivingCell(0, 1), LivingCell(2, 0))

    val result = LawsOfNature.valueAt(LivingCell(1, 1), new World((2, 2), 1, seed = Some(seed)))

    result shouldBe a[Nothing]
    inside(result) { case Nothing(x, y) =>
      x should be(1)
      x should be(1)
    }
  }

  "A living Cell" should "continue living" in {
    val seed = Array(LivingCell(0, 0), LivingCell(1, 0))

    val result = LawsOfNature.valueAt(LivingCell(1, 1), new World((1, 1), 1, seed = Some(seed)))

    result shouldBe a[LivingCell]
    inside(result) { case LivingCell(x, y, age) =>
      x should be(1)
      x should be(1)
      age should be(1)
    }
  }

  "Out of nothing" should "be giving birth to a new cell" in {
    val seed = Array(LivingCell(0, 0), LivingCell(1, 0), LivingCell(0, 1))

    val result = LawsOfNature.valueAt(Nothing(1, 1), new World((1, 1), 1, seed = Some(seed)))

    result shouldBe a[LivingCell]
    inside(result) { case LivingCell(x, y, age) =>
      x should be(1)
      x should be(1)
      age should be(0)
    }
  }
}
