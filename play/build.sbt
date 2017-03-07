description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.lucidchart" % "opentracing-thread-context" % "0.3",
  "com.google.guava" % "guava" % "21.0",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.4.0",
  "com.typesafe.play" %% "play" % SettingKey[String]("playVersion").value,
  "io.opentracing" % "opentracing-api" % "0.20.7"
)

moduleName := "opentracing-play"
