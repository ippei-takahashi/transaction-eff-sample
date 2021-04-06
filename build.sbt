name := "TransactionEffSample"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.3.0",
  "org.typelevel" %% "cats-free" % "2.3.0",
  "org.atnos" %% "eff" % "5.15.0",
  "org.atnos" %% "eff-monix" % "5.15.0"
)

