package lectures.part5TypeSystem

object Reflection extends App {

  // reflection problem + macros + quasiquotes => METAPROGRAMMING

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  // STEPS
  // 0 - import
  import scala.reflect.runtime.{universe => ru}

  // 1 - MIRROR
  val m = ru.runtimeMirror(getClass.getClassLoader)
  // 2 - create class object = "class description"
  val clazz = m.staticClass("lectures.part5TypeSystem.Reflection.Person") // creating a class object by NAME
  // 3 - create a reflected mirror = "can DO things like instantiate objects, invoke methods"
  val cm = m.reflectClass(clazz)
  // 4 - get the constructor
  val constructor = clazz.primaryConstructor.asMethod
  // 5 - reflect the constructor
  val constructorMirror = cm.reflectConstructor(constructor)
  // 6 - invoke the constructor
  val instance = constructorMirror.apply("John")

  println(instance)

  // I have an instance
  val p = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName"

  // 1 - mirror
  // 2 - reflect the instance
  val reflected = m.reflect(p)
  // 3 - method symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can DO things
  val method = reflected.reflectMethod(methodSymbol)
  // 5 - invoke the method

  method.apply()

  // type erasure

  // pp (pain point) #1: cannot differentiate types at runtime
  val numbers = List(1,2,3)
  numbers match {
    case listOfStrings: List[String] => println("list of strings")
    case listOfNumbers: List[Int] => println("list of numbers")
  } // will print list of strings

  // pp #2: limitations on overloads
  def processList(list: List[Int]): Int = 42
//  def processList(list: List[String]): Int = 44 // will NOT compile

  // TypeTags
  // 0 - import
  import ru._

  // 1 - creating a type tag "manually"
  val ttag = typeTag[Person]
  println(ttag.tpe)

  class MyMap[K, V]

  // 2 - pass type tags as implicit parameters
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) // (typeTag: TypeTag[MyMap[Int, String]])
  println(typeArgs)

  def isSubType[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubType[Dog, Animal])

  // 3 - method symbol
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method = can DO things
  val sameMethod = reflected.reflectMethod(anotherMethodSymbol)
  // 5 - invoke the method
  sameMethod.apply()
}
