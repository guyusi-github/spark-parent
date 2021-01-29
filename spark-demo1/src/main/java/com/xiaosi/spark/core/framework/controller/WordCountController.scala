package com.xiaosi.spark.core.framework.controller

import com.xiaosi.spark.core.framework.common.TController
import com.xiaosi.spark.core.framework.service.WordCountService


class WordCountController extends TController{

  private val wordCountService = new WordCountService()

  override def dispatch(): Unit = {
    // 业务操作
    val array = wordCountService.dataAnalysis()
    array.foreach(println)
  }
}
