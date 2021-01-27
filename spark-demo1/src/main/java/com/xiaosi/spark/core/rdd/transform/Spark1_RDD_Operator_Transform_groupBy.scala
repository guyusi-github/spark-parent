package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_Operator_Transform_groupBy {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)

    val RDD: RDD[String] = sc.makeRDD(List("hello","spark","scala","hadoop"),2)
    // todo groupBy : 分组
    val groupByRDD: RDD[(Char,Iterable[String])] = RDD.groupBy(_.charAt(1))
    groupByRDD.collect().foreach(data => {println})

  }
}
