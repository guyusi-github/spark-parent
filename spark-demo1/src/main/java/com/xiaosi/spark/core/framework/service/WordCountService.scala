package com.xiaosi.spark.core.framework.service

import com.xiaosi.spark.core.framework.common.TService
import com.xiaosi.spark.core.framework.dao.WordCountDao
import org.apache.spark.rdd.RDD
//服务层
class WordCountService  extends TService{

  private val wordCountDao = new WordCountDao()

  override def dataAnalysis() =  {
    val lines = wordCountDao.readFile("spark-demo1/data/1.txt")
    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordToOne = words.map(word=>(word,1))
    val wordToSum: RDD[(String, Int)] = wordToOne.reduceByKey(_+_)
    val array: Array[(String, Int)] = wordToSum.collect()
      array
  }
}
