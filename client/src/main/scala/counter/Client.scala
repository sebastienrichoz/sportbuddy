package counter

import java.nio.ByteBuffer

import scala.scalajs.js.typedarray._
import org.scalajs.dom

import boopickle.Default._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Client extends autowire.Client[ByteBuffer, Pickler, Pickler] {

  override def doCall(req: Request): Future[ByteBuffer] = {
    dom.ext.Ajax.post(
      url = "/service/" + req.path.mkString("/"),
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream")
    ).map { r =>
      TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer])
    }
  }

  override def read[Result: Pickler](p: ByteBuffer) = Unpickle[Result].fromBytes(p)
  override def write[Result: Pickler](r: Result) = Pickle.intoBytes(r)

}
