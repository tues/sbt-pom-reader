name := "sbt-pom-reader"

organization := "com.typesafe.sbt"

version := "2.1.0-RC1-SNAPSHOT"

licenses +=("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

sbtPlugin := true

val mvnVersion = "3.3.9"
val mvnWagonVersion = "2.10"
val aetherVersion = "1.0.2.v20150114"

libraryDependencies ++= Seq(
  "org.apache.maven" % "maven-embedder" % mvnVersion,
  "org.apache.maven" % "maven-aether-provider" % mvnVersion,
  "org.eclipse.aether" % "aether-transport-wagon" % aetherVersion,
  "org.apache.maven.wagon" % "wagon-http" % mvnWagonVersion,
  "org.apache.maven.wagon" % "wagon-http-lightweight" % mvnWagonVersion,
  "org.apache.maven.wagon" % "wagon-file" % mvnWagonVersion
)

initialCommands in console :=
  """| import com.typesafe.sbt.pom._
     | import sbt._
     | val localRepo = file(sys.props("user.home")) / ".m2" / "repository"
     | val pomFile = file("src/sbt-test/simple-pom/can-extract-basics/pom.xml")
     | val pom = loadEffectivePom(pomFile, localRepo, Seq.empty, Map.empty)
     | """.stripMargin

scriptedSettings

scriptedLaunchOpts <+= version apply { v => "-Dproject.version=" + v }

