package conversion

/**
 *  隐式函数： 其他类的类型转换，丰富类库功能
 */
object Demo2 {
  def main(args: Array[String]): Unit = {
    implicit def addDelete(mysql:MySql): DB = {
      new DB
    }
    val mysql = new MySql
    mysql.insert()
    mysql.delete()   //mysql 也能调用DB的方法
  }


  class MySql{
    def insert(): Unit ={
      println("插入数据~~~")
    }
  }

  class DB{
    def delete(): Unit ={
      println("删除数据~~~")
    }
  }
}
