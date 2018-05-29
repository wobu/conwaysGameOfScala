package gameoflife.web

import gameoflife.RandomLifeGenerator
import gameoflife.Universe._
import org.scalajs.dom
import org.scalajs.dom.{Event, UIEvent, html}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle
import scala.util.control.NonFatal

object ConwaysGameOfLifeApp {

  case class Settings(canvasHeight: Int = 250)

  object Settings {
    val canvasHeight = dom.document.getElementById("canvasHeight").asInstanceOf[html.Input]
    val applySettings = dom.document.getElementById("applySettings").asInstanceOf[html.Button]
  }

  object ControlUnits {
    val run = dom.document.getElementById("runButton").asInstanceOf[html.Input]
    val stop = dom.document.getElementById("stopButton").asInstanceOf[html.Input]
    val reset = dom.document.getElementById("resetButton").asInstanceOf[html.Button]
  }

  trait Renderer {
    def world: Option[World]
    def settings: Settings

    private[this] val canvas = dom.document.getElementById("canvas").asInstanceOf[html.Canvas]
    private[this] val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    private[this] val statusText = dom.document.getElementById("statusText").asInstanceOf[html.Paragraph]

    Settings.canvasHeight.value = settings.canvasHeight.toString

    private[this] def resize(): Unit = {
      canvas.width = canvas.parentElement.clientWidth
      canvas.height = settings.canvasHeight
    }

    dom.window.onresize = (e: UIEvent) => {
      resize()
    }

    resize()

    val grid = new Grid(canvas, context)

    private[this] def render() = {
      resize()

      world match {
        case None =>
          grid.draw(Seq.empty)
          statusText.textContent = ""
        case Some(w) =>
          grid.draw(w.population)
          statusText.textContent = s"Age: ${w.age}"
      }
    }

    js.timers.setInterval(100) {
      render()
    }
  }

  def main(args: Array[String]): Unit = {
    var world: Option[World] = None
    var evolutionIntervalHandle: Option[SetIntervalHandle] = None
    var settings = Settings()

    def worldLocal = world
    def settingsLocal = settings

    val renderer = new Renderer {
      def world = worldLocal
      def settings = settingsLocal
    }

    import renderer._

    ControlUnits.run.onchange = (e: Event) => {
      evolutionIntervalHandle = Some(js.timers.setInterval(400) { evolution() })
    }

    ControlUnits.stop.onchange = (e: Event) => {
      evolutionIntervalHandle.foreach(js.timers.clearInterval)
      evolutionIntervalHandle = None
    }

    ControlUnits.reset.onclick = (e: UIEvent) => {
      world = None
    }

    Settings.applySettings.onclick = (e: UIEvent) => {
      try {
        val canvasHeight = Settings.canvasHeight.value.toInt
        settings = settings.copy(canvasHeight = canvasHeight)
      } catch {
        case NonFatal(_) =>
      }
    }

    def evolution() = {
      world = world match {
        case None => Some(new World(() => grid.dimensions, seed = Some(RandomLifeGenerator(grid.dimensions))))
        case _ => world
      }

      world.foreach(_.evolve())
    }
  }
}
