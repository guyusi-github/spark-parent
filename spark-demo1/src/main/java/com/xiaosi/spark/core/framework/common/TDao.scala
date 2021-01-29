package com.xiaosi.spark.core.framework.common

import com.xiaosi.spark.core.framework.util.EnvUtil
import jdk.internal.util.EnvUtils

trait TDao {
    def readFile(path : String) = {
      EnvUtil.take().textFile(path)
    }
}
