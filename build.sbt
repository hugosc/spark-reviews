name := "SparkReviews"
version := "0.1"
scalaVersion := "2.11.1"

val sparkVersion = "2.1.1"
val playVersion = "2.5.15"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "com.typesafe.play" %% "play-json" % playVersion
)

dependencyOverrides ++= Set(
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.0")
