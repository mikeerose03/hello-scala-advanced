package exercises

object CurriesPAF extends App {
  val simpleAddFunction = (x: Int, y: Int) => x+y
  def simpleAddMethod(x: Int, y: Int) = x+y
  def curriedAddMethod(x: Int)(y: Int) = x+y

  // add7: Int => Int = y => 7+y
  println(simpleAddFunction(7, 5))
  println(simpleAddMethod(7, 5))
  println(curriedAddMethod(7)(5))

  //val add7 = curriedAddMethod(7) _
  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7)

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // alternative syntax for PAF

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
  // y => simpleAddMethid(7, y)

  val add7_6 = simpleAddFunction(6, _: Int)

  println(add7(5))

  // underscores are powerful
  def concat(a: String, b: String, c: String) = a + b + c
  val insertName = concat("Hello, I'm ", _: String, ", how are you?")
  // x: String => concat("hello, I'm ", x, ", how are you?")
  println(insertName("Mikee"))

  val fillInTheBlanks = concat("Hello, ", _: String, _: String) // (x, y) => concat("Hello, ", x, y)
  println(fillInTheBlanks("Daniel", " Scala is awesome"))

  /*
  def formatTo(f: String, n: Float): String = f.format(n)

//  println(formatTo("%4.2f", 8))
  val formatTo_42f = formatTo("%4.2f", _: Float)
  val formatTo_86f = formatTo("%8.6f", _: Float)
  val formatTo_1412f = formatTo("%14.12f", _: Float)

  List(1,2,3,4) map (x => formatTo_42f(x)) foreach print
  List(1,2,3,4) map (x => formatTo_86f(x)) foreach print
  List(1,2,3,4) map (x => formatTo_1412f(x)) foreach print
*/
  def curriedFormatter(f: String)(n: Double): String = f.format(n)

  val formatTo_42f = curriedFormatter("%4.2f") _
  val formatTo_86f = curriedFormatter("%8.6f") _
  val formatTo_1412f = curriedFormatter("%14.12f") _

  val numbers = List(Math.PI,Math.E,1,9.8, 1.83e-12)
  println(numbers.map(formatTo_42f))

  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parentMethod(): Int = 42

  println(byName(method))
  println(byName(parentMethod)) // ==> byName(parentMethod())
  println(byName(parentMethod()))
//  println(byName(() => 42)) // not ok
  println(byName((() => 42)())) // ok
  //println(byName(parentMethod _) // not ok

  //  println(byFunction(method)) // not ok. the compiler does not do ETA-expansion
  println(byFunction(parentMethod)) // compiler does ETA
  println(byFunction(() => 46)) // ok
  println(byFunction(parentMethod _)) // works but with warning - unnecessary
}
