package com.xiaosi.spark.core.framework.aplication

import com.xiaosi.spark.core.framework.common.TApplication
import com.xiaosi.spark.core.framework.controller.WordCountController
import org.apache.spark.{SparkConf, SparkContext}

object WordCountApplication extends App with TApplication{  //继承可直接运行

  start(){
    val controller = new WordCountController()
    controller.dispatch()
  }

}
