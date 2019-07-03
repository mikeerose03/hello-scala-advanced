package exercises

object PimpMyLibrary extends App {

  /*
  implicit class RichString(value: String) {
    def asInt: Int = value.toInt
    def encrypt: String = {
      val alphabet = "abcdefghijklmnopqrstuvwxyz"
      value
        .map(s => alphabet.charAt(alphabet.indexOf(s)+2))
        .mkString("")
    }
  }
  println("john".encrypt)
  println("1".asInt)

  implicit class RichInt(value: Int) {
    def times[T](f: Int => T): T = f(value)
    def *[T](l: List[T]): List[T] = {
      def aux(n: Int, acc: List[T]): List[T] = {
        if (n >= value) acc
        else aux(n+1, acc ++ l)
      }
      aux(0, List())
    }
  }
  println(2.times(_+1))
  println(3 * List(1,2))
   */

  // answer

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string) // java.lang.Integer
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("john".encrypt(2))

  implicit class RichInt(value: Int) {
    def times(f: () => Unit): Unit = {
      def timesAux(n: Int): Unit = {
        if (n <= 0) ()
        else {
          f()
          timesAux(n-1)
        }
      }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concat(n: Int): List[T] =
        if (n <= 0) List()
        else concat(n-1) ++ list
      concat(value)
    }
  }

  2.times(() => println("Scala Rocks!"))
  println(4 * List(1,2))
}
