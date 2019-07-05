package exercises

object PathDependentTypes extends App {

  /*
  trait Item[Key]
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType, Key <: ItemType](key: Key): ItemType

  get[IntItem, Int](42)
  get[StringItem, String]("home")

  get[IntItem, Int]("scala")
  */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42)
  get[StringItem]("home")

//  get[IntItem]("scala")

}
