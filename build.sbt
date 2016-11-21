name := """tradepacks"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, DebianPlugin, JavaServerAppPackaging)

import com.typesafe.sbt.packager.archetypes.ServerLoader


serverLoading in Debian := ServerLoader.SystemV

maintainer in Linux := "Eduardo Aviles <eduardo.avilesj@gmail.com>"

packageSummary in Linux := "Trade packers from asuramedia"

packageDescription := "Tradepackers app"


scalaVersion := "2.11.7"

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