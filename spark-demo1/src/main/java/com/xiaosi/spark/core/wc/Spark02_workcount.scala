package com.xiaosi.spark.core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_workcount {
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
    //4.对分组后进行聚合
    val wordTogroup: RDD[(String, Iterable[(String,Int)])] = wordToOne.groupBy(t=>t._1)
    //5.将分组后的进行打印出来
   val wordToCount =  wordTogroup.map{
     case (word , list )=> {
      list.reduce(
       (t1,t2)=>{
         (t1._1,t1._2+t2._2)
       }
      )
    }}
    val array= wordToCount.collect()
    array.foreach(println)
   //关闭连接
    sc.stop()
  }
}
