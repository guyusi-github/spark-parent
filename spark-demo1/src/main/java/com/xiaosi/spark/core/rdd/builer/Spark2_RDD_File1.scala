package com.xiaosi.spark.core.rdd.builer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark2_RDD_File1 {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // todo 创建RDD
    // 以文件为单位创建RDD
    val rdd = sc.wholeTextFiles("spark-demo1/data/1.txt")

    rdd.collect().foreach(println)
    //关闭环境
    sc.stop()
  }

}
