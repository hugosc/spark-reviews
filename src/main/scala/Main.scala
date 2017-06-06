import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import play.api.libs.json

object main {
    def topWords(arg: Array[String]) {

    }

    def topSentences(){

    }
    def main(arg: Array[String]) : Unit = {
        val sparkConf = (new SparkConf()).setAppName("SparkTweets").setMaster("local[4]")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("WARN")


        val raw = sc.textFile("data/dataset.json")
        println(raw.first)
        val counts = raw.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_+_)
        counts.countByKey()
        println(counts.first)


        sc.stop()
    }
}
