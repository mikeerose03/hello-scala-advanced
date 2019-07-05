package exercises

object TypeMembers extends App {
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  /*
  trait NumberList {
    type A <: Number
    def head: A
    def tail: NumberList
  }
  */

  // answer

  trait ApplicableToNumbers {
    type A <: Number
  }

//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
//    type A = String
//    def head = hd
//    def tail = tl
//  } // DOES NOT COMPILE

  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = tl
  }

}
