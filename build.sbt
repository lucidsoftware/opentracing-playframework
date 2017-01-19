import sbt.Keys._

lazy val `opentracing-globaltracer` = RootProject(
  new URI("pom", "io.opentracing.contrib:opentracing-globaltracer:0.1.0-SNAPSHOT:git://github.com/opentracing-contrib/java-globaltracer.git#223ac98d", "")
)

lazy val `opentracing-spanmanager` = RootProject(
  new URI("pom", "io.opentracing.contrib:opentracing-spanmanager:0.0.1:git://github.com/opentracing-contrib/java-activespan.git#23885b8a", "")
)

def play = Project("play", file("play"))

lazy val `play_2.3` = play.copy(s"${play.id}_2_3").settings(
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  playVersion := "2.3"
)

lazy val `play_2.5` = play.copy(s"${play.id}_2_5").settings(
  playVersion := "2.5"
)

def `play-active` = Project("play-active", file("play-active"))
  .dependsOn(`opentracing-globaltracer`, `opentracing-spanmanager`)

lazy val `play-active_2.3` = `play-active`.copy(s"${`play-active`.id}_2_3").dependsOn(`play_2.3`).settings(
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  playVersion := "2.3"
)

lazy val `play-active_2.5` = `play-active`.copy(s"${`play-active`.id}_2_5").dependsOn(`play_2.5`).settings(
  playVersion := "2.5"
)

inScope(Global)(Seq(
  credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", sys.env.getOrElse("SONATYPE_USERNAME", ""), sys.env.getOrElse("SONATYPE_PASSWORD", "")),
  developers += Developer("pauldraper", "Paul Draper", "paulddraper@gmail.com", url("https://github.com/pauldraper")),
  licenses += "Apache 2.0 License" -> url("https://www.apache.org/licenses/LICENSE-2.0"),
  organization := "com.lucidchart",
  organizationHomepage := Some(url("http://opentracing.io/")),
  organizationName := "OpenTracing",
  pgpPassphrase := Some(Array.emptyCharArray),
  pgpPublicRing := file(System.getProperty("user.home")) / ".pgp" / "pubring",
  pgpSecretRing := file(System.getProperty("user.home")) / ".pgp" / "secring",
  resolvers += Resolver.typesafeRepo("releases"),
  scalaVersion := "2.11.8",
  scmInfo := Some(ScmInfo(
    url("https://github.com/lucidsoftware/opentracing-playframework"),
    "scm:git:git@github.com:lucidsoftware/opentracing-playframework.git"
  )),
  startYear := Some(2017),
  version := "0.0-SNAPSHOT"
))
