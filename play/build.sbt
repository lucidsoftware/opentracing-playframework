description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.lucidchart" % "opentracing-thread-context" % "0.5",
  "com.google.guava" % "guava" % "21.0",
  "com.typesafe.play" %% "play" % SettingKey[String]("playVersion").value,
  "io.opentracing" % "opentracing-api" % "0.31.0",
  "io.opentracing" % "opentracing-util" % "0.31.0"
)

moduleName := "opentracing-play"
