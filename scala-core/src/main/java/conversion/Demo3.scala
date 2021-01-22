package conversion

/**
 *  隐式函数： 隐式值使用
 */
object Demo3 {
  def main(args: Array[String]): Unit = {
    // 隐式值变量(值)
    implicit val name : String = "Scala"

    //申明方法
    def hello(implicit content : String) : Unit = {
      println("hello" + content)
    }   // 没有参数下 优先找隐式值 比默认值高
    def hello1(implicit content : String = "spark") : Unit = {
      println("hello" + content)
    }   // 没有参数下 优先找隐式值 比默认值高  就是方法后面没有跟（）
    hello  //helloScala
    hello1 //helloScala
    hello1() //hellospark
  }
}
