package com.xiaosi.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 分别统计每个品类点击的次数，下单的次数和支付的次数： 方法二
 */
object Spark_req_2 {

  def main(args: Array[String]): Unit = {
     //todo 待改善
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // 1.读取原始日志数据
    val actionRDD: RDD[String] = sc.textFile("spark-demo1/data/user_visit_action.txt")
    actionRDD.cache() //会重复使用数据，先加入缓存

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

    // (品类ID, 点击数量) => (品类ID, (点击数量, 0, 0))
    // (品类ID, 下单数量) => (品类ID, (0, 下单数量, 0))
    //                    => (品类ID, (点击数量, 下单数量, 0))
    // (品类ID, 支付数量) => (品类ID, (0, 0, 支付数量))
    //                    => (品类ID, (点击数量, 下单数量, 支付数量))
    // ( 品类ID, ( 点击数量, 下单数量, 支付数量 ) )
     val rdd1 = clickCountRDD.map{
       case ( cid, cnt ) => {
         (cid, (cnt, 0, 0))
       }
     }
    val rdd2 = orderCountRDD.map{
      case ( cid, cnt ) => {
        (cid, (0, cnt, 0))
      }
    }
    val rdd3 = payCountRDD.map{
      case ( cid, cnt ) => {
        (cid, (0, 0, cnt))
      }
    }
    // 将三个数据源合并在一起，同意进行聚合计算
    val sourceRDD: RDD[(String, (Int, Int, Int))] = rdd1.union(rdd2).union(rdd3)
    // 6. 将结果采集到控制台打印出来
    val analysisRDD = sourceRDD.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )
    val resultRDD = analysisRDD.sortBy(_._2, false).take(10)
    resultRDD.foreach(println)
    sc.stop()
  }
}
