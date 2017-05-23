package counter

import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.{Application, ApplicationLoader, Environment}

trait CounterSpec extends PlaySpec with OneServerPerSuite {

  override lazy val app: Application =
    (new Loader).load(ApplicationLoader.createContext(Environment.simple()))

}
