package counter

trait ServiceDef {

  def get(): Int

  def increment(step: Int): Int

  def reset(): Int

}
