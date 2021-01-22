package conversion

/**
 *  隐式函数： 基本类型转换
 */
object Demo1 {
  def main(args: Array[String]): Unit = {
    val num : Double = 3
   // print(num)
    val num1: Int = 3.5  //由于下面得隐式函数，不报错
    print(num1)
  }

  //隐式转换函数
  implicit def f1(d:Double): Int = {
    d.toInt
  }
}
