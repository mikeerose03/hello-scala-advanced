package lectures.part4Implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T: Serializer](messages: T): Int
    def receive[T: Serializer](message: T, statusCode: T): Int
    def receive(future: Future[P2PRequest]): Int
//    def receive(future: Future[P2PResponse]): Int //DOES NOT COMPILE
    // lots of overloads
  }

  /*
  * COST of this structure
  *   1 - type erasure
  *   2 - lifting doesn't working for all overloads
  *     // e.g.
  *     val receiveFV   = receive _ //??
  *   3 - code duplication
  *   4 - type inferrence and default args
  *     //e.g. actor.receive(??!)
  * */

  // better structure

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2P Request
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(request: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2P Response
      println("Handling P2P response")
      42
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  /* Magnet Pattern Pros */

  // 1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    def apply(): Int = 2
  }

  receive(Future(new P2PResponse))
  receive(Future(new P2PRequest))

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // "magnitized"
  trait AddMagnet { // notice that there's no type parameter
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x+1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("3"))

  // con: Cannot add type parameter since the compiler would not know what type its being lifted for
  // e.g. receiveFV(new P2PResponse) would not be compiled

  /* Magnet Pattern Cons */

  /*
  * 1 - verbose
  * 2 - difficult to read
  * 3 - you can't name or place default arguments
  *   e.g. receive() cannot compile
  * 4 - call by name doesn't work correctly
  * */

}
