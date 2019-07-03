package lectures.part2AFP

object LazyEvaluation extends App {

  // lazy delays the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }

  println(x)
  println(x) // will no longer print hello

  // examples of implications
  // 1. side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // 2. in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))

  // using lazy vals

  def betterByNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // only evaluated once
    t+t+t+1
  }

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }
  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30 filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals are used under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println
  gt20Lazy foreach println

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a+1
  // same as List(1,2,3).withFilter(_ % 2 == 0).map(_+1)


}
