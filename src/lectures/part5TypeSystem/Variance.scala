package lectures.part5TypeSystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // is the problem of "inheritance" or more specifically the type substitution of generics

  class Cage[T]
  // covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat] // will not COMPILE

  // contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // COVARIANT POSITION

//  class ContravariantCage[-T](val animal: T) // WILL NOT COMPILE!

//  class CovariantVariableCage[+T](var animal: T) // WILL ALSO NOT COMPILE!
  // types of vars are in CONTRAVARIANT POSITION

  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

//  class ContravariantVariableCage[-T](var animal: T) // WILL NOT COMPILE!
  // also in COVARIANT POSITION

  class InvariantVariableCage[T](var animal: T) //OK

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // method arguments are in CONTRAVARIANT POSITION
//  }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat) // this should'nt happen
   */

  class AnotherContravariantCage[-T] { // OK
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
//  acc.addAnimal(new Dog) // DOES NOT WORK
  acc.addAnimal(new Cat) // OK
  class Kitty extends Cat
  acc.addAnimal(new Kitty) // OK

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // note: METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
//    def get(isItAPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION

    /*
    val catShop = new PetShop[Animal] {
      def get(isPuppy: Boolean): Animal = new Cat
    }

    val dogShop: PetShop[Dogs] = catshop // should not happen!
    dogShop.get(true) // will return the WRONG TYPE
   */

    def get[S <: T](isPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
//  val evilCat = shop.get(true, new Cat) // WILL RETURN RUNTIME EXCEPTION
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova) // OK

  /*
  * BIG RULES
  *   - method arguments are in CONTRAVARIANT position
  *   - return types are in COVARIANT position
  * */
}
