package gameoflife

import gameoflife.Universe.LivingCell

object RandomLifeGenerator {
  def apply(dimensions: (X, Y)): Seq[LivingCell] = {
    val randomLocations = for {
      x <- 0 to dimensions._1
      y <- 0 to dimensions._2
      if math.random < 0.5
    } yield (x, y)

    randomLocations.map(l => LivingCell(l._1, l._2))
  }
}
