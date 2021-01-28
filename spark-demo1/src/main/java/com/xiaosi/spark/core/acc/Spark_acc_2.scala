package com.xiaosi.spark.core.acc

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 自定义的累加器  ： 累计器：分布式只写共享变量
 */
object Spark_acc_2 {

  def main(args: Array[String]): Unit = {
     //todo 累加器
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    val rdd = sc.makeRDD(List("hello", "spark", "hello"))
    // 累加器 : WordCount
    // 创建累加器对象
    val wcAcc = new MyAccumulator()
    // 向Spark进行注册
    sc.register(wcAcc,"wordCountAcc")
    rdd.foreach(
      word => {
        // 数据的累加（使用累加器）
        wcAcc.add(word)
      }
    )

    // 获取累加器累加的结果
    println(wcAcc.value)
    sc.stop()
  }

  /*
    自定义数据累加器：WordCount

    1. 继承AccumulatorV2, 定义泛型
       IN : 累加器输入的数据类型 String
       OUT : 累加器返回的数据类型 mutable.Map[String, Long]

    2. 重写方法（6）
   */
   class MyAccumulator extends AccumulatorV2[String, mutable.Map[String ,Long]]{

    private var wcMap = mutable.Map[String,Long]()

    // 判断是否为初始状态
    override def isZero: Boolean = {
      wcMap.isEmpty
    }
    //复制一个用户返回driver端
    override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
      new MyAccumulator()
    }
    // 重置
    override def reset(): Unit = {
      wcMap.clear()
    }
     // 在excutor 端的操作逻辑
    override def add(word: String): Unit = {
      val newCnt: Long = wcMap.getOrElse(word, 0L) + 1  //得到一个新的单词数量累家
      wcMap.update(word,newCnt)
    }
     //// Driver合并多个累加器 ，用executor 端计算返回来的值去更新driver端   other 是各个excuter中的变量的
    override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
      val map1: mutable.Map[String, Long] = this.wcMap
      val map2: mutable.Map[String, Long] = other.value

      map2.foreach{
        case (word ,count) => {
          val newCount = map1.getOrElse(word,0L) + count
          map1.update(word,newCount)
        }
      }

    }
    // 累加器结果
    override def value: mutable.Map[String, Long] =  {
      wcMap
    }
  }

}
