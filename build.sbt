name := "Conway's game of Scala"

scalaVersion in Global := "2.11.5"

lazy val root = project in file(".") aggregate(core, web)

lazy val core = project in file("core")

lazy val web = project in file("web") dependsOn (core)

