package gameoflife.web

import org.scalajs.dom
import org.scalajs.dom.{Event, UIEvent, html}

import scala.scalajs.js.JSApp

object ConwaysGameOfLifeApp extends JSApp {

  import gameoflife.Universe._

  def main(): Unit = {
    val canvas = dom.document.getElementById("canvas").asInstanceOf[html.Canvas]
    val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val runButton = dom.document.getElementById("runButton").asInstanceOf[html.Input]
    val stopButton = dom.document.getElementById("stopButton").asInstanceOf[html.Input]
    val resetButton = dom.document.getElementById("resetButton").asInstanceOf[html.Button]

    def resizeCanvas(): Unit = {
      canvas.width = canvas.parentElement.clientWidth
      canvas.height = canvas.parentElement.clientHeight
    }

    resizeCanvas()

    val grid = new Grid(canvas, context)
    var world: Option[World] = None
    var interval: Option[Int] = None

    runButton.onchange = (e: Event) => {
      interval = Some(dom.setInterval(run _, 400))
    }

    stopButton.onchange = (e: Event) => {
      interval.foreach(dom.window.clearInterval)
      interval = None
    }

    resetButton.onclick = (e: UIEvent) => {
      world = None
      grid.draw(Seq.empty)
    }

    dom.window.onresize = (e: UIEvent) => {
      resizeCanvas()
    }

    def run() = {
      world = world match {
        case None => Some(new World(() => (grid.columns, grid.lines), 10, seed = Some(Seq(LivingCell(0, 1), LivingCell(1, 1), LivingCell(2, 1)))))
        case _ => world
      }

      world.foreach(_.evolve())
      world.foreach(w => grid.draw(w.population))
    }

    grid.draw(Seq.empty)
  }
}
