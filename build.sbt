name := "user"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.26",
  "org.mindrot" % "jbcrypt" % "0.3m"
)     

play.Project.playJavaSettings
