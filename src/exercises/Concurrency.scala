package exercises

object Concurrency extends App {

  // 1. inception threads
  def inception(n: Int = 1): Unit = {
    if (n <= 50) {
      val aThread = new Thread(() => {
        println(s"hello from thread $n")
      })
      inception(n+1)
      aThread.start()
      aThread.join()
    } else println("done")
  }

//  inception()
  // answer
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = {
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i+1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })
  }

//  inceptionThreads(50).start()
  // 2.
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
//  threads.foreach(_.start())

//  println(x)
  // biggest possible value of x: 100
  // answer: 100
  // smallest possible value of x: 98
  // answer: 1

  var message = ""
  var awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
//  awesomeThread.start()
//  Thread.sleep(2000)
//  println(message)
  // the value of message is "Scala is awesome"
  // answer: the value is almost always "Scala is awesome" but it is not guaranteed
}
