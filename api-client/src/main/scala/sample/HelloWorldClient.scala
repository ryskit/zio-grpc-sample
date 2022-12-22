package sample

import sample.helloworld.ZioHelloworld.GreeterClient
import sample.helloworld.HelloRequest
import io.grpc.ManagedChannelBuilder
import zio.Console._
import scalapb.zio_grpc.ZManagedChannel
import zio._

object HelloWorldClient extends zio.ZIOAppDefault {
  val clientLayer: Layer[Throwable, GreeterClient] =
    GreeterClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext()
      )
    )

  def myAppLogic =
    for {
      r <- GreeterClient.sayHello(HelloRequest("World"))
      _ <- printLine(r.message)
    } yield ()

  final def run =
    myAppLogic.provideLayer(clientLayer).exitCode
}
