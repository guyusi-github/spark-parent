package com.xiaosi.spark.core.framework.common

import com.xiaosi.spark.core.framework.bean.ddd
import com.xiaosi.spark.core.framework.util.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

trait TApplication {

  def start(master : String = "local[*]",app:String = "Application")(op : => Unit) : Unit = {  //控制抽象（对操作进行设置）
    val sparConf = new SparkConf().setMaster(master).setAppName(app)
    val sc = new SparkContext(sparConf)
    EnvUtil.put(sc)
    try {
      op
    } catch {
      case ex => println(ex.getMessage)
    }
    // TODO 关闭连接
    sc.stop()
    EnvUtil.clear()

  }
}
