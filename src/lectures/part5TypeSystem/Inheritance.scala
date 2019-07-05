package lectures.part5TypeSystem

object Inheritance extends App {

  // convenience
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem

  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "LION" }
  trait Tiger extends Animal { override def name: String = "TIGER" }
  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name) // TIGER

  /*
    Mutant
    extends Animal with { override def name: String = "lion" }
    with Animal with { override def name: String = "tiger" }

    LAST OVERRIDE GETS PICKED
   */

  // the super inheritance problem: TYPE LINEARIZATION

  trait Cold {
    def print = println("Cold")
  }

  trait Green extends Cold {
    override def print = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print

  /*
    WHAT'S HAPPENING HERE?

    Cold = AnyRef with <Cold>
    Green =
          = Cold with <Green>
          = AnyRef with <Cold> with <Green>
    Blue
          = Cold with <Blue>
          = AnyRef with <Cold> with <Blue>
    Red   = AnyRef with <Red>
    White = Red with Green with Blue with <White>
      = AnyRef with <Red>
        with (AnyRef with <Cold> with <Green>)
        with (AnyRef with <Cold> with <Blue>)
        with <White>
      = AnyRef with <Red> with <Cold> with <Green> with <Blue> with <White> => This is called "TYPE LINEARIZATION"
   */


}
