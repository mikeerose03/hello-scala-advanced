package exercises

object StructuralTypes extends App {


  // #1
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZ"
  }

  def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)

  // answer
  case object CBNil extends CBL[Nothing] {
    def head: Nothing = ???
    def tail: CBL[Nothing] = ???
  }

  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // T == Brain

  // #2
  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  // answer
  val brainzList = CBCons(new Brain, CBNil)
  val stringsList = CBCons("Brains", CBNil)

  HeadEqualizer.===(brainzList, new Human)
  // problem:
  HeadEqualizer.===(new Human, stringsList) // NOT TYPESAFE

}
