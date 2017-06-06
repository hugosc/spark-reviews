import play.api.libs.json._ // JSON library
import play.api.libs.functional.syntax._ // Combinator syntax
import java.text.NumberFormat
import java.util.Locale
import java.time.LocalDate
import java.time.format._

package object review {

    def myParseLocalDate(s:String) : LocalDate = {
        LocalDate.parse(s, DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
    }

    def myParseInt(s:String) : Int = {
        NumberFormat.getNumberInstance(Locale.US).parse(s).intValue
    }

    case class Author(
        memberSince: String,
        reviews: Int,
        ratings: Int,
        postForum: Int,
        helpfulVotes: Int,
        level: Int
    )

    case class Review(
    	id: String,
        title: String,
        bubbleCount: Int,
        createdAt: LocalDate,
        text: String,
        query: String,
        author: Author,
        collectedAt: String
    )


    implicit val authorReads: Reads[Author] = new Reads[Author] {
        override def reads(json: JsValue): JsResult[Author] = {
            for {
                memberSinceAsString <- (json \ "memberSince").validate[String]
            } yield {
                val reviewsValue = (json \ "reviews")
                val ratingsValue = (json \ "ratings")
                val postForumValue = (json \ "postForum")
                val helpfulVotesValue = (json \ "helpfulVotes")
                val levelValue = (json \ "level")

                val reviewsAsInt = reviewsValue.asOpt[Int].orElse(
                    reviewsValue.asOpt[String] map (myParseInt(_))
                ).get
                val ratingsAsInt = ratingsValue.asOpt[Int].orElse(
                    ratingsValue.asOpt[String] map (myParseInt(_))
                ).get
                val postForumAsInt = postForumValue.asOpt[Int].orElse(
                    postForumValue.asOpt[String] map (myParseInt(_))
                ).get
                val helpfulVotesAsInt = helpfulVotesValue.asOpt[Int].orElse(
                    helpfulVotesValue.asOpt[String] map (myParseInt(_))
                ).get
                val levelAsInt = levelValue.asOpt[Int].orElse(
                    levelValue.asOpt[String] map (myParseInt(_))
                ).get
                Author(
                    memberSinceAsString,
                    reviewsAsInt,
                    ratingsAsInt,
                    postForumAsInt,
                    helpfulVotesAsInt,
                    levelAsInt
                )
            }
        }
    }

    implicit val reviewReads: Reads[Review] = (
        (JsPath \\ "$oid").read[String] and
        (JsPath \ "title").read[String] and
        (JsPath \ "bubbleCount").read[Int] and
        ((JsPath \ "createdAt").read[String].map (s=>myParseLocalDate(s))) and
        ((JsPath \ "text").readNullable[String].map( _ match {
            case Some(s) => s
            case None => ""}
        )) and
        (JsPath \ "query").read[String] and
        (JsPath \ "author").read[Author] and
        (JsPath \\ "$date").read[String]
    )(Review.apply _)

}
/*
{
	"_id":
	{
		"$oid":"5921cdae4b679c4646a52f57"
	},
	"title":"Must do even it was so busy",
	"bubbleCount":50,
	"createdAt":"May 20, 2017",
	"text":"This is the most busiest atttaction in Paris and there are some nice restaurants on it and the views were spectacular and this is the top must do in Paris",
	"query":"Eiffel_Tower",
	"author":
	{
		"memberSince":"Since this month",
		"reviews":"2",
		"ratings":0,
		"postForum":0,
		"helpfulVotes":0,
		"level":0
	},
	"collectedAt":
	{
		"$date":"2017-05-21T17:26:06.452Z"
	}
}
*/
