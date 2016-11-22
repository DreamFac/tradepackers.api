import com.typesafe.sbt.packager.archetypes.ServerLoader

name := """tradepacks"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, DebianDeployPlugin)

serverLoading in Debian := ServerLoader.SystemV


packageName := "tradepacks"

maintainer in Debian := "Eduardo Aviles <eduardo.avilesj@gmail.com>"

packageSummary in Debian := "Trade packers from asuramedia"

packageDescription := "Tradepackers app"

scalaVersion := "2.11.7"

javaOptions in Universal ++= Seq(
  // JVM memory tuning
  "-J-Xmx1024m",
  "-J-Xms512m",

  // Since play uses separate pidfile we have to provide it with a proper path
  // name of the pid file must be play.pid
  //s"-Dpidfile.path=/var/run/${packageName.value}/play.pid",

  // alternative, you can remove the PID file
  s"-Dpidfile.path=/dev/null",

  // Use separate configuration file for production environment
  //s"-Dconfig.file=/usr/share/${packageName.value}/conf/application.conf",

  s"-Dplay.crypto.secret=b=IjefW?yz`K^^=_ly97<qKgRl7fGl9TU9oJtivvlc:J6V2z2lGBCMvo0>:^09d^"

  // Use separate logger configuration file for production environment
  //s"-Dlogger.file=/usr/share/${packageName.value}/conf/logback.xml"

)

libraryDependencies ++= Seq(
  // If you enable PlayEbean plugin you must remove these
  // JPA dependencies to avoid conflicts.
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.projectlombok" % "lombok" % "1.16.10",
  "com.github.AsuraMedia" % "java-oauth-wrapper" % "0.0.1-beta1"
)

resolvers += (
  "jitpack" at "https://jitpack.io"
  )

PlayKeys.externalizeResources := false