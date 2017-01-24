addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("com.lucidchart" % "sbt-cross" % "master-SNAPSHOT")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

resolvers += Resolver.sonatypeRepo("snapshots")
