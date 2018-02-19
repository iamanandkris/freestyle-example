lazy val root = (project in file("."))
  .settings(
    name := "freestyle-test",
    scalaVersion := "2.12.4",
    version := "1.0"
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.frees" %% "frees-core" % "0.7.0",
      "io.frees" %% "frees-effects" % "0.7.0",
      "io.frees" %% "frees-logging" % "0.7.0",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      compilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M10" cross CrossVersion.full),
      compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
    ))
