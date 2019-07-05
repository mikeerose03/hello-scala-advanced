package lectures.part5TypeSystem

object StructuralTypes extends App {

  // structural types

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("not making a sound")
  }

//  def closeQuietly(closeable: JavaCloseable OR HipsterCloseable) // NOT ALLOWED

  type UnifiedCloseable = {
    def close(): Unit
  } // STRUCTURAL TYPE

  def closeQuietly(closeable: UnifiedCloseable) = closeable.close() // NOT ALLOWED
  closeQuietly(new HipsterCloseable)


  // TYPE REFINEMENTS

  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advCloseable: AdvancedCloseable): Unit = advCloseable.closeSilently()

//  closeShh(new AdvancedCloseable)
//  closeShh(new HipsterCloseable) // DOES NOT COMPILE

  // using structural types as standalone types
  def altClose(closeable: { def close() }): Unit = closeable.close()

  // type-checking => duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark")
  }

  class Car {
    def makeSound(): Unit = println("vrooom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // CAVEAT: based on reflection

}
