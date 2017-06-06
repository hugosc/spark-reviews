import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import play.api.libs.json._
import org.apache.spark.rdd._
import spark_rake._
import review._
import java.time._


object main {
    def topWords(arg: RDD[Review]) {
        val counts = arg.flatMap(review => review.text.split(" ")).map(word => (word.toLowerCase, 1)).reduceByKey(_+_)
        val sorted = counts.sortBy(_._2, false)
        sorted.take(50) foreach {
            tup => println(tup)
        }
    }

    def topSentences(arg: RDD[Review]){
        val pontuation = Array('.','!','?',';')

        val counts = arg.flatMap(review => review.text.split(pontuation))
                                .map(sentence => (sentence.stripPrefix(" ").toLowerCase, 1))
                                .reduceByKey(_+_)
        val sorted = counts.sortBy(_._2, false)
        val filtered = sorted.filter(_._1.length > 3)
        filtered.take(50) foreach {
            tup => println(tup)
        }
    }

    def temporalDist(arg: RDD[Review]){
        val adjusted = arg.map(review => (review.createdAt.toEpochDay(), 1))
        val result = adjusted.countByKey()
        val sortedKeys = result.keys.toArray.sortBy(x=>x)
        sortedKeys.foreach(k =>
            println(LocalDate.ofEpochDay(k) + "->" + result(k))
          )
    }
    def main(arg: Array[String]) : Unit = {

        val stopWords = SparkRake.stopWords
        println(stopWords)
        
        val sparkConf = (new SparkConf()).setAppName("SparkTweets").setMaster("local[4]")
        val sc = new SparkContext(sparkConf)
        sc.setLogLevel("WARN")


        val raw = sc.textFile("data/dataset.json")
        val reviews = raw map (r => Json.parse(r).as[Review])
        //topWords(reviews)
        //topSentences(reviews)
        temporalDist(reviews)
        sc.stop()
    }
}
