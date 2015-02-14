package gameoflife

object Universe {

  type X = Int
  type Y = Int

  trait Cell {
    def x: X

    def y: Y

    def livingNeighbours(world: World): Array[Cell] = {
      val yNeighbourBoundary = Math.max(0, y - 1) to Math.min(y + 1, world.dimension._2)
      val xNeighbourBoundary = Math.max(0, x - 1) to Math.min(x + 1, world.dimension._1)

      world.population.filter { cell =>
        yNeighbourBoundary.contains(cell.y) && xNeighbourBoundary.contains(cell.x)
      }
    }
  }

  case class LivingCell(x: X, y: Y, age: Int = 0) extends Cell

  case class Nothing(x: X, y: Y) extends Cell

  class World(val dimension: (X, Y), lifetime: Int, seed: Option[Array[LivingCell]] = None) {
    var age: Int = 0
    var population: Array[Cell] = seed.getOrElse(Array.empty[Cell]).asInstanceOf[Array[Cell]]
  }

  type LawOfNature = PartialFunction[(Cell, World), Cell]

  def LawsOfNature: LawOfNature = Birth orElse Life orElse UnderPopulation orElse Overcrowding orElse { case (c: Cell, w: World) => c}

  val UnderPopulation: LawOfNature = {
    case (c: LivingCell, w: World) if c.livingNeighbours(w).size < 2 => Nothing(c.x, c.y)
  }

  val Overcrowding: LawOfNature = {
    case (c: LivingCell, w: World) if c.livingNeighbours(w).size > 3 => Nothing(c.x, c.y)
  }

  val Birth: LawOfNature = {
    case (c: Nothing, w: World) if c.livingNeighbours(w).size == 3 => LivingCell(c.x, c.y)
  }

  val Life: LawOfNature = {
    case (c: LivingCell, w: World) if 2 to 3 contains c.livingNeighbours(w).size => c.copy(age = c.age + 1)
  }
}
