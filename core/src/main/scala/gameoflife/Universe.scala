package gameoflife

object Universe {

  type X = Int
  type Y = Int

  trait Cell {
    def x: X

    def y: Y
  }

  case class LivingCell(x: X, y: Y) extends Cell

  type Seed = Array[Cell]

  class World(dimension: (X, Y), lifetime: Int, seed: Option[Seed] = None) {
    var age: Int = 0
    var population: Array[Cell] = Array.empty
  }

  trait LawOfNature
}
