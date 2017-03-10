description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.lucidchart" % "opentracing-thread-context" % "0.4",
  "com.google.guava" % "guava" % "21.0",
  "com.typesafe.play" %% "play" % SettingKey[String]("playVersion").value,
  "io.opentracing" % "opentracing-api" % "0.20.7",
  "io.opentracing.contrib" % "opentracing-globaltracer" % "0.0.1"
)

moduleName := "opentracing-play"
