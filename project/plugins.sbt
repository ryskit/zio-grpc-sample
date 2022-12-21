addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("com.thesamet"  % "sbt-protoc"   % "1.0.6")

val zioGrpcVersion = "0.6.0-test5"

libraryDependencies ++= Seq(
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % zioGrpcVersion,
  "com.thesamet.scalapb"          %% "compilerplugin"   % "0.11.12"
)
