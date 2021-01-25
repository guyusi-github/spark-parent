package conversion

/**
 *  Scala 函数式编程高级 : 控制抽象
 *
 *
 */
object Demo7 {
  def main(args: Array[String]): Unit = {
    //特殊的模式匹配1
    val (x , y ) = (1,2)
    println(x+ "" +  y)
    //特殊的模式匹配2
    val (a,b) = BigInt(10) /% 3
    print(a,b) //3 1
    //特殊的模式匹配3
    val tuples: List[(String, Int)] = List(("a" -> 1), ("b" -> 2), ("c" -> 3))
    for ((k,v) <- tuples) {
      println(k + "--> " + v)
    }
  }


}
