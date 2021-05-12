package testLab

import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ShoppingSuite extends FunSpec with Matchers with MockFactory {
  private val product = Product("Apples")
  private val quantity = 3
  private val price = Price(6.00)
  private val item = Item(product, ItemDetails(quantity, price))

  describe("A shopping") {
    it("should fail with an AssertionError when trying to pick zero or less items") {
      val mockWarehouse = mock[Warehouse]
      (mockWarehouse.get _).expects(*, *).never()
      val mockCatalog = mock[Catalog]
      val mockCart = mock[Cart]
      val mockLogger =  mock[Logger]
      an [AssertionError] should be thrownBy new Shopping(mockWarehouse, mockCatalog, mockCart, mockLogger).pick(product, 0)
    }

    it("should correctly return a cart with the added products") {
      val mockWarehouse = mock[Warehouse]
      (mockWarehouse.get _).expects(product, quantity).returning(product, quantity).once()
      val mockCatalog = mock[Catalog]
      val mockCart = mock[Cart]
      val mockLogger = mock[Logger]
      setUpMocksForProduct(mockWarehouse, mockCatalog, mockCart, mockLogger)
      new Shopping(mockWarehouse, mockCatalog, mockCart, mockLogger).pick(product, quantity)
    }

    it("should correctly return a cart with all available products when asked for more") {
      val mockWarehouse = mock[Warehouse]
      (mockWarehouse.get _).expects(product, quantity + 1).returning(product, quantity).once()
      val mockCatalog = mock[Catalog]
      val mockCart = mock[Cart]
      val mockLogger =  mock[Logger]
      setUpMocksForProduct(mockWarehouse, mockCatalog, mockCart, mockLogger)
      new Shopping(mockWarehouse, mockCatalog, mockCart, mockLogger).pick(product, quantity + 1)
    }

    it("should return an empty cart when asked for products which are not in the warehouse") {
      val nonExistingProduct = Product("Bananas")
      val mockWarehouse = mock[Warehouse]
      (mockWarehouse.get _).expects(nonExistingProduct, quantity).returning(product, 0).once()
      val mockCatalog = mock[Catalog]
      val mockCart = mock[Cart]
      val mockLogger =  mock[Logger]
      setUpMocksForNothing(mockWarehouse, mockCatalog, mockCart, mockLogger)
      new Shopping(mockWarehouse, mockCatalog, mockCart, mockLogger).pick(nonExistingProduct, quantity)
    }
  }

  private def setUpMocksForProduct(mockWarehouse: Warehouse, mockCatalog: Catalog, mockCart: Cart, mockLogger: Logger): Unit = {
    (mockWarehouse.supply _).expects(*, *).never()
    (mockCatalog.products _).expects().never()
    (mockCatalog.priceFor(_: Product, _: Int)).expects(product, quantity).returning(price).once()
    (mockCatalog.priceFor(_: Product)).expects(*).never()
    (mockCart.add _).expects(item).once()
    (mockCart.size _).expects().anyNumberOfTimes()
    (mockCart.totalCost _).expects().anyNumberOfTimes()
    (mockCart.content _).expects().never()
    (mockLogger.log _).expects(*).anyNumberOfTimes()
  }

  private def setUpMocksForNothing(mockWarehouse: Warehouse, mockCatalog: Catalog, mockCart: Cart, mockLogger: Logger): Unit = {
    (mockWarehouse.supply _).expects(*, *).never()
    (mockCatalog.products _).expects().never()
    (mockCatalog.priceFor(_: Product, _: Int)).expects(*, *).never()
    (mockCatalog.priceFor(_: Product)).expects(*).never()
    (mockCart.add _).expects(*).never()
    (mockCart.size _).expects().never()
    (mockCart.totalCost _).expects().never()
    (mockCart.content _).expects().never()
    (mockLogger.log _).expects(*).anyNumberOfTimes()
  }
}
