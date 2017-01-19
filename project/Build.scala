import sbt.BuildLoader.ResolveInfo
import sbt._

object Build extends sbt.Build {

  override def buildLoaders = Seq(BuildLoader.resolve { resolveInfo =>
    if (resolveInfo.uri.getScheme == "pom") {
      val Array(organization, name, version, wrapped) = resolveInfo.uri.getSchemeSpecificPart.split(":", 4)
      val wrappedInfo = new ResolveInfo(new URI(wrapped), resolveInfo.staging, resolveInfo.config, resolveInfo.state)
      RetrieveUnit(wrappedInfo).map { fn => () => {
        val file = fn()
        IO.write(file / "build.sbt",
          s"""externalPom()
            |
            |crossPaths := false
            |
            |name := "$name"
            |
            |organization := "$organization"
            |
            |version := "$version"
          """.stripMargin)
        file
      }}
    } else {
      None
    }
  })

}
