package testLab

import cucumber.api.junit.Cucumber
import cucumber.api.CucumberOptions
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(features = Array("classpath:features/Shopping.feature"),
                 glue = Array("classpath:testLab"),
                 plugin = Array("pretty", "html:target/cucumber/html"))
class RunShoppingFeaturesSuite

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers

class ShoppingSteps extends ScalaDsl with EN with Matchers {
  private val warehouse = new BasicWarehouse()
  private val cart = new BasicCart()
  private val logger = new BasicLogger()
  private var catalog: Catalog = _
  private var shopping: Shopping = _
  private var wantedQuantity = 0
  private var product: Product = _
  private var priceValue: Double = 0.0
  private var cartAfterBuy: Cart = _

  Given("""^I want (\d+) (\w+)$""") {
    (q: Int, p: String) => {
      wantedQuantity = q
      product = Product(p)
    }
  }
  And("""^(\d+) (\w+) are available$""") { (q: Int, n: String) => warehouse.supply(Product(n), q) }
  And("""^the price for (\w+) is (\d+\.\d{2}) each$""") {
    (cn: String, v: Double) => {
      priceValue = v
      catalog = new BasicCatalog(Map(Product(cn) -> Price(v)))
    }
  }
  When("""^I buy them$""") {
    shopping = new Shopping(warehouse, catalog, cart, logger)
    cartAfterBuy = shopping.pick(product, wantedQuantity)
  }
  Then("""^I should have my cart filled with (\d+) (\w+)$""") {
    (q: Int, n: String) => cartAfterBuy.content shouldEqual Set(Item(Product(n), ItemDetails(q, Price(priceValue * q))))
  }
  And("""^the size of the cart should be (\d+)$""") { (i: Int) => cartAfterBuy.size shouldEqual i }
  And("""^the total cost should be (\d+\.\d{2})$""") { (c: Double) => cartAfterBuy.totalCost shouldEqual c }

  Given("""I put (\d+) (\w+) for (\d+\.\d{2}) total in my cart$""") {
    (q: Int, n: String, p: Double) => {
      cartAfterBuy = new BasicCart()
      priceValue = p / q
      cartAfterBuy.add(Item(Product(n), ItemDetails(q, Price(p))))
    }
  }
  When("""^I remove (\d+) (\w+)$""") { (q: Int, n: String) => cartAfterBuy.remove(Product(n), q) }
}
