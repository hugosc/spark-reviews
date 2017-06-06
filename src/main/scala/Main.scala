import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import play.api.libs.json

object main {
    def main(arg: Array[String]) : Unit = {
        val sparkConf = (new SparkConf()).setAppName("SparkTweets").setMaster("local[4]")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("WARN")


        val raw = sc.textFile("data/dataset.json")
        println(raw.first)

        sc.stop()
    }
}
