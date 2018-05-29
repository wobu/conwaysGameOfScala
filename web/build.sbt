enablePlugins(ScalaJSPlugin)

enablePlugins(SbtWeb)

scalaJSUseMainModuleInitializer in Compile := true

scalaJSUseMainModuleInitializer in Test := false

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  "org.webjars" % "jquery" % "3.3.1-1",
  "org.webjars" % "bootstrap" % "3.3.7-1")
