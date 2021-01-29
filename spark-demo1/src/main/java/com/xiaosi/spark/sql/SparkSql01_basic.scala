package com.xiaosi.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 *  rdd dataframe dataset 三者两两转换
 */
object SparkSql01_basic {
  def main(args: Array[String]): Unit = {
    //todo 累加器
    System.setProperty("hadoop.home.dir", "F:\\spark\\资料\\2.资料\\WindowsDep\\hadoop-3.0.0")
    // todo 准备环境
    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("Sql")
    val spark = SparkSession.builder().config(sparkConfig).getOrCreate()
    import spark.implicits._
    //todo dataframe
    val df: DataFrame = spark.read.json("spark-demo1/data/User.json")
    println(df)

    //DataFare => Sql
    df.createOrReplaceTempView("user")
    spark.sql("select * from user").show
    spark.sql("select name, id from user").show
    spark.sql("select avg(id) from user").show

    // DataFrame => DSL
    //在使用Dataframe时，如果涉及到转换操作，需要引入转换规则
    df.select("id","name").show
    df.select($"id" + 1).show   //这里用到隐式转换  import spark.implicits._
    df.select('id + 1).show  // +1 操作

    // todo DataSet
    // DataFrame 其实就是特定的泛型dataset
    val seq = Seq(1,2,3,4)
    val ds : Dataset[Int] =  seq.toDS()
    ds.show()

    // todo RDD <=> DataFrame
    val df1 = spark.sparkContext.makeRDD(List( (1, "zhangsan", 10), (2, "lisi", 30), (3, "wangwu", 40)))
    val frame : DataFrame = df1.toDF("id", "name", "age")
    val rdd: RDD[Row] = frame.rdd

    //todo DataFrame <=> DataSet
    val uds: Dataset[User] = frame.as[User]
    val frame1 = ds.toDF()
    println(frame1)

    //todo  比较常用   DataFrame <=> DataSet
    val ds2: Dataset[User] = df1.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }.toDS()
    val userRDD: RDD[User] = ds2.rdd

    // TODO 关闭环境
    spark.close()
  }
  case class User( id:Int, name:String, age:Int )
}
