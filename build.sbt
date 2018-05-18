name := "JSONValididatorAPI"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % "3.7",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.github.java-json-tools" % "json-schema-validator" % "2.2.10",
  "org.scalatra" %% "scalatra" % "2.6.+",
  "org.scalatra" %% "scalatra-scalatest" % "2.6.3" % "test",
  "javax.servlet" % "javax.servlet-api" % "3.1.0",
  "org.scalatra" %% "scalatra-json" % "2.6.+",
  "org.json4s"   %% "json4s-native" % "3.5.2"
)
