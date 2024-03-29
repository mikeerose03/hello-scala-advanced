package lectures.part3Concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration._
// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesAndPromises extends App {

  def calculateMeaningOfLife = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life in another thread
  } // (global) which is passed by the compiler

  println("waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  } // SOME thread

  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // database
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the "DB"
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  /* mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(ex) => ex.printStackTrace()
  }

  Thread.sleep(1000) */

  // function composition of futures
  // map, flatMap, filter

  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  // for-comprehension
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "forever alone")
  }

  val aFetchProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM Banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the database
      Thread.sleep(5000)
      User(name)
    }

    def createTransaction(user: User, merchant: String, amount: Double): Future[Transaction] = Future {
      // simulate some process
      Thread.sleep(1000)
      Transaction(user.name, merchant, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchant: String, cost: Double): String = {
      // fetch the user from "DB"
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchant, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds) // implicit conversion -> pimp my library
    }
  }

  // println(BankingApp.purchase("Daniel", "iPhone 12", "Rock the JVM Store", 3000))

  // promises

  val promise = Promise[Int]() // "controller" over a future
  val future = promise.future

  // thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I've received " + r)
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // "fulfilling the promise
    promise.success(42)
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)
}
