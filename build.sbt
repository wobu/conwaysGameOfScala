name := "Conway's game of Scala"

scalaVersion := "2.11.5"

lazy val root = project in file(".") aggregate(core, uiJS)

lazy val core = project in file("core")

lazy val uiJS = project in file("ui-js") dependsOn(core) enablePlugins(ScalaJSPlugin)
