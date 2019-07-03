package exercises

object AdvancedPatternMatching extends App {

//  class Patterns(val patterns: List[Int => Boolean])
//  object Patterns {
//    def unapply(p: Patterns): Option[List[Int => Boolean]] = Some(p.patterns)
//  }
//
//  val patternsList: List[Int => Boolean] = List(_ % 2 == 0, _ < 10, _ > 10)

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n = 8
  val mathProperty = n match {
    case singleDigit => "single digit"
    case even => "an even number"
    case _ => "no property"
  }

  println(mathProperty)
}
