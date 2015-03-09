package gameoflife.web

import org.scalajs.dom
import org.scalajs.dom.{UIEvent, html}

import scala.scalajs.js.JSApp

object ConwaysGameOfLifeApp extends JSApp {

  import gameoflife.Universe._

  def main(): Unit = {
    val canvas = dom.document.getElementById("canvas").asInstanceOf[html.Canvas]
    val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    def resizeCanvas(): Unit = {
      canvas.width = dom.window.innerWidth
      canvas.height = dom.window.innerHeight
    }

    resizeCanvas()

    val grid = new Grid(canvas, context)
    val world = new World(() => (grid.columns, grid.lines), 10, seed = Some(Seq(LivingCell(0, 1), LivingCell(1, 1), LivingCell(2, 1))))

    def run() = {
      grid.draw(world.population)

      world.evolve()
    }

    dom.window.onresize = (e: UIEvent) => {
      resizeCanvas()
    }

    dom.setInterval(run _, 400)
  }
}
