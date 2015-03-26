package gameoflife.web

import gameoflife.Universe._
import org.scalajs.dom
import org.scalajs.dom.{Event, UIEvent, html}

import scala.scalajs.js.JSApp

object ConwaysGameOfLifeApp extends JSApp {

  object ControlUnits {
    val run = dom.document.getElementById("runButton").asInstanceOf[html.Input]
    val stop = dom.document.getElementById("stopButton").asInstanceOf[html.Input]
    val reset = dom.document.getElementById("resetButton").asInstanceOf[html.Button]
  }

  trait Renderer {
    def world: Option[World]

    private[this] val canvas = dom.document.getElementById("canvas").asInstanceOf[html.Canvas]
    private[this] val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    private[this] val statusText = dom.document.getElementById("statusText").asInstanceOf[html.Paragraph]

    private[this] def resize(): Unit = {
      canvas.width = canvas.parentElement.clientWidth
      canvas.height = canvas.parentElement.clientHeight
    }

    dom.window.onresize = (e: UIEvent) => {
      resize()
    }

    resize()

    val grid = new Grid(canvas, context)

    private[this] def render() = {
      world match {
        case None =>
          grid.draw(Seq.empty)
          statusText.textContent = ""
        case Some(w) =>
          grid.draw(w.population)
          statusText.textContent = s"Age: ${w.age}"
      }
    }

    dom.setInterval(render _, 100)
  }

  def main(): Unit = {
    var world: Option[World] = None
    var evolutionIntervalHandle: Option[Int] = None

    def worldLocal = world

    val renderer = new Renderer {
      def world = worldLocal
    }

    import renderer._

    ControlUnits.run.onchange = (e: Event) => {
      evolutionIntervalHandle = Some(dom.setInterval(evolution _, 400))
    }

    ControlUnits.stop.onchange = (e: Event) => {
      evolutionIntervalHandle.foreach(dom.window.clearInterval)
      evolutionIntervalHandle = None
    }

    ControlUnits.reset.onclick = (e: UIEvent) => {
      world = None
    }

    def evolution() = {
      world = world match {
        case None => Some(new World(() => (grid.columns, grid.lines), seed = Some(Seq(LivingCell(0, 1), LivingCell(1, 1), LivingCell(2, 1)))))
        case _ => world
      }

      world.foreach(_.evolve())
    }
  }
}
