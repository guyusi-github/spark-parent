package com.xiaosi.spark.core.acc

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 系统默认的累加器  ： 累计器：分布式只写共享变量
 */
object Spark_acc_1 {

  def main(args: Array[String]): Unit = {
     //todo 累加器
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    val RDD = sc.makeRDD(List(1, 2, 3, 4))
    val sum = sc.longAccumulator("sum")
    RDD.foreach(num=>{
      sum.add(num)
    })
    println(sum)
  }
}
