package testLecture.code.e0basics

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import testLecture.code.ProgramToCover

// sbt
// > coverage
// > testOnly *ConditionCoverageExample
// > coverageReport

@RunWith(classOf[JUnitRunner])
class ConditionCoverageExample extends FunSuite {
  test("Cover all the conditions") {
    ProgramToCover.methodToCover(c2 = true, c4 = true, c5 = true)
    ProgramToCover.methodToCover(c1 = true, c3 = true)
  }
}

@RunWith(classOf[JUnitRunner])
class DecisionCoverageExample extends FunSuite {
  test("Cover all the decision branches") {
    ProgramToCover.methodToCover(c1 = true, c2 = true, c5 = true)
    ProgramToCover.methodToCover(c3 = true)
  }
}