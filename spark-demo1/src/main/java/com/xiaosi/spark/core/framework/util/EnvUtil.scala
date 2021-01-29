package com.xiaosi.spark.core.framework.util

import org.apache.spark.SparkContext

object EnvUtil {
  //编写线程池 ： 供全局使用
  private val scLocal = new ThreadLocal[SparkContext]()
     def put(sc : SparkContext)={
       scLocal.set(sc)
     }
      def take() : SparkContext ={
        scLocal.get()
      }
    def clear() : Unit= {
      scLocal.remove()
  }

}
