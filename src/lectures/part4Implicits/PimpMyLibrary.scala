package lectures.part4Implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal{
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  // normal class: new RichInt(42).isEven

  // implicit class: 42.isEven
  // this is called "type enrichment" or you could say "pimping"

  // 1 to 10 is an example of this

  // implicit conversions
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)

  println("6"/2)

  // equivalent to implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone to implicit conversions
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /* in C...
  * if (n) doSomething()
  * else doSomethingElse()
  * */

  val aConditionedValue = if (3) "OK" else "Something Wrong"
  println(aConditionedValue) // Something Wrong
}
