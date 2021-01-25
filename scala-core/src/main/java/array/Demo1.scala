package array

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * list 集合学习  类型： 可变& 不可变(immutable)
  */
object Demo1 {
  def main(args: Array[String]): Unit = {
    val ints : Array[Int] = Array(1,2,3,4,5)
    //输出方式一
    ints.foreach(println)

    //输出方式二  ： 自定义函数
    def printxxx(i : Int ): Unit ={
      println(i)
    }
    ints.foreach(printxxx)

    //输出方式三 : 匿名函数
    ints.foreach((i: Int)=>{println(i)})

    //输出方式四 : 匿名函数
    ints.foreach((i)=>{println(i)})

    //输出方式四 : 匿名函数
    ints.foreach({println(_)})
    //输出方式四 : 匿名函数
    ints.foreach(println(_))
    //输出方式四 : 匿名函数
    ints.foreach(println)

    //修改元素 内存地址不可变 内容可变
    ints.update(1,5)

    //增加元素 地址已经改变，成为了一个新的数组
    val ints1 : Array[Int] = ints:+(6)  //加后
    val ints2 : Array[Int] = (6)+:ints  //加前
    println(ints.mkString(","))
    println(ints == ints1) //false


    //---------------------------------可变数组：ArrayBuffer------
    val b_int: ArrayBuffer[Int] = ArrayBuffer(1,2,3,4)

    //增加元素
    val unit: Unit = b_int.insert(1,9)
    println(b_int) // 1,9,2,3,4
    //删除数据
    val i: Int = b_int.remove(1)
    println(i) // 9

    //  可变数组&不可变数组 之间得转换
    val array: Array[Int] = b_int.toArray
    val buffer: mutable.Buffer[Int] = ints.toBuffer
  }

}
