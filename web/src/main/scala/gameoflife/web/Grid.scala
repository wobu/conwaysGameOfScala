package gameoflife.web

import gameoflife.Universe.LivingCell
import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.html

class Grid(canvas: html.Canvas, context: dom.CanvasRenderingContext2D) {
  type X = Int
  type Y = Int

  val CellSpacing = 1
  val CellSize: (X, Y) = (10, 10)

  def lines: Int = canvas.height / (CellSize._2 + CellSpacing)
  def columns: Int = canvas.width / (CellSize._1 + CellSpacing)

  def draw(population: Seq[LivingCell]): Unit = {
    context.fillStyle = Color.White.toString()
    context.fillRect(0, 0, canvas.width, canvas.height);

    context.fillStyle = Color(224, 224, 224).toString()

    // draw grid lines
    (0 to lines).foreach { line =>
      context.fillRect(0, line * (CellSize._2 + CellSpacing), 1 + canvas.width - (canvas.width % columns), CellSpacing)
    }

    // draw grid columns
    (0 to columns).foreach { column =>
      context.fillRect(column * (CellSize._1 + CellSpacing), 0, CellSpacing, 1 + canvas.height - (canvas.height % lines))
    }

    context.fillStyle = Color(204, 255, 153).toString()
    population.foreach { cell =>
      val startX = CellSpacing + (cell.x * (CellSize._1 + CellSpacing))
      val startY = CellSpacing + (cell.y * (CellSize._2 + CellSpacing))
      context.fillRect(startX, startY, CellSize._1, CellSize._2)
    }
  }
}