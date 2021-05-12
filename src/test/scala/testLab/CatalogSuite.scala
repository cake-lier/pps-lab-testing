package testLab

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{fixture, Matchers, Outcome}

@RunWith(classOf[JUnitRunner])
class CatalogSuite extends fixture.FunSpec with Matchers {
  override type FixtureParam = Catalog
  private val products = Map(Product("Apples") -> Price(1.00), Product("Milk bottles") -> Price(2.00))

  override protected def withFixture(test: OneArgTest): Outcome = {
    test(new BasicCatalog(products))
  }

  describe("A catalog") {
    describe("when empty") {
      it("should contain no items") { _ =>
        val catalog = new BasicCatalog(Map())
        catalog.products shouldEqual Map.empty
      }
    }

    describe("when filled") {
      it("should contain only the added items") {
        _.products should contain only (products.toSeq: _*)
      }

      it("should return the correct price for one item") {
        _ priceFor products.toSeq.head._1 shouldEqual products.toSeq.head._2
      }

      it("should return the correct price for multiple items") { c =>
        val quantity = 3
        c.priceFor(products.toSeq.head._1, quantity).value shouldEqual products.toSeq.head._2.value * quantity
      }

      it("should throw NoSuchElementException when searching for a non-existing product with a quantity of one") {
        an [NoSuchElementException] should be thrownBy _.priceFor(Product("Bananas"))
      }

      it("should throw NoSuchElementException when searching for a non-existing product with a quantity greater than one") {
        an [NoSuchElementException] should be thrownBy _.priceFor(Product("Bananas"), 5)
      }
    }
  }
}
