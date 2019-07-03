package lectures.part4Implicits

object TypeClassEnrichment extends App {

  // Enrichment

  case class User(name: String, age: Int, email: String)

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  object UserNameSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div> ${user.name} </div>"
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div> $value </div>"
  }

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  val john: User = User("John", 25, "john@gmail.com")

  println(john.toHTML) // println(new HTMLEnrichment[User](john).toHTML(UserSerializer)

  /* WHY ENRICHMENT IS COOL
  * - extend to new types
  * - choose implementation (by importing or passing it implicitly
  * - super expressive
  * */

  println(2.toHTML)
  println(john.toHTML(UserNameSerializer))

  /* Elements of Enriching Type Classes
  * - type class itself (e.g. HTMLSerializer[T] {...})
  * - type class instances (some of which are implicit e.g. UserSerializer, IntSerializer)
  * - conversion with implicit classes (e.g. HTMLEnrichment)
  * */

  // context bounds

  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T : HTMLSerializer](content: T): String =
    s"<html><body> ${content.toHTML}</body></html>"
  /* WHATs happening here?
     the "HTMLSerializer" in [T : HTMLSerializer] is a "context bound"
     which tells the compiler to inject HTMLSerializer to the "content.toHTML(serializer)"
   */

  /* implicitly
    - is a method
   */

  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]

}
