import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import play.api.libs.json._

import review._


object main {
    def main(arg: Array[String]) : Unit = {

        val sparkConf = (new SparkConf()).setAppName("SparkTweets").setMaster("local[4]")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("WARN")


        val raw = sc.textFile("data/dataset.json")

        val reviews = raw map (r => Json.parse(r).as[Review])

        sc.stop()
    }
}
