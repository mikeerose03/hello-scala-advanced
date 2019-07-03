package exercises

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean = contains(elem)
//
//  def head: A
//  def tail: MySet[A]
//  def isEmpty: Boolean

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(set: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
/*
  def remove(elem: A): MySet[A]
  def intersection(set: MySet[A]): MySet[A]
  def difference(set: MySet[A]): MySet[A]
 */
  def -(elem: A): MySet[A]
  def &(set: MySet[A]): MySet[A]
  def --(set: MySet[A]): MySet[A]

  def unary_! : MySet[A]
}
/*
class NonEmptySet[A](h: A, t: MySet[A]) extends MySet[A] {

  def head: A = h
  def tail: MySet[A] = t
  def isEmpty: Boolean = false

  def contains(elem: A): Boolean = {
    def containsAux(set: MySet[A]): Boolean =
      if (set.head == elem) true
      else if (set.tail.isEmpty) false
      else containsAux(set.tail)

    containsAux(this)
  }
  def +(elem: A): MySet[A] = new NonEmptySet(elem, this)
  def ++(set: MySet[A]): MySet[A] = new NonEmptySet(h, t ++ set)

  def map[B](f: A => B): MySet[B] = new NonEmptySet(f(h), t.map(f))
  def flatMap[B](f: A => MySet[B]): MySet[B] = f(h) ++ t.flatMap(f)
  def filter(predicate: A => Boolean): MySet[A] =
    if (predicate(h)) new NonEmptySet(h, t.filter(predicate))
    else t.filter(predicate)
  def foreach(f: A => Unit): Unit = {
    def foreachAux(set: MySet[A]): Unit =
      if (!set.isEmpty) {
        f(set.head)
        foreachAux(set.tail)
      }
    foreachAux(this)
  }

}

class EmptySet[A] extends MySet[A] {

  def head: A = throw new NoSuchElementException
  def tail: MySet[A] = throw new NoSuchElementException
  def isEmpty: Boolean = true

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet(elem, this)
  def ++(set: MySet[A]): MySet[A] = set

  def map[B](f: A => B): MySet[B] = new EmptySet()
  def flatMap[B](f: A => MySet[B]): MySet[B] =  new EmptySet()
  def filter(predicate: A => Boolean): MySet[A] =  new EmptySet()
  def foreach(f: A => Unit): Unit =  new EmptySet()
}
*/

class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(set: MySet[A]): MySet[A] = set

  def map[B](f: A => B): MySet[B] = new EmptySet
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
/*
  def remove(elem: A): MySet[A] = new EmptySet
  def intersection(set: MySet[A]): MySet[A] = new EmptySet
  def difference(set: MySet[A]): MySet[A] = set
 */
  def -(elem: A): MySet[A] = this
  def &(set: MySet[A]): MySet[A] = this
  def --(set: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

// all elements of type A which satisfy a property
// { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)
  // { x in A | property(x) } + element = { x in A | property(x) || x == element }
  def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)
  // { x in A | property(x) } + element = { x in A | property(x) || set contains x }
  def ++(set: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || set(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(f: A => Unit): Unit = politelyFail

  def filter(predicate: A => Boolean): MySet[A] =
    new PropertyBasedSet[A](x => property(x) && predicate(x))
  def -(elem: A): MySet[A] = filter(x => x != elem)
  def &(set: MySet[A]): MySet[A] = filter(set)
  def --(set: MySet[A]): MySet[A] = filter(!set)
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

class NonEmptySet[A](h: A, t: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean =
    elem == h || t.contains(elem)
  def +(elem: A): MySet[A] =
    if (this contains elem) this
    else new NonEmptySet(elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] =
    t ++ anotherSet + h

  def map[B](f: A => B): MySet[B] = (t map f) + f(h)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (t flatMap f) ++ f(h)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = t filter predicate
    if (predicate(h)) filteredTail + h
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(h)
    t foreach f
  }

  /*
  def remove(elem: A): MySet[A] = this filter (_ != elem)
  def intersection(set: MySet[A]): MySet[A] = this filter (set contains _)
  def difference(set: MySet[A]): MySet[A] = (this filter (!set.contains(_))) ++ set filter (!this.contains(_))
   */

  def -(elem: A): MySet[A] =
    if (h == elem) t
    else t - elem + h
  def &(set: MySet[A]): MySet[A] = filter(set)
  def --(set: MySet[A]): MySet[A] = filter(x => !set(x))

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}


object MySet {
  def apply[A](values: A*): MySet[A] = {
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet())
  }
}

object SetTest extends App {
  val set = MySet(1,2,3,4)
  //set + 5 ++ MySet(-1,-2) + 3 flatMap (x => MySet(x, 10 * x)) filter (_%2 == 0) foreach println

  MySet(1, 2, 3, 4, 5, 6) & set - 2 -- set foreach println

  val negative = !set //s.unary_! = all the naturals that are not equal to 1,2,3,4
  println(negative(2))
  println(negative(-2))
  val negativeEven = negative.filter(_ % 2 == 9)
  println(negativeEven(5))
  val negativeEven5 = negativeEven + 5 // all the numbers > 4 + 5
  println(negativeEven(5))
}