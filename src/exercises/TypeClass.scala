package exercises

object TypeClass extends App {

  // Exercise #1
  case class User(name: String, age: Int, email: String)
  /*
  trait Equal[T] {
    def isEqual(value1: T, value2: T): Boolean
  }

  object EmailEqual extends Equal[User] {
    override def isEqual(user1: User, user2: User): Boolean = user1.email == user2.email
  }

  object NameEqual extends Equal[User] {
    override def isEqual(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object AgeEqual extends Equal[User] {
    override def isEqual(user1: User, user2: User): Boolean = user1.age == user2.age
  }
  */

  // implicits
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
/*

  object Equal {
    def apply[T](implicit instance: Equal[T]) = instance
  }

  object Equality {
    def isEqual[T](value1: T, value2: T)(implicit equal: Equal[T]) = equal(value1, value2)

    def apply[T](implicit equal: Equal[T]) = equal
  }
 */

  // answer
  // AD-HOC polymorphism
  implicit object FullEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean = user1.name == user2.name && user1.age == user2.age && user1.email == user2.email
  }

  object NameEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer(a, b)
  }

  val john = User("John", 32, "john@gmail.com")
  val john2 = User("John", 24, "john@gmail.com")

  println(Equal(john, john2))

  // Exercise #2

  // #1
  /* // my answer
  implicit class Equalizer[T](a: T) {
    def ===(b: T)(implicit equalizer: Equal[T]): Boolean = equalizer(a, b)
    def !==(b: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(a, b)
  }
  */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, other)
  }

  println(john === john2)
  /*
    john.===(john2)
    new TypeSafeEqual(john).===(john2)
    new TypeSafeEqual(john).===(john2)(FullEquality)
   */
  println(john !== john2)

  /*
  * WHY THIS IS TYPE SAFE
  *
  * john == 43 works!
  * john === 43 DOES NOT WORK!
  * */
}
