package exercises

import java.{util => ju}

object ScalaConversions extends App {

  /*
  implicit class ScalaOptionConverter[T](value: ju.Optional[T]) {
    def asScala: Option[T] = if (value.isPresent()) Some(value.get()) else None
  }
  */

  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (o.isPresent) Some(o.get()) else None
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala
  println(scalaOption)
}
