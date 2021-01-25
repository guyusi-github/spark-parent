package conversion

/**
 * 集合常用方法
 */
object List_Fun_Apply1 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)
    // filter 过滤
    val ints: List[Int] = list.filter(x => {
      x % 2 == 0
    })
    println(ints)

    // zip 拉链
    val list1 = List(1, 2, 3, 7)
    val list2 = List(3, 4, 5, 6)
    val tuples: List[(Int, Int)] = list1.zip(list2)
    println(tuples)

    // 集合并集 union
    val unionList: List[Int] = list1.union(list2)
    println(unionList)

    // 集合交集  intersect
    val intersectList: List[Int] = list1.intersect(list2)
    println(intersectList)

    // 集合差集  diff
    val diffList: List[Int] = list1.diff(list2)
    println(diffList)
  }
}
