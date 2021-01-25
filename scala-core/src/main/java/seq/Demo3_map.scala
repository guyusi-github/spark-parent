package seq

import scala.collection.mutable

object Demo3_map {

  def main(args: Array[String]): Unit = {

    //scala k-v中以特殊方式申明    不可变
    val map: Map[String, Int] = Map("a" -> 1,"b" -> 2,"c" -> 3)
    //增
    val map1: Map[String, Int] = map + ("d"-> 4)
    println(map1)
    println(map == map1) //false
    //删除
    val value: Any = map.- ("c")

    // 可变
    mutable.Map("a" -> 1,"b" -> 2,"c" -> 3)
  }
}
