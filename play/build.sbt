enablePlugins(PlayCross)

description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "21.0",
  "com.typesafe.play" %% "play" % (playVersion.value match {
    case "2.3" => "2.3.9"
    case "2.5" => "2.5.10"
  }),
  "io.opentracing" % "opentracing-api" % "0.20.7"
)

moduleName := "opentracing-play"
