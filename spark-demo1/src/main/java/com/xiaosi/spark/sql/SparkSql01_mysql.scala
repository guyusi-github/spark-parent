package com.xiaosi.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkSql01_mysql {
  def main(args: Array[String]): Unit = {
    //todo 累加器
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("Sql")
    val spark = SparkSession.builder().config(sparkConfig).getOrCreate()
    //引入隐式转换
    import spark.implicits._
    //读取sql
    val df = spark.read.format("jdbc")
      .option("url","jdbc:mysql://172.16.10.254:3306/test?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "secxun")
      .option("password", "secxun@2019")
      .option("dbtable", "base_attr_info")
      .load()
    df.show()
    // 保存数据
    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://linux1:3306/spark-sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "123123")
      .option("dbtable", "base_attr_info_history")
      .mode(SaveMode.Append)  //  可选参数 ：Append,Overwrite,ErrorIfExists,Ignore;
      .save()                  //           追加     重写        存在就异常    忽略


    // TODO 关闭环境
    spark.close()
  }


}
