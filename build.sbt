lazy val root = (project in file("."))
  .settings(
    name := "freestyle",
    scalaVersion := "2.12.1",
    version := "1.0",
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"))
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.47deg" %% "freestyle" % "0.1.0-SNAPSHOT"
    ))
        