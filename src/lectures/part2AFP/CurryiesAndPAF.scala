package lectures.part2AFP

object CurryiesAndPAF extends App {
  // curried function
  val superAdder: Int => Int => Int =
    x => y => x+y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  def curriedAdder(x: Int)(y: Int): Int = x+y

  val add4: Int => Int = curriedAdder(4) // doesn't work if type is not declared
  println(add4(5))

  // lifting - transforming a method into functions (ETA-EXPANSION)

  // functions != methods because of JVM limitation
  def inc(x: Int): Int = x+1
  List(1,2,3).map(inc) // ETA-expansion in the background
  // same as List(1,2,3).map(x => inc(x))

  // Partial Function applications
  val add5 = curriedAdder(5) _ // tells compiler to convert the expression to Int => Int

  val simpleAddFunction = (x: Int, y: Int) => x+y
  def simpleAddMethod(x: Int, y: Int) = x+y
  def curredAddMethod(x: Int)(y: Int) = x+y

  // add7: Int => Int = y => 7+y
}
