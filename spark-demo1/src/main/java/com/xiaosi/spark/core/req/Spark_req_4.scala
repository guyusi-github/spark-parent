package com.xiaosi.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 分别统计每个品类点击的次数，下单的次数和支付的次数：方法四
 *
 *            方法四主要是因为前面的操作有suffer 会有数据倾斜 ，这里采用累加器进行操作
 */
object Spark_req_4 {

  def main(args: Array[String]): Unit = {
    //todo : Top10热门品类
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConfig)
    // 1.读取原始日志数据
    val actionRDD: RDD[String] = sc.textFile("spark-demo1/data/user_visit_action.txt")

    val acc = new HotCategoryAccumulator()
    sc.register(acc, "hotCategory")
    // 2. 将数据转换结构
    actionRDD.foreach(
      action=>{
        val datas = action.split("_")
        if (datas(6) != "-1") {
          // 点击的场合
          acc.add((datas(6), "click"))
        }else if (datas(8) != "null") {
          // 下单的场合
          val ids = datas(8).split(",")
          ids.foreach(
            id => {
              acc.add( (id, "order") )
            }
          )
        } else if (datas(10) != "null") {
          // 支付的场合
          val ids = datas(10).split(",")
          ids.foreach(
            id => {
              acc.add( (id, "pay") )
            }
          )
        }
      }
    )
    val accVal: mutable.Map[String, HotCategory] = acc.value
    // (id,(id,x,x,x)) => (id,x,x,x)
    val categories: mutable.Iterable[HotCategory] = accVal.map(_._2)

    val sort = categories.toList.sortWith(
      (left, right) => {
        if ( left.clickCnt > right.clickCnt ) {
          true
        } else if (left.clickCnt == right.clickCnt) {
          if ( left.orderCnt > right.orderCnt ) {
            true
          } else if (left.orderCnt == right.orderCnt) {
            left.payCnt > right.payCnt
          } else {
            false
          }
        } else {
          false
        }
      }
    )

    // 5. 将结果采集到控制台打印出来
    sort.take(10).foreach(println)
    sc.stop()



  }

  //构造数据结构
  case class HotCategory( cid:String, var clickCnt : Int, var orderCnt : Int, var payCnt : Int )
  /**
   * 自定义累加器
   * 1. 继承AccumulatorV2，定义泛型
   *    IN : ( 品类ID, 行为类型 )
   *    OUT : mutable.Map[String, HotCategory]
   * 2. 重写方法（6）
   */
  class HotCategoryAccumulator extends AccumulatorV2[(String,String),mutable.Map[String, HotCategory]]{
    //定义返回值类型
    private val hcMap = mutable.Map[String, HotCategory]()

    override def isZero: Boolean = {
      hcMap.isEmpty
    }

    override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = {
      new HotCategoryAccumulator();
    }

    override def reset(): Unit = {
      hcMap.clear()
    }

    override def add(v: (String, String)): Unit = {
      val cid = v._1
      val actionType = v._2
      val category = hcMap.getOrElse(cid, HotCategory(cid, 0, 0, 0))
      if(actionType == "click"){
        category.clickCnt += 1
      }else if (actionType == "order") {
        category.orderCnt += 1
      } else if (actionType == "pay") {
        category.payCnt += 1
      }
      //更新返回值
      hcMap.update(cid, category)
    }
    //每个分区的 excetor端都执行一遍和driver端合并
    override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
      val map1 = this.hcMap  //driver 端
      val map2 = other.value  //executor 端
      map2.foreach{
        case (cid ,hc) => {
          // 从driver端拿到实时的数据
          val category: HotCategory = map1.getOrElse(cid, HotCategory(cid, 0,0,0))
          //拿此时执行的这个分区也就是称为map2的数据去加到map也就是driver端
          category.clickCnt += hc.clickCnt
          category.orderCnt += hc.orderCnt
          category.payCnt += hc.payCnt
          map1.update(cid,category)
        }
      }
    }

    override def value: mutable.Map[String, HotCategory] = {
      hcMap
    }
  }
}
