package lectures.part4Implicits

import java.util.Optional
import java.{util => ju}

object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
    Iterator
    Iterable
    ju.List - collection.mutable.Buffer
    ju.Set - collection.mutable.Set
    ju.Map - collection.mutable.Map
   */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1,2,3)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer) // true

  val numbers = List(1,2,3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala

  println(backToScala eq numbers) // false
  println(backToScala == numbers) // true


}
