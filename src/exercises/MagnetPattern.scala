package exercises

import scala.collection.mutable

object MagnetPattern extends App {
  // prove call by name doesn't work well in magnet pattern

  // answer
  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandleMagnet(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha"
  }

//  handle(sideEffectMethod())
  handle {
    println("Hello, Scala") // ignored by magnet
    "magnet"
  }
}
