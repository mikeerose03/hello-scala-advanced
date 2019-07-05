package lectures.part5TypeSystem

object FBoundedPolymorphism extends App {

  /*
  trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Animal] = ??? // List[Cat] !!
  }

  class Dog extends Animal {
    override def breed: List[Animal] = ??? // List[Dog] !!
  }
   */

  // Solution #1 - naive
  /*
  trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Cat] = ??? // List[Cat] !!
  }

  class Dog extends Animal {
    override def breed: List[Dog] = ??? // List[Dog] !!
  }
   */

  // Solution #2 - FBP
  /*
  trait Animal[A <: Animal[A]] { // recursive type
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    def breed: List[Animal[Cat]] = ??? // List[Cat] !!
  }

  class Dog extends Animal[Dog] {
    def breed: List[Animal[Dog]] = ??? // List[Dog] !!
  }
   */

  // Limitations
  /*
  class Crocodile extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ???
  } // this SHOULD be illegal
   */

  trait Entity[E <: Entity[E]] // often present in ORMs
  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = ???
  }

  // Solution #3 - FBP with self types


  /* ATTEMPT */
  /*
  trait StrictlyAnimal {
    type Key
  }

  trait Animal[K] extends StrictlyAnimal { // recursive type
    type Key = K
    def breed[S <: K]: List[Animal[K]]
  }

  class Cat extends Animal[Cat] {
    def breed[S <: Cat]: List[Animal[Cat]] = ??? // List[Cat] !!
  }

  class Dog extends Animal[Dog] {
    def breed[S <: Dog]: List[Animal[Dog]] = ??? // List[Dog] !!
  }
   */

  // answer
/*
  trait Animal[A <: Animal[A]] { self: A =>
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    def breed: List[Animal[Cat]] = ??? // List[Cat] !!
  }

  class Dog extends Animal[Dog] {
    def breed: List[Animal[Dog]] = ??? // List[Dog] !!
  }

 */

/*
  class Crocodile extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ???
  }
 */ // self type enforces this to be ILLEGAL

  // Limitations

  /*
  // THIS SHOULD BE ILLEGAL
  trait Fish extends Animal[Fish]
  class Shark extends Fish {
    override def breed: List[Animal[Fish]] = List(new Cod)
  }

  class Cod extends Fish {
    override def breed: List[Animal[Fish]] = ???
  }
   */

  // Solution #4 - Type Classes!
/*
  trait Animal
  trait CanBreed[A] {
    def breed(a: A): List[A]
  }

  class Dog extends Animal
  object Dog {
    implicit object DogsCanBreed extends CanBreed[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }

  val dog = new Dog
  dog.breed // sure to return List[Dog]
  /*
    new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
    implicit value to pass to breed: Dog.DogsCanBreed
   */

  class Cat extends Animal
  object Cat {
    implicit object CatsCanBreed extends CanBreed[Dog] { // WRONG CODE
      def breed(a: Dog): List[Dog] = List()
    }
  }

  val cat = new Cat
  // cat.breed // DOES NOT COMPILE

 */
  // Solution #5
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

  val cat = new Cat
  // cat.breed // WILL NOT COMPILE
}
