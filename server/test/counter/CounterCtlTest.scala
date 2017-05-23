package counter

import play.api.test.FakeRequest
import play.api.test.Helpers._

class CounterCtlTest extends CounterSpec {

  "test index page" in {
    val response = route(app, FakeRequest("GET", "/greet")).value
    assert(status(response) === OK)
    assert(contentAsString(response) === "Hello, world!")
  }

}
