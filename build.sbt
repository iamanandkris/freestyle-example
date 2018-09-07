val elastic4s = Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-http" % "6.1.3",
  "com.sksamuel.elastic4s" %% "elastic4s-circe" % "6.1.3"
)

val freeStyleV = "0.8.2"
val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.9.0")

lazy val root = (project in file("."))
  .settings(
    name := "freestyle-test",
    scalaVersion := "2.12.4",
    version := "1.0"
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.frees" %% "frees-core" % freeStyleV,
      "io.frees" %% "frees-effects" % freeStyleV,
      "io.frees" %% "frees-logging" % freeStyleV,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      compilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M10" cross CrossVersion.full),
      compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
    ) ++ elastic4s)
