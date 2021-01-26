package com.xiaosi.spark.core.rdd.builer

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 分区数据如何存放
 */
object Spark1_RDD_memory_par1 {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")

    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
   // sparkConfig.set("spark.default.parallelism","5") //表明这个核数是5
    val sc = new SparkContext(sparkConfig)
    // todo 创建RDD
    val rdd = sc.makeRDD(    //第二个参数是分区数量 ,分区数量没有默认为和电脑核数一样
      List(1,2,3,4),2
    )
    rdd.saveAsTextFile("spark-demo1/output")
    //关闭环境
    sc.stop()
  }

}
