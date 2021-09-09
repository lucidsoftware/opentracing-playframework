description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.lucidchart" % "opentracing-thread-context" % "0.5",
  "com.google.guava" % "guava" % "21.0",
  "com.typesafe.play" %% "play" % SettingKey[String]("playVersion").value,
  "io.opentracing" % "opentracing-api" % "0.31.0",
  "io.opentracing" % "opentracing-util" % "0.31.0",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"
)

moduleName := "opentracing-play"
publishTo := sonatypePublishToBundle.value
// This is needed because the play axis changes the version
sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / "target" / "sonatype-staging" / (Global / version).value
scalafmtOnCompile := true
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
