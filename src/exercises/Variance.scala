package exercises

object Variance extends App {

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  /*
  class InvariantParking[T](val things: List[T]) {
    def park(v: T): InvariantParking[T] = ???
    def impound(vs: List[T]): InvariantParking[T] = ???
    def checkVehicles(c: String): List[T] = ???
    def flatMap(f: () => InvariantParking[T]): List[T] =
      f(things.head).things ++ new InvariantParking(things.tail).flatMap(f)
  }

  class CovariantParking[+T](val things: List[T]) {
    def park[B](v: B): CovariantParking[B] = ???
    def impound[B](vs: List[B]): CovariantParking[B] = ???
    def checkVehicles(c: String): List[T] = ???
    def flatMap[B](f: T => CovariantParking[B]): List[B] =
      f(things.head).things ++ new CovariantParking(things.tail).flatMap(f)
  }

  class ContravariantParking[-T](val things: List[T]) {
    def park[B](v: B): CovariantParking[B] = ???
    def impound[B](vs: List[B]): CovariantParking[B] = ???
    def checkVehicles[B](c: String): List[B] = ???
    def flatMap[S <: T](f: S => CovariantParking[S]): List[S] =
      f(things.head).things ++ new ContravariantParking(things.tail).flatMap(f)
  }
  */

  // answers

  // #1
  class IParking[T](vs: List[T]) {
    def park(v: T): IParking[T] = ???
    def impound(v: List[T]): IParking[T] = ???
    def checkVehicles(c: String): List[T] = ???
  }

  class CParking[+T](vs: List[T]) {
    def park[S >: T](v: S): CParking[S] = ???
    def impound[S >: T](v: List[S]): CParking[S] = ???
    def checkVehicles(c: String): List[T] = ???
  }

  class XParking[-T](vs: List[T]) {
    def park(v: T): XParking[T] = ???
    def impound(vs: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](c: String): List[S] = ???
  }

  /*
    Rule of the thumb
     - use covariance = COLLECTION OF THINGS
     - use contravariance = GROUP OF ACTIONS
   */

  // #2
  class CParking2[+T](vs: IList[T]) {
    def park[S >: T](v: S): CParking2[S] = ???
    def impound[S >: T](vs: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](c: String): IList[S] = ???
  }

  class XParking2[-T](vs: IList[T]) {
    def park(v: T): XParking2[T] = ???
    def impound[S <: T](vs: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](c: String): IList[S] = ???
  }

  // #3
  class IParking3[T](vs: List[T]) {
    def park(v: T): IParking3[T] = ???
    def impound(v: List[T]): IParking3[T] = ???
    def checkVehicles(c: String): List[T] = ???

    def flatMap[S](f: T => IParking3[S]): IParking3[S] = ???
  }

  class CParking3[+T](vs: IList[T]) {
    def park[S >: T](v: S): CParking3[S] = ???
    def impound[S >: T](vs: IList[S]): CParking3[S] = ???
    def checkVehicles[S >: T](c: String): IList[S] = ???

    def flatMap[S](f: T => CParking3[S]): CParking3[S] = ???
  }

  class XParking3[-T](vs: IList[T]) {
    def park(v: T): XParking3[T] = ???
    def impound[S <: T](vs: IList[S]): XParking3[S] = ???
    def checkVehicles[S <: T](c: String): IList[S] = ???

    def flatMap[R <: T, S](f: Function[R, XParking3[S]]): XParking3[S] = ???
  }

}
