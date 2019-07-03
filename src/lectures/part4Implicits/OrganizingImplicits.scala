package lectures.part4Implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
//  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1,4,5,3,2).sorted)

  // scala.Predef

    /*
    * Implicits (used as implicit parameters):
    *   - val/var
    *   - object
    *   - accessor methods = defs with no parenthesis
    * */
}
