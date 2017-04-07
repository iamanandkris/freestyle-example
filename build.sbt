lazy val root = (project in file("."))
  .settings(
    name := "freestyle",
    scalaVersion := "2.12.1",
    version := "1.0",
    //scalacOptions ++= Seq("-Ymacro-debug-lite"),
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"))
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.47deg" %% "freestyle" % "0.1.0-SNAPSHOT",
      compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch)
    ))
        