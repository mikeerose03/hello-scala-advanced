package lectures.part2AFP

object PartialFunctions extends App {

  val aFunction = (x: Int) => x+1 // Function1[Int,Int] == Int => Int

  val aFussyFunction = (x: Int) =>
    if (x==1) 42
    else if (x==2) 56
    else if (x==5) 99
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // (1,2,5) => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // a partial function value

  println(aPartialFunction(2))

  // partial function utilities
  println(aPartialFunction.isDefinedAt(67)) // false

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2)) // Some(56)
  println(lifted(56)) // None

  val aChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(aChain(45)) // 67

  // partial functions extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
  * NOTE: PF can only have ONE parameter types
  * */


}
