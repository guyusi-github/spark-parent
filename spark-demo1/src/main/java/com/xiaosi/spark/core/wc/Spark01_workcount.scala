package com.xiaosi.spark.core.wc


import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_workcount {
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
    //3.将数据根据进行拆分分组
    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(word => word)
    //4.对分组后进行聚合
    var wordToCount =  wordGroup.map{
      case (word, list) => {
        (word,list.size)
      }
    }
    //5.将分组后的进行打印出来
    val array: Array[(String,Int)] = wordToCount.collect()
    array.foreach(println)
   //关闭连接
    sc.stop()

  }
}
