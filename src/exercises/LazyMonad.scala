package exercises

// 1.
trait Idle[+A] {
  def flatMap[B](f: A => Idle[B]): Idle[B]
}
object Idle {
  def apply[A](v: => A): Idle[A] = new IdleValue(v)
}

class IdleValue[+A](el: A) extends Idle[A] {
  def flatMap[B](f: A => Idle[B]): Idle[B] = f(el)
}

class IdleUnit extends Idle[Nothing] {
  def flatMap[B](f: Nothing => Idle[B]): Idle[B] = this
}

// answer
class Lazy[+A](value: => A) {
  private lazy val internalValue = value
  def flatMap[B](f: ( => A) => Lazy[B]): Lazy[B] = f(internalValue)
  def use: Unit = internalValue
}

object Lazy {
  def apply[A](value: => A): Lazy[A] = new Lazy(value)
}

object LazyMonad extends App {
  val idleVal = Idle {
    println("Today I feel like doing anything")
    42
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  * left-identity
  *
  * unit.flatMap(f) = f(v)
  * Lazy(v).flatMap(f) = f(v)
  *
  * right-identity
  *
  * l.flatMap(unit) = 1
  * Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)
  *
  * associativity: l.flatMap(f).flatMap(g) = l.flatMap(x =< f(x).flatMap(g))
  *
  * Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  * Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
  * */

  // 2.
  /*
  * Modan[T] { // List
  *   def flatMap[B](f: T => Monad[B]): Monad[B] = ... (assuming that this is already implemented)
  *   def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x)))
  *   // my answer
  *   def flatten(m: Monad[Monad[T]]): Monad[T] =
  *     for {
  *       mm <- m
  *       i <- mm
  *     } yield Monad + i
  *
  *   // answer
  *   def flatten(m: Monad[Monad[T]): Monad[T] = m.flatMap((x: Monad[T]) => x)
  * */
}

