package com.xiaosi.spark.core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_workcount {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
     //application
    ///spark 框架
    // todo 建立spark框架的连接
    val sparkConf = new SparkConf().setMaster("local").setAppName("Wordcount")
    val sc = new SparkContext(sparkConf)
    //
    //1.读取文件 ，获取一行一行的单词
    val lines: RDD[String] = sc.textFile("E:\\spark-parent\\spark-demo1\\src\\main\\resources")

    //2.将单词分成一个个
    val words : RDD[String]= lines.flatMap(_.split(" "))

    //3.将单词进行机构化操作，统计方便
     val wordToOne : RDD[(String ,Int)] = words.map(word => (word, 1))
    val value: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
    val array= value.collect()
    array.foreach(println)
   //关闭连接
    sc.stop()
  }
}
