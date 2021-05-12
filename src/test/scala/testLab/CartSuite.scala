package testLab

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{fixture, Matchers, Outcome}

@RunWith(classOf[JUnitRunner])
class CartSuite extends fixture.FlatSpec with Matchers {
  override type FixtureParam = Cart
  private val initItems = Seq(Item(Product("Milk bottles"), ItemDetails(3, Price(1.00))),
                              Item(Product("Apples"), ItemDetails(1, Price(2.00))))

  override protected def withFixture(test: OneArgTest): Outcome = {
    val cart = new BasicCart(initItems.map(i => i.product -> i.details).toMap)
    test(cart)
  }

  "An empty cart" should "be empty when first created" in { _ =>
    val cart = new BasicCart()
    cart should have size 0
    cart.totalCost shouldEqual 0
    cart.content shouldEqual Set.empty
  }

  "A filled cart" should "contain only the added items" in {
    _.content should contain only (initItems: _*)
  }

  it should "have a size equal to the number of added items" in {
    _ should have size initItems.size
  }

  it should "have a total cost equal to the sum of the costs all added items" in {
    _.totalCost shouldEqual initItems.map(_.details.price.value).sum
  }

  it should "update its content, size and total cost when a new item is added" in { c =>
    val newItem: Item = Item(Product("Chocolate bars"), ItemDetails(5, Price(5.00)))
    c add newItem
    val allItems = initItems :+ newItem
    c.content should contain only(allItems: _*)
    c should have size allItems.size
    c.totalCost shouldEqual allItems.map(_.details.price.value).sum
  }

  it should "update its content and total cost but not its size when an already added product is added" in { c =>
    val repeatedItem: Item = Item(Product("Apples"), ItemDetails(2, Price(3.00)))
    c.add(repeatedItem)
    val allItems = initItems.map(i => i.product -> i.details)
                            .toMap
                            .get(repeatedItem.product)
                            .map(d => ItemDetails(d.qty + repeatedItem.details.qty,
                                                  Price(d.price.value + repeatedItem.details.price.value)))
                            .map(d => initItems.dropRight(1) :+ Item(repeatedItem.product, d))
                            .get
    c.content should contain only(allItems: _*)
    c should have size initItems.size
    c.totalCost shouldEqual initItems.map(_.details.price.value).sum + repeatedItem.details.price.value
  }
}