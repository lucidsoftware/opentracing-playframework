description := "Opentracing for Play"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "21.0",
  "com.typesafe.play" %% "play" % SettingKey[String]("playVersion").value,
  "io.opentracing" % "opentracing-api" % "0.20.7"
)

moduleName := "opentracing-play"
