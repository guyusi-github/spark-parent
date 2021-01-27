package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 案例实操：对数据操作
 * 1) 数据准备
 * agent.log：时间戳，省份，城市，用户，广告，中间字段使用空格分隔。
 * 2) 需求描述
 * 统计出每一个省份每个广告被点击数量排行的 Top3
 * 3) 需求分析 ：代码注释
 * 4) 功能实现  ： 代码实现
 */
object Spark1_RDD_Operator_Transform_xxpriticsxxx {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    //实操案例
    // 1. 获取原始数据： 时间戳,省份 ,城市 ， 用户， 广告
    val dataRDD: RDD[String] = sc.textFile("spark-demo1/data/agent.log")
    // 2. 将原始数据进行结构的转换，方便统计  ： （（省份，广告），1）
    val reduceRDD: RDD[((String, String), Int)] = dataRDD.map(line => {
      val datas = line.split(" ")
      ((datas(1), datas(4)), 1)

    })
    //3 .将转换后的数据进行分组聚合        ： （（省份，广告），1）=》 （（省份，广告），sum）
    val reduceByKeyRDD: RDD[((String, String), Int)] = reduceRDD.reduceByKey(_ + _)
    // 4. 将聚合的结果进行结构的转换        ：（（省份，广告），sum） =》 （省份，（广告，sum））
    val newMapRDD: RDD[(String, (String, Int))] = reduceByKeyRDD.map {
      case ((pro, ad), sum) => {
        (pro, (ad, sum))
      }
    }
    // 5. 将省份进行分组                 ： （省份，（广告，sum）） （省份，（广告，sum））
    val groupByKeyRDD: RDD[(String, Iterable[(String, Int)])] = newMapRDD.groupByKey()
    // 6.将分组后的数据组内进行排序（降序），取前三名
    val result: RDD[(String, List[(String, Int)])] = groupByKeyRDD.mapValues(
      iter => {
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
      }
    )
    result.collect().foreach(println)

  }
}
