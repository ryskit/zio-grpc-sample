import Dependencies._
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val `contract-grpc-proto-interface` = (project in file("contracts/contract-grpc-proto-interface"))
  .settings(
    name := "contract-grpc-proto-interface",
    libraryDependencies ++= Seq(
      "com.google.api.grpc"   % "proto-google-common-protos" % "2.11.0" % "protobuf-src",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc"       % scalapb.compiler.Version.scalapbVersion,
      Libraries.scalapbJson4s
    ),
    Compile / PB.targets := Seq(
      scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value,
      scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value
    )
  )

val `api-client` = (project in file("api-client"))
  .settings(
    name := "api-client",
    libraryDependencies ++= Seq(
      Libraries.grpcNetty
    )
  )
  .dependsOn(`contract-grpc-proto-interface`)

lazy val root = (project in file("."))
  .settings(
    name := "zio-grpc-sample",
    libraryDependencies ++= Seq(
      Libraries.grpcNetty
    ),
    fork := true
  )
  .dependsOn(`contract-grpc-proto-interface`)
  .aggregate(
    `contract-grpc-proto-interface`,
    `api-client`
  )
