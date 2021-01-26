package com.xiaosi.spark.core.rdd.builer

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 文件分区
 *
 */
object Spark2_RDD_File_par {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // todo 创建RDD
    val rdd = sc.textFile("spark-demo1/data/1.txt", 3)  //默认分区为2
    rdd.saveAsTextFile("spark-demo1/file-par1")
    //关闭环境
    sc.stop()
  }

}
