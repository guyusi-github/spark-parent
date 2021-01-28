package com.xiaosi.spark.core.acc

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 广播变量的学习
 */
object Spark_acc_3 {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    val rdd1 = sc.makeRDD(List(
      ("a", 1),("b", 2),("c", 3)
    ))
    val map = mutable.Map(("a", 4),("b", 5),("c", 6))
    //封装广播变量
    val bc: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map)
    rdd1.map{
      case (w ,c) =>{
        //方法广播变量
        val l : Int = bc.value.getOrElse(w,0)
        (w,(c,l))
      }
    }.collect().foreach(println)
    sc.stop()
  }

}
