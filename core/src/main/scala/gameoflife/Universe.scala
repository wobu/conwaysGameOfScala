package gameoflife

object Universe {

  type X = Int
  type Y = Int

  trait Locatable {
    def x: X

    def y: Y

    def neighbours(world: World): Seq[Locatable] = {
      val yNeighbourBoundary = Math.max(0, y - 1) to Math.min(y + 1, world.dimension._2)
      val xNeighbourBoundary = Math.max(0, x - 1) to Math.min(x + 1, world.dimension._1)

      world.population.filterNot(_ == this).filter { cell =>
        yNeighbourBoundary.contains(cell.y) && xNeighbourBoundary.contains(cell.x)
      }
    }
  }

  case class LivingCell(x: X, y: Y, age: Int = 0) extends Locatable

  case class Nothing(x: X, y: Y) extends Locatable

  class World(val dimension: (X, Y), lifetime: Int, seed: Option[Seq[LivingCell]] = None) {
    var age: Int = 0
    var population: Seq[LivingCell] = seed.getOrElse(Seq.empty[LivingCell])

    /**
     * Life goes one.
     */
    def evolve(): Unit = {
      age += 1

      population = (population ++ nothingness).map(LawsOfNature(_, this)).filter {
        case _: LivingCell => true
        case _ => false
      }.map(_.asInstanceOf[LivingCell])
    }

    def locations: Seq[(X, Y)] = for (x <- 0 to dimension._1; y <- 0 to dimension._2) yield (x, y)

    def nothingness: Seq[Nothing] = locations.filterNot(l => population.exists(c => c.x == l._1 && c.y == l._2)).map(n => Nothing(n._1, n._2))
  }

  type LawOfNature = PartialFunction[(Locatable, World), Locatable]

  def LawsOfNature: LawOfNature = Birth orElse Life orElse UnderPopulation orElse Overcrowding orElse { case (c: Locatable, w: World) => c}

  val UnderPopulation: LawOfNature = {
    case (c: LivingCell, w: World) if c.neighbours(w).size < 2 => Nothing(c.x, c.y)
  }

  val Overcrowding: LawOfNature = {
    case (c: LivingCell, w: World) if c.neighbours(w).size > 3 => Nothing(c.x, c.y)
  }

  val Birth: LawOfNature = {
    case (c: Nothing, w: World) if c.neighbours(w).size == 3 => LivingCell(c.x, c.y)
  }

  val Life: LawOfNature = {
    case (c: LivingCell, w: World) if 2 to 3 contains c.neighbours(w).size => c.copy(age = c.age + 1)
  }
}
