package exercises

import scala.collection.mutable
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._

object Futures extends App {

  // 1
  def timeTravel(year: String) = Future(s"Arrived at year $year")

//  timeTravel("1998").foreach(println)
//  Thread.sleep(1000)

  // answer
  def fullfillImmediately[T](value: T): Future[T] = Future(value)

  // 2

  def inSequence(future1: Future[String], future2: Future[String]): Future[List[String]] =
    for {
      f1 <- future1
      f2 <- future2
    } yield {
      val res = List(f1, f2)
      res
    }

  // answer
  def inSequenceAnswer[A,B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second)

//  inSequence(timeTravel("2015"), timeTravel("1998")).foreach(println)
//  Thread.sleep(1000)

  // 3 - answer

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete)

    promise.future
  }

  // 4 - answer

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]

    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result))
        lastPromise.tryComplete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)
    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }
  val slow = Future {
    Thread.sleep(200)
    45
  }
//  first(fast, slow).foreach(println)
//  last(fast, slow).foreach(println)

  Thread.sleep(1000)
  // 5

  def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] = {
    val value = Await.result(action(), 2.seconds)
    if (!condition(value)) retryUntil(action, condition)
    else Future(value)
  }

  //  val value = Await.result(retryUntil[Int](() => Future[Int] {
  //    val value = new Random().nextInt(10)
  //    println(s"try $value")
  //    value
  //  }, (x: Int) => x % 5 == 0), 2.seconds)
  //
  //  println("Finally! " + value)
  //  Thread.sleep(500)

  // answer
  def retryUntilAnswer[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntilAnswer(action, condition)
      }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUntilAnswer(action, (x: Int) => x < 50).foreach(result => println("settled at " + result))

  Thread.sleep(10000)
}
