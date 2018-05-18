import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class JSONValidatorServlet(implicit dataStore: JSONSchemaStore) extends ScalatraServlet with NativeJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats.withBigDecimal;

  before(){
    contentType = formats("json")
  }

  get("/schema/:id") {
    var schema = dataStore.getSchemaText(params("id"))
    if (schema.isEmpty){
      try {
        NotFound(Map(
          "action" -> "readSchema",
          "error_code" -> 404,
          "reason" -> "invalid id"
        ))
      } catch {
        case e: Exception => e.printStackTrace()
      }
    } else {
      Ok(schema.get)
    }
  }

}
