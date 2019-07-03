package lectures.part2AFP

object Monads extends App {

  // our own Try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing]  {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  * left-identity
  *
  * until.flatMap(f) = f(x)
  * Attempt(x).flatMap(f) = f(x) // only makes sense for success case
  * Success(x).flatMap(f) = f(x) // proven
  *
  * right-identity
  *
  * attempt.flatMap(unit) = attempt
  * Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  * Fail(_).flatMap(...) = Fail(e) // proven
  *
  * associativity
  *
  * // Fail
  * attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  * Fail(e).flatMap(f).flatMap(g) = Fail(e)
  * Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e) // proven
  *
  * // Success
  * Success(v).flatMap(f).flatMap(g) =
  *   f(v).flatMap(f) OR Fail(e)
  *
  * Success(v).flatMap(x => f(x).flatMap(g)) =
  *   f(v).flatMap(f) OR Fail(e)
  * */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)
}
