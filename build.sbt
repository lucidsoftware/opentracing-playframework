import com.lucidchart.sbtcross.{CrossableProject, LibraryVersionAxis}

val playAxis = new LibraryVersionAxis("play", SettingKey[String]("playVersion"), LibraryVersionAxis.minorVersion)

def play = Project("play", file("play")).cross.cross(playAxis)

def `play-active` = Project("play-active", file("play-active"))
  .cross.cross(playAxis).dependsOn(CrossableProject.toDependency(play))

lazy val `play_2.10_2.3` =  play("2.3.9")("2.10.6")
lazy val `play_2.11_2.3` = play("2.3.9")("2.11.8")
lazy val `play_2.11_2.5` = play("2.5.10")("2.11.8")
lazy val `play-active_2.10_2.3` = `play-active`("2.3.9")("2.10.6")
lazy val `play-active_2.11_2.3` = `play-active`("2.3.9")("2.11.8")
lazy val `play-active_2.11_2.5` = `play-active`("2.5.10")("2.11.8")

inScope(Global)(Seq(
  credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    sys.env.getOrElse("SONATYPE_USERNAME", ""),
    sys.env.getOrElse("SONATYPE_PASSWORD", ""
  )),
  developers += Developer("pauldraper", "Paul Draper", "paulddraper@gmail.com", url("https://github.com/pauldraper")),
  licenses += "Apache 2.0 License" -> url("https://www.apache.org/licenses/LICENSE-2.0"),
  organization := "com.lucidchart",
  organizationHomepage := Some(url("http://opentracing.io/")),
  organizationName := "OpenTracing",
  PgpKeys.pgpPassphrase := Some(Array.emptyCharArray),
  resolvers += Resolver.typesafeRepo("releases"),
  scalaVersion := "2.11.8",
  scmInfo := Some(ScmInfo(
    url("https://github.com/lucidsoftware/opentracing-playframework"),
    "scm:git:git@github.com:lucidsoftware/opentracing-playframework.git"
  )),
  startYear := Some(2017),
  version := sys.props.getOrElse("build.version", "0-SNAPSHOT")
))
