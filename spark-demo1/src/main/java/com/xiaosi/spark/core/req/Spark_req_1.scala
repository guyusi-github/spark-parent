package com.xiaosi.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 系统默认的累加器  ： 累计器：分布式只写共享变量
 */
object Spark_req_1 {

  def main(args: Array[String]): Unit = {
     //todo 累加器
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // 1.读取原始日志数据
    val actionRDD: RDD[String] = sc.textFile("spark-demo1/data/user_visit_action.txt")
    // 2，统计品类的点击数量：（品类ID,点击数量）
    val clickActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(6) != "-1"
      }
    )
    val clickCountRDD = clickActionRDD.map(
      action => {
        val datas = action.split("_")
        (datas(6), 1)
      }
    ).reduceByKey(_+_)
    // 3. 统计品类的下单数量： （平类ID，下单数量）
    val orderActionRDD = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(8) != "null"
      }
    )
    // 步骤3.1 :    orderid => 1,2,3
    // 步骤3.2 :    【(1,1)，(2,1)，(3,1)】
    val orderCountRDD = orderActionRDD.flatMap(
      action => {
        val datas: Array[String] = action.split("_")
        val cid: String = datas(8)
        val cids: Array[String] = cid.split(",")
        cids.map(id => {
          (id, 1)
        }
        )
      }
    ).reduceByKey(_ + _)
    // 4. 统计品类的支付数量 ： （品类ID , 支付数量）
    val payActionRDD = actionRDD.filter(
      action => {
        val datas = action.split("_")
        datas(10) != "null"
      }
    )
    // orderid => 1,2,3
    // 【(1,1)，(2,1)，(3,1)】
    val payCountRDD = payActionRDD.flatMap(
      action => {
        val datas = action.split("_")
        val cid = datas(10)
        val cids = cid.split(",")
        cids.map(id=>(id, 1))
      }
    ).reduceByKey(_+_)
    payCountRDD.collect.foreach(println)
    println("--------------------------------------------------------")
    // 5.将品类的支付数量： （品类ID，支付数量）
     //   点击数量排序 ，下单数量排序，支付数量排序
    //    元组排序：先比较第一个，再比较第二个，再比较第三个，依此类推
    //    （品类，（点击数量，下单数量，支付数量））
     val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickCountRDD.cogroup(orderCountRDD, payCountRDD)
      //做数据转换 (19,(CompactBuffer(6044),CompactBuffer(1722),CompactBuffer(1158))) -->  (19,(6044,1722,1158))
    val analysisRDD: RDD[(String, (Int, Int, Int))] = cogroupRDD.mapValues {
      case (clickIter, orderIter, payIter) => {
        var clickCnt = 0
        val iter1 = clickIter.iterator
        if (iter1.hasNext) {
          clickCnt = iter1.next()
        }
        var orderCnt = 0
        val iter2 = orderIter.iterator
        if (iter2.hasNext) {
          orderCnt = iter2.next()
        }
        var payCnt = 0
        val iter3 = payIter.iterator
        if (iter3.hasNext) {
          payCnt = iter3.next()
        }

        (clickCnt, orderCnt, payCnt) //返回类型
      }
    }
    val resultRDD: Array[(String, (Int, Int, Int))] = analysisRDD.sortBy(_._2, false).take(10)
    // 6. 将结果采集到控制台打印出来
    resultRDD.foreach(println)
    sc.stop()
  }
}
