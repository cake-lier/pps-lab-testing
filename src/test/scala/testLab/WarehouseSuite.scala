package testLab

import org.junit.runner.RunWith
import org.scalatest.{fixture, Matchers, Outcome}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WarehouseSuite extends fixture.FunSuite with Matchers {
  override type FixtureParam = Warehouse
  private val product = Product("Apples") -> 3

  override protected def withFixture(test: OneArgTest): Outcome = {
    val warehouse = new BasicWarehouse()
    warehouse.supply(product._1, product._2)
    test(warehouse)
  }

  test("A warehouse should contain the added products") { w =>
    w get(product._1, product._2) shouldEqual product
    w get(product._1, 1) shouldEqual (product._1, 0)
  }

  test("A warehouse can return less items than the ones which contains") { w =>
    w get(product._1, product._2 - 1) shouldEqual (product._1, product._2 - 1)
    w get(product._1, 1) shouldEqual (product._1, 1)
  }

  test("A warehouse should not return more items than the ones which contains") { w =>
    w get(product._1, product._2 + 1) shouldEqual product
    w get(product._1, 1) shouldEqual (product._1, 0)
  }

  test("A warehouse should tally up already added products when those are added") { w =>
    w supply(product._1, product._2)
    w get(product._1, product._2 * 2) shouldEqual (product._1, product._2 * 2)
    w get(product._1, 1) shouldEqual (product._1, 0)
  }

  test("A warehouse should be correctly restocked for a product") { w =>
    w get(product._1, product._2) shouldEqual product
    w get(product._1, 1) shouldEqual (product._1, 0)
    w supply(product._1, product._2)
    w get(product._1, product._2) shouldEqual product
    w get(product._1, 1) shouldEqual (product._1, 0)
  }
}
