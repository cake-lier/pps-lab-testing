package testLab

import org.junit.runner.RunWith
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.{PropertyChecks, TableFor1}

@RunWith(classOf[JUnitRunner])
class ListPropertiesSuite extends PropSpec with PropertyChecks with Matchers {
  private val lists: TableFor1[Seq[Int]] = Table("list",
                                                 Array[Int](1, 2, 3),
                                                 Vector[Int](4, 5, 6),
                                                 List[Int](7, 8, 9))
  private val f: Int => Double = Math.pow(_, 2.0)
  private val g: Int => Int = _ + 1

  property("List concatenation should be associative") {
    forAll(lists) { xs => forAll(lists) { ys => forAll(lists) { zs => (xs ++ ys) ++ zs shouldEqual xs ++ (ys ++ zs) } } }
  }

  property("List concatenation with an empty List should yield the original List") {
    forAll(lists) { xs => Seq.empty ++ xs shouldEqual xs }
  }

  property("The identity function applied to a List should yield the original list") {
    forAll(lists) { xs => xs.map(identity) shouldEqual xs }
  }

  property("The function composition can be expressed as an inverted sequence of maps") {
    forAll(lists) { xs => xs.map(f compose g) shouldEqual xs.map(g).map(f) }
  }
}
