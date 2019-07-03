package lectures.part4Implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@gmail.com").toHtml

  /*
  * Cons
  *   1 - only works for the types WE write
  *   2 - only ONE implementation out of quite a number
  *
  * // pattern matching - another option to solve cons
  *
  * */

//  object HTMLSerializer {
//    def serializeToHtml(value: Any) = value match {
//      case User(n, a, e) =>
//      case _ =>
//    }
//  }

  /*
  * Cons for pattern matching
  * 1 - lost type safety
  * 2 - need to modify the code everytime
  * 3 - still ONE implementation
  * */

  // better design

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  val john = User("John", 32, "john@gmail.com")
//  println(UserSerializer.serialize(john))

  // Pros of this design

  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  // 2 - we can define multiple serializers for the same type
  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // TYPE CLASS

  // Type Class Template
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  // using with implicits and type class pattern

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div> $value </div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john)(UserSerializer))

  // access to the entire type class interface
  println(HTMLSerializer[User](UserSerializer).serialize(john))
}
