package conversion

/**
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

  }
}
