package lectures.part3Concurrency

import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector
import scala.concurrent.forkjoin.ForkJoinPool

object ParallelUtils extends App {

  // 1 - parallel collections

  val parList = List(1,2,3).par

  val aParVector = ParVector[Int](1,2,3)

  /*
  * Seq
  * Vector
  * Array
  * Map - Hash, Trie
  * Set - Hash, Trie
  * */

  def measure[T](operation: => T): Long = {
    var time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 10000).toList
  val serialTime  = measure {
    list.map(_ + 1)
  }
  println("serial time " + serialTime)
  val parallelTime = measure {
    list.par.map(_ + 1)
  }

  println("parallel time " + parallelTime)

  /*
  * Map-reduce model
  *   - split the elements into chunks - Splitter
  *   - operation
  *   - recombined - Combiner
  * */

  // map, flatMap, filter, foreach, reduce, fold

  // NOTE: fold, reduce can yield different results when used with par
  println(List(1,2,3).reduce(_ - _)) // -4
  println(List(1,2,3).par.reduce(_ - _)) // 2

  // synchronization
  var sum = 0
  List(1,2,3).par.foreach(sum += _)
  println(sum) // race conditions

  // configuring
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
  * alternatives
  *   - ThreadPoolTaskSupport - deprecated
  *   - ExecutionContextTaskSupport(EC)
  * */

  /* creating a new task support
  aParVector.tasksupport = new TaskSupport {
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???

    override def parallelismLevel: Int = ???

    override val environment: AnyRef = ???
  } */

  // 2 - atomic ops and references

  val atomic = new AtomicReference[Int](2)

  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-sage write

  atomic.getAndSet(5) // thread safe combo

  atomic.compareAndSet(38, 56)
  // if the value is 38, then set 56
  // reference equality

  atomic.updateAndGet(_ + 1)
  atomic.getAndUpdate(_ + 1)

  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
  atomic.getAndAccumulate(12, _ + _)
}
