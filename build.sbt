lazy val root = (project in file("."))
  .settings(
    name := "freestyle-test",
    scalaVersion := "2.12.1",
    version := "1.0",
    scalaOrganization := "org.typelevel",
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.sonatypeRepo("releases")/*,
      Resolver.sonatypeRepo("snapshots")*/)
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.47deg" %% "freestyle" % "0.1.0-SNAPSHOT",
      "com.47deg" %% "freestyle-effects" % "0.1.0-SNAPSHOT",
      "io.monix" %% "monix" % "2.2.4",
      compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch)
    ))
