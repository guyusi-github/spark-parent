package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_Operator_Transform_par {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    val RDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4) , 2)
    //分区操作
    val mapRDD = RDD.mapPartitions( //对每个分区单独操作
      iter => {
        iter.map(_ * 2) //iter 代表每个分区
      }
    )
    mapRDD.collect().foreach(println)

  }
}
