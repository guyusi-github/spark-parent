package conversion

import scala.util.control.Breaks

/**
 *  Scala 函数式编程高级 : 控制抽象
 *
 *
 */
object Demo5 {
  def main(args: Array[String]): Unit = {

    // 案例一：
      def test(f: => Unit): Unit={
         f    // 输入啥返回啥
      }
      //调用
      test{
        println("xxxxx")
      }

    //案例二 （应用）：
    //说明
    //1. myRunInThread 是一个高阶函数
    //2. 接收的函数式 () => Unit 即 没有参数，没有返回值
    def myRunInThread(f1: () => Unit): Unit = {
      new Thread {
        //重写了 Thread 的 run 方法,这里调用了f1函数.
        override def run(): Unit = {
          f1()
        }
      }.start()
    }

    //说明
    //1.myRunInThread {} 本身是 myRunInThread() 为了习惯 写成了 myRunInThread {}
    //2.() => 后面的三句话是一个整体，你也可以使用{} 括起来.
    //3.() => xxxx 就是一个匿名函数，传递给 f1
    myRunInThread {
      () => println("干活咯！5秒完成...")   //（） 为原函数参数
        Thread.sleep(5000)
        println("干完咯！")
    }


    //案例三
    //说明
    //1. 只要condition 为真，就不在执行 block代码
    def until(condition: => Boolean)(block: => Unit) {//类似while循环，递归
      if (!condition) {
        block // block就是【x -= 1 println(x)】，会导致x的减小
        //这里传入的始终是 () => {x == 0} 的匿名函数，但是x是变化,因此总会x==0成立的
        until(condition)(block)
      }
    }

    var x = 10
    until(x == 0) {
      x -= 1
      println(x)
    }

  }


}
