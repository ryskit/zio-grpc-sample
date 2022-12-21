package sample

import io.grpc.Status
import sample.helloworld.ZioHelloworld.RGreeter
import sample.helloworld.{HelloReply, HelloRequest}
import sample.userdatabase.UserDatabase
import scalapb.zio_grpc.ServerLayer
import zio.Console._
import zio._

package object userdatabase {
  trait UserDatabase {
    def fetchUser(name: String): IO[Status, User]
  }

  object UserDatabase {
    // accessor
    def fetchUser(name: String): ZIO[UserDatabase, Status, User] =
      ZIO.serviceWithZIO[UserDatabase](_.fetchUser(name))

    val live = ZLayer.succeed(new UserDatabase {
      def fetchUser(name: String): IO[Status, User] =
        ZIO.succeed(User(name))
    })
  }
}

object GreeterWithDatabase extends RGreeter[UserDatabase] {
  def sayHello(
    request: HelloRequest
  ): ZIO[UserDatabase, Status, HelloReply] =
    UserDatabase.fetchUser(request.name).map { user =>
      HelloReply(s"Hello ${user.name}")
    }
}

object GreeterWithDatabaseServer extends zio.ZIOAppDefault {
  val serverLayer = ServerLayer.fromServiceLayer(
    io.grpc.ServerBuilder.forPort(9090)
  )(GreeterWithDatabase.toLayer)

  val ourApp = UserDatabase.live >>> serverLayer

  def run =
    (ourApp.build *> ZIO.never).exitCode
}
