package testLecture.code.e3propertyBased

import org.junit.runner.RunWith
import org.scalacheck.{Gen, Prop}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.{Checkers, PropertyChecks, TableFor1}
import org.scalatest.{Matchers, PropSpec}

import scala.collection.immutable.{BitSet, HashSet, TreeSet}

@RunWith(classOf[JUnitRunner])
class SetSpec extends PropSpec with PropertyChecks with Checkers with Matchers {
  val examples: TableFor1[Set[Int]] = Table("set",BitSet.empty, HashSet.empty[Int], TreeSet.empty[Int])
  val examplesGen: Gen[Set[Int]] = Gen.oneOf(BitSet.empty, HashSet.empty[Int], TreeSet.empty[Int])

  property("an empty Set should have size 0") {
    // ScalaTest's property-based testing style
    forAll(examples) { set => set.size should be (0) }
    // ScalaCheck's property-based testing style
    check { Prop.forAll(examplesGen) { set => set.isEmpty } }
  }

  property("invoking head on an empty set should produce NoSuchElementException") {
    // ScalaTest's property-based testing style
    forAll(examples) { set => a [NoSuchElementException] should be thrownBy{set.head} }
    // ScalaCheck's property-based testing style
    check { Prop.forAll(examplesGen) { set =>
      Prop.throws(classOf[NoSuchElementException])(set.head) }
    }
  }

  property("A set must have size >= 0") {
    check { Prop.forAll { (l: Set[Int]) => l.size >= 0 } }
  }
}