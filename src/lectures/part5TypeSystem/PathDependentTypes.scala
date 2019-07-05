package lectures.part5TypeSystem

object PathDependentTypes extends App {
  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner

  val oo = new Outer
  // val otherInner: oo.Inner = new o.Inner // DOES NOT WORK

  o.print(inner)
  // oo.print(inner) // DOES NOT WORK

  // path-dependent type

  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)


}
