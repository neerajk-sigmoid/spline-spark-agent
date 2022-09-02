/*
 * Copyright 2022 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.spline.example.batch

import org.apache.spark.sql.functions.col
import za.co.absa.spline.SparkApp

object ExampleRDDJob extends SparkApp("Example 1 (successful)") {

  import org.apache.spark.sql._
  import za.co.absa.spline.harvester.SparkLineageInitializer._

  // Initializing library to hook up to Apache Spark
  spark.enableLineageTracking()

  // A business logic of a spark job ...

  val rddData = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .parquet("data/output/batch/union_job_results")
    .select(col("_1").cast("int"))
    .rdd

  val rddRes = rddData.map(row => row.getInt(0) * 2)

  rddRes
    .toDF("foo")
    .write
    .mode(SaveMode.Append)
    .parquet("data/output/batch/jobrdd_results")
}
