package exercises

object PartialFunction extends App {

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 3 => 999
    }
    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 3
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
    case _ => "Im afraid I don't understand what you're saying."
  }

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)
    //foreach(line => println("bot says: " + chatbot(line)))

  /*
  val divide = new PartialFunction[Int, Int] {
    def apply(x: Int) = 42 / x
    def isDefinedAt(x: Int) = x != 0
  }

  val quotient42 = divide(2)
  println(quotient42)

  scala.io.Source.stdin.getLines().foreach {
    case "hello" => {
      println("bot: hi")
      printf("you: ")
    }
    case "hi" => {
      println("bot: hello")
      printf("you: ")
    }
    case "how are you" => {
      println("bot: im fine thank you")
      printf("you: ")
    }
    case _ => {
      println("bot: Im afraid I don't understand what you're saying.")
      printf("you: ")
    }
  } */
}
