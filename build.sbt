name := """play-scala-forms-example"""

version := "2.6.x"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

lazy val root = (project in file(".")).enablePlugins(PlayScala).aggregate(compiler).dependsOn(compiler)

libraryDependencies += guice

val testingDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.+" % "test",
  "org.scalactic" %% "scalactic" % "3.0.+" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.+" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  //"org.unblock" %% "serverinterpreter" % "0.0.1-SNAPSHOT",
  "org.scorexfoundation" %% "scrypto" % "2.+",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.+",
  "com.typesafe.akka" %% "akka-actor" % "2.4.+",
  "org.bitbucket.inkytonik.kiama" %% "kiama" % "2.1.0",
  "com.lihaoyi" %% "fastparse" % "1.0.0",
  //"org.scorexfoundation" %% "scrypto" % "2.+",
  //"org.scorexplatform" %% "sigma-state" % "0.0.1"
)

lazy val compiler = project

PlayKeys.devSettings := Seq("play.server.http.port" -> "8080")
