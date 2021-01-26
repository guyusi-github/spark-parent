package com.xiaosi.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark1_RDD_Operator_Transform_test {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    def mapFunction(num:Int):Int={
      num *2
    }

    val value = sc.makeRDD(List(1, 2, 3, 4))
    val value1 = value.map(mapFunction)
    value1.collect().foreach(println)

  }
}
