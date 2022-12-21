import sbt._

object Dependencies {

  object V {
    val grpc          = "1.51.0"
    val scalapbJson4s = "0.12.0"
  }

  object Libraries {
    val grpcNetty     = "io.grpc"               % "grpc-netty"     % V.grpc
    val scalapbJson4s = "com.thesamet.scalapb" %% "scalapb-json4s" % V.scalapbJson4s
  }

}
