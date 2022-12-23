package sample

import io.grpc.{Metadata, ServerBuilder, Status}
import sample.greeter.ZioGreeter.ZGreeter
import sample.greeter._
import scalapb.zio_grpc.{RequestContext, Server, ServerLayer}
import zio.Console._
import zio._

object GreeterServiceWithMetadata {
  case class User(name: String)

  private val userKey = Metadata.Key.of("user-key", io.grpc.Metadata.ASCII_STRING_MARSHALLER)

  class GreeterService() extends ZGreeter[Any, User] {
    def greet(req: Request): ZIO[User, Status, Response] =
      for {
        user <- ZIO.service[User].map(_.name)
      } yield Response(s"Hello user-key: $userKey, req: ${req.name}")
  }

  def findUser(rc: RequestContext): IO[Status, User] =
    rc.metadata.get(userKey).flatMap {
      case Some(name) => ZIO.succeed(User(name))
      case _          => ZIO.fail(Status.UNAUTHENTICATED.withDescription("No access!"))
    }

  val live: ZLayer[Any, Nothing, ZGreeter[Any, RequestContext]] =
    ZLayer.fromFunction { a: Any =>
      new GreeterService()
        .transformContextZIO(findUser)
    }
}

object Server extends ZIOAppDefault {

  def serverWait: ZIO[Any, Throwable, Unit] =
    for {
      _ <- printLine("Server is running. Press Ctrl-C to stop.")
      _ <- (print(".") *> ZIO.sleep(1.second)).forever
    } yield ()

  def server(port: Int): Layer[Throwable, Server] =
    ServerLayer.fromServiceLayer(ServerBuilder.forPort(port))(GreeterServiceWithMetadata.live)

  private val myAppLogic =
    serverWait.provideLayer(server(9090))

  override def run = myAppLogic.exitCode

}
