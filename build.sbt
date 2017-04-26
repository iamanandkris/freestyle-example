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
      "com.47deg" %% "freestyle-async" % "0.1.0-SNAPSHOT",
      "io.monix" %% "monix" % "2.2.4",
      "io.monix" %% "monix-cats" % "2.2.4",
      "com.typesafe.akka" %% "akka-actor" % "2.4.17",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.0",
      compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch),
      compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
    ))
