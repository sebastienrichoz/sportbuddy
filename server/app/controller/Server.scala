package controller

import java.nio.ByteBuffer

import boopickle.Default._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.Service

object Server extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer) = Unpickle[R].fromBytes(p)
  override def write[R: Pickler](r: R) = Pickle.intoBytes(r)

  val routes: Server.Router = route[ServiceDef](Service)
}