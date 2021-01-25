package conversion

/**
 *  Scala 函数式编程高级 : 偏函数
 *
 *   --------------------本节重点 -》 使用花括号-》 结合使用模式匹配
 */
object Demo4 {
  def main(args: Array[String]): Unit = {
         //给你一个集合val list = List(1, 2, 3, 4, "abc") ，请完成如下要求:
       //将集合list中的所有数字+1，并返回一个新的集合
        //要求忽略掉 非数字 的元素，即返回的 新的集合 形式为 (2, 3, 4, 5)
       val list = List(1, 2, 3, 4, "abc")


    //1. 模式匹配
    def addOne1(i : Any): Any = {
     i match {
       case x : Int => x+1
       case _ =>
     }
    }

    val list1: List[Any] = list.map(addOne1)
    println(list1)

    //偏函数解决
    /**
     * 基本介绍
     *
     * 在对符合某个条件，而不是所有情况进行逻辑操作时，使用偏函数是一个不错的选择
     * 将包在大括号内的一组case语句封装为函数，我们称之为偏函数，它只对会作用于指定类型的参数或指定范围值的参数实施计算，超出范围的值会忽略.
     * 偏函数在Scala中是一个特质PartialFunction
     */
    //说明
    val addOne3= new PartialFunction[Any, Int] {
      def isDefinedAt(any: Any) = {
        if (any.isInstanceOf[Int]) true
        else false
      }
      def apply(any: Any) = any.asInstanceOf[Int] + 1
    }
    val list3 = list.collect(addOne3)
    println("list3=" + list3)

    //偏函数简写(对部分数据操作采取偏函数)
    val ints = list.collect { case x: Int => x + 1 }
    println(ints)
    //对全部数据用map（映射）
      val string : List[String] =  list.map{
      case num => num + "xxxx"
    }
    println(string)

    //控制逻辑




  }

 /*
 *  隐式类： 直接类型里引用
 */
object Demo4 {
  def main(args: Array[String]): Unit = {

    implicit class Person( u : User){
      def delete(): Unit ={
        println("删除")
      }
    }

    class User(){
      def insert(): Unit ={
        print("删除")
      }
    }

    val user: User = new User()
    user.delete()

  }}
}
