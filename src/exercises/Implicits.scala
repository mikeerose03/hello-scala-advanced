package exercises

object Implicits extends App {

  // exercise #1

  // my answer
//  implicit def alphabeticalOrdering: Ordering[Person] =
//    Ordering.fromLessThan(_.name.charAt(0).toLower < _.name.charAt(0).toLower)

  case class Person(name: String, age: Int)

  val person = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

//  println(person.sorted)

//  implicit def ageOrdering: Ordering[Person] =
//    Ordering.fromLessThan(_.age < _.age)

  /* answer
  *  implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan(_.name.compareTo(_.name) < 0)
  * */

  /*
  * Implicit Scope
  *   1. normal scope = LOCAL SCOPE (basically where we write our code)
  *   2. imported scope
  *   3. companions of all types involved in the method signature
  *     - List
  *     - Ordering
  *     - all the types involved = A or any supertype
  * */

  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age.compareTo(b.age) < 0)
  }

  import AlphabeticNameOrdering._
  println(person.sorted)

  // exercise #2

  case class Purchase(nUnits: Int, unitPrice: Double)

  // order by total price

  // order by unit count

  // order by unit price

  object TotalPriceOrdering {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => (a.nUnits * a.unitPrice).compareTo(b.nUnits * b.unitPrice) < 0)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits.compareTo(b.nUnits) < 0)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice.compareTo(b.unitPrice) < 0)
  }

  val purchases: List[Purchase] = List(
    Purchase(2, 300.0),
    Purchase(1, 500.0),
    Purchase(10, 40.0),
  )

  import UnitPriceOrdering._

  println(purchases.sorted)

  /* //answer
  * object Purchase { set as companion object since it is widely used
  *   implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  * }
  *
  * object UnitCountOrdering {
  *   implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  * }
  *
  * object UnitPriceOrdering {
  *   implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  * }
  * */
}
