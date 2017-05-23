package counter

import scala.concurrent.stm.Ref

object Service extends ServiceDef {

  private val value = Ref(0)

  def get(): Int = value.single.get

  def increment(step: Int): Int = value.single.transformAndGet(_ + step)

  def reset(): Int = value.single.transformAndGet(_ => 0)

}
