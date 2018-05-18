import com.fasterxml.jackson.core.JsonParseException
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
      NotFound(Map(
        "action" -> "readSchema",
        "error_code" -> 404,
        "reason" -> "invalid id"
      ))
    } else {
      Ok(schema.get)
    }
  }

  post("/schema/:id") {
    try{
      dataStore.set(params("id"), request.body)
      Ok(Map(
        "action" -> "uploadSchema",
        "id" -> params("id"),
        "status"-> "success"
      ))
    } catch {
      case e: JsonParseException => BadRequest(Map(
          "action" -> "uploadSchema",
          "id" -> params("id"),
          "status"-> "error",
          "message"-> "Invalid JSON"
      ))
    }

  }
}
