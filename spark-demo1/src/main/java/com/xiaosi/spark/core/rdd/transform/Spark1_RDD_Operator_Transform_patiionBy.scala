package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_Operator_Transform_patiionBy {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    val RDD: RDD[Any] = sc.makeRDD(List(List(1, 2), 3, List(4, 5)))
    val flagMapRDD: RDD[Any] = RDD.flatMap(
      data => {
        data match {
          case list: List[_] => list
          case dat => List(dat)
        }
      })

    flagMapRDD.collect().foreach(println)

  }
}
