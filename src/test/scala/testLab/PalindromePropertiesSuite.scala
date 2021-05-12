package testLab

import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks

@RunWith(classOf[JUnitRunner])
class PalindromePropertiesSuite extends PropSpec with PropertyChecks with  Matchers {
  private val palindromeGen = for (s <- Gen.alphaStr; c <- Gen.option(Gen.alphaChar)) yield s ++ c ++ s.reverse

  property("A palindrome is a word which reversed is equal to itself") {
    forAll(palindromeGen) { p => p shouldEqual p.reverse }
  }
}
