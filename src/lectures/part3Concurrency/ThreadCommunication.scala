package lectures.part3Concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  * the producer-consumer problem
  *
  * producer -> [ x ] -> consumer
  * */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons() = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("consumer waiting...")
      while(container.isEmpty) {
        println("consumer actively waiting...")
      }

      println("consumer has consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("producer computing...")
      Thread.sleep(500)
      val value = 42
      println("producer has produced after loong work, the value " + value)
      container.set(value)
    })
    consumer.start()
    producer.start()
  }

  //naiveProdCons()

  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("consumer waiting...")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println("consumer has consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("producer hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("producer is producing the value " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  smartProdCons()

  /* level 2
  * producer -> [ ? ? ? ] -> consumer
  *
  * */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          // hey producer, there's empty space available, are you lazy?!
          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // hey consumer, new food for you
          buffer.notify()

          i+=1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  /* level 3
  * producer -> [ ? ? ? ] -> consumer 1
  * producer2 ---^      ^---- consumer2
  * */

  // my answer
  /*
  object Consumer {
    def create(name: String, buffer: mutable.Queue[Int]) = {
      new Thread(() => {
        val random = new Random()

        while(true) {
          buffer.synchronized {
            if (buffer.isEmpty) { // wrong! (should be while instead of if)
              println(s"[consumer-$name] buffer empty, waiting...")
              buffer.wait()
            }

            // there must be at least ONE value in the buffer
            val x = buffer.dequeue()
            println(s"[consumer-$name] consumed " + x)

            // hey producer, there's empty space available, are you lazy?!
            buffer.notify()
          }

          Thread.sleep(random.nextInt(500))
        }
      })
    }
  }

  object Producer {
    def create(name: String, buffer: mutable.Queue[Int], capacity: Int) = {
      new Thread(() => {
        val random = new Random()
        var i = 0

        while (true) {
          buffer.synchronized {
            if (buffer.size == capacity) {  // wrong! (should be while instead of if)
              println(s"[producer-$name] buffer is full, waiting...")
              buffer.wait()
            }

            // there must be at least ONE empty space in the buffer
            println(s"[producer-$name] producing " + i)
            buffer.enqueue(i)

            // hey consumer, new food for you
            buffer.notify()

            i+=1
          }

          Thread.sleep(random.nextInt(250))
        }
      })
    }
  }

  def prodConsChonkBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]

    val consumer1 = Consumer.create("one", buffer)
    val producer1 = Producer.create("one", buffer, 3)

    val consumer2 = Consumer.create("two", buffer)
    val producer2 = Producer.create("two", buffer, 3)

    consumer1.start()
    consumer2.start()

    producer1.start()
    producer2.start()
  }
  */

//  prodConsChonkBuffer()

  // answer

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      new Thread(() => {
        val random = new Random()

        while(true) {
          buffer.synchronized {
            while (buffer.isEmpty) {
              println(s"[consumer-$id] buffer empty, waiting...")
              buffer.wait()
            }

            // there must be at least ONE value in the buffer
            val x = buffer.dequeue()
            println(s"[consumer-$id] consumed " + x)

            buffer.notify()
          }

          Thread.sleep(random.nextInt(500))
        }
      })
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      new Thread(() => {
        val random = new Random()
        var i = 0

        while (true) {
          buffer.synchronized {
            while (buffer.size == capacity) {
              println(s"[producer-$id] buffer is full, waiting...")
              buffer.wait()
            }

            // there must be at least ONE empty space in the buffer
            println(s"[producer-$id] producing " + i)
            buffer.enqueue(i)

            buffer.notify()

            i+=1
          }

          Thread.sleep(random.nextInt(500))
        }
      })
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

  //multiProdCons(3, 3)

  /*
  * 1. Think of an example where notifyAll acts in a different way than notify?
  *   - when the threads are not synchronized.
  * 2. Create a deadlock
  *   - when no one is notifying the thread and all threads are just waiting
  * 3. Create a livelock -
  *   - when no thread can proceed because they are yielding execution to each other
  * */

  // 1. notifyAll

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(x => new Thread(() => {
      bell.synchronized {
        println(s"[thread $x] is waiting...")
        bell.wait()
        println(s"[thread $x] hurray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      bell.synchronized {
        println("[announcer] Rock'n roll!")
        bell.notifyAll()
      }
    }).start()
  }

//  testNotifyAll()

  // 2. deadlock case

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: My friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"
    def switchSide() = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend) = {
      while(this.side == other.side) {
        println(s"$this: Oh, but please, $other, feel free to pass...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

//  new Thread(() => sam.bow(pierre)).start() // sam's lock,    | then pierre's lock
//  new Thread(() => pierre.bow(sam)).start() // pierre's lock, | then sam's lock

  // 3. - livelock

  new Thread(() => sam.pass(pierre)).start
  new Thread(() => pierre.pass(sam)).start

}
