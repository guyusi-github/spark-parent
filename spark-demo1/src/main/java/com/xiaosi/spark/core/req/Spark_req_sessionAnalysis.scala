package com.xiaosi.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 *  Top10 热门品类中每个品类的 Top10 活跃 Session 统计
 */
object Spark_req_sessionAnalysis {

  def main(args: Array[String]): Unit = {
    //todo 待改善
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // 1.读取原始日志数据
    val actionRDD: RDD[String] = sc.textFile("spark-demo1/data/user_visit_action.txt")
    actionRDD.cache() //会重复使用数据，先加入缓存

    // Q : 存在大量的shuffle操作（reduceByKey）
    // reduceByKey 聚合算子，spark会提供优化，缓存
    // 2. 将数据转换结构
    //    点击的场合 : ( 品类ID，( 1, 0, 0 ) )
    //    下单的场合 : ( 品类ID，( 0, 1, 0 ) )
    //    支付的场合 : ( 品类ID，( 0, 0, 1 ) )
    val flatRDD: RDD[(String, (Int, Int, Int))] = actionRDD.flatMap(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          // 点击的场合
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          // 下单的场合
          val ids = datas(8).split(",")
          ids.map(id => (id, (0, 1, 0)))
        } else if (datas(10) != "null") {
          // 支付的场合
          val ids = datas(10).split(",")
          ids.map(id => (id, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )

    // 3. 将相同的品类ID的数据进行分组聚合
    //    ( 品类ID，( 点击数量, 下单数量, 支付数量 ) )
    val analysisRDD = flatRDD.reduceByKey(
      (t1, t2) => {
        ( t1._1+t2._1, t1._2 + t2._2, t1._3 + t2._3 )
      }
    )

    // 4. 将统计结果根据数量进行降序处理，取前10名
    val resultRDD = analysisRDD.sortBy(_._2, false).take(10)

    // 5. 将结果采集到控制台打印出来
    resultRDD.foreach(println)

    sc.stop()
  }
}
