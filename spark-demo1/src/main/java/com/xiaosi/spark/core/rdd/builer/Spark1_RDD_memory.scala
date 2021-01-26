package com.xiaosi.spark.core.rdd.builer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_memory {
  def main(args: Array[String]): Unit = {
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // todo 创建RDD
    val seq = Seq[Int](1, 2, 3)
    //parallelize ： 并行
    //val rdd: RDD[Int] = sc.parallelize(seq)  //方式一
    val rdd: RDD[Int] = sc.makeRDD(seq)     //方式二
    rdd.collect().foreach(println)
    //关闭环境
    sc.stop()
  }

}
