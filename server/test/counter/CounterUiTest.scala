package counter

import org.scalatestplus.play.{ChromeFactory, OneBrowserPerSuite}

class CounterUiTest
  extends CounterSpec
    with OneBrowserPerSuite
    with ChromeFactory {

  System.setProperty("webdriver.chrome.driver", "/home/julien/chromedriver")

  "increment" in {
    go to s"http://localhost:$port${routes.CounterCtl.index().url}"
    eventually(id("counter").element)
    val counterElem = id("counter").element
    assert(counterElem.text == "0")
    click on id("inc")
    eventually(assert(counterElem.text == "1"))
  }

}
