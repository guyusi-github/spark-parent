package conversion

/**
 * 集合常用方法
 */
object List_Fun_Apply {
  def main(args: Array[String]): Unit = {
    // todo 集合常用方法
    val list = List(1, 2, 4, 3, 1, 3)

    println(list:::1::Nil)  //Nil空集合： list（）

    // todo 方法一
    //求和
    println("sum = " + list.sum)
    //最大值
    println("max = " + list.max)
    //最小值
    println("min = " + list.min)
    //乘积
    println("product = " + list.product) //依次相乘
    //反序
    println("reverse = " + list.reverse)


    // todo 方法二
    //分组 ---   groupBy --- (通过制定函数放回值进行分组)
    //1. 通过数字分组
    val intToInts: Map[Int, List[Int]] = list.groupBy { case x => x }
    for ((k, v) <- intToInts) {
      print(k + " - > " + v)
    }
    //2. 通过奇偶数分组
    val booleanToInts: Map[Boolean, List[Int]] = list.groupBy(x => x % 2 == 0)
    for (elem <- booleanToInts) {
      println(elem._1 + " === > " + elem._2)
    }

    // 3. 使用字符串首字母当分组标志
    val sortList: List[String] = List("11", "12", "22", "23")
    val stringToStrings: Map[String, List[String]] = sortList.groupBy(x => x.substring(0, 1))
    stringToStrings.foreach(x => {
      println(x._1 + " -->" + x._2)
    })

    // --- sortBy --- : 根据制定规制排序
    val sortList1: List[Int] = list.sortBy(x => x)
    println(sortList1.mkString(","))
    //
    val strings: List[String] = List("11", "23", "14", "22")
    val sortList2: List[String] = strings.sortBy(x => x.substring(1))
    println(sortList2.mkString(","))
    // --- sortWith --- : 根据制定规制排序
    val ints: List[Int] = list.sortWith((x, y) => {
      x < y //升序
    })
    print(ints.mkString(","))
    val sortList3: List[String] = strings.sortWith((left, right) => {
      left.substring(1).toInt > right.substring(1).toInt //根据内容的第一位进行排序
    })
    println(sortList3.mkString(","))

    //-- take -- 获得前几个
    val ints1: List[Int] = list.take(3)
    println(ints1.mkString(","))

    // todo 重点函数
    //--- map(映射) ---
    val mapList: List[(Int, Int)] = list.map(x => (x, 1))
    val intToInt: Map[Int, Int] = mapList.groupBy(x => x._1).map(x => (x._1, x._2.size))
    intToInt.foreach((x)=>{
      println((x._1,x._2))
    })

    // TODO WordCount
    // 对集合中的单词字符串进行统计出现的次数,并且按照出现次数降序排列,取前三名
    val wordList = List("Hello", "Scala", "Hello", "World", "Hbase", "Hadoop", "Kafka", "Scala", "World")
    // 1.将相同的单词放置在一起(分组)
    val wordToList: Map[String, List[String]] = wordList.groupBy(word => word)
    // 2.将数据结构进行转换
    val wordToCountMap: Map[String, Int] = wordToList.map(t => {
      (t._1, t._2.size)
    })
    // 3.将数据进行排序(降序)
    // map无序,需要转成list
    // List((k1,v1),(k2,v2))
    val sortWordList = wordToCountMap.toList.sortWith((left, right) => {
      left._2 > right._2
    })
    // 4.取排序完成后的前3条数据
    val resultList: List[(String, Int)] = sortWordList.take(3)
    println(resultList.mkString(","))

    // todo flagMap :扁平化操作
    val lineList = List("Hello World", "Hello Scala", "Hello Hadoop")
    val strings1: List[String] = lineList.flatMap(x => x.split(" "))
    val tuples: List[(Int, Int)] = sortList1.map(x => (x, 1)).groupBy(x => x._1).map(x => (x._1, x._2.size)).toList.sortWith((x, y) => {
      x._2 < y._2
    })
    println(tuples)

    //  todo reduce :  list内部两个元素依次依次计算
    // 数据减少,不是结果变少
    // 将集合的数据经过逻辑处理后只保留一个结果,具体的结果需要参考逻辑实现
    val list1 = List(1, 2, 3, 4)
    list1.reduce((x,y)=>{x-y})
    list1.reduce(_-_)  //和上面那个等价


    // todo fold ： 折叠
    val list2 = List(1, 2, 3, 4)
    //val i: Int = list.fold(100)(_ + _)// 110  外面元100素和list2的元素最压缩
    //val i: Int = list.fold(100)(_ - _) // 90
    //val i: Int = list.foldLeft(100)(_ - _) // 90
    val i1: List[Int] = list2.scanLeft(10)(_ - _) // List(10, 9, 7, 4, 0)
    println(i1)
  }
}
