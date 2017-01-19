import sbt.Keys._
import sbt._
import sbt.plugins.{CorePlugin, JvmPlugin}

object PlayCross extends AutoPlugin {
  object autoImport {
    val playVersion = settingKey[String]("Play version")
  }
  import autoImport._

  override val requires = CorePlugin && JvmPlugin

  override val projectSettings = Seq(
    unmanagedSourceDirectories in Compile += file(s"${sourceDirectory.value}-${playVersion.value}") / "main" / "scala",
    target := file(s"${target.value}-${playVersion.value}"),
    version := s"${version.value}-${playVersion.value}"
  )
}
