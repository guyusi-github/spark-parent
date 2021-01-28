package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_Operator_Transform_cogroup {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)

    val dataRDD1 = sc.makeRDD(List(("a",1),("a",2),("c",3)))
    val dataRDD2 = sc.makeRDD(List(("a",1),("c",2),("c",3)))
    val RDD: RDD[(String, (Iterable[Int], Iterable[Int]))] = dataRDD1.cogroup(dataRDD2)
    RDD.collect.foreach(println)
    /*
      (a,(CompactBuffer(1, 2),CompactBuffer(1)))
      (c,(CompactBuffer(3),CompactBuffer(2, 3)))
     */
  }
}
