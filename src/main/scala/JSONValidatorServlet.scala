import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonschema.core.report.ProcessingReport
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
      Created(Map(
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



  post("/validate/:id") {
    val schema = dataStore.getSchema(params("id"))
    if (schema.isEmpty) {
      NotFound(
        Map(
          "action" -> "validateSchema",
          "error_code" -> 404,
          "reason" -> "schema not found"
        )
      )
    } else {
      val mapper = new ObjectMapper
      mapper.setSerializationInclusion(Include.NON_NULL)

      try {
        val json = mapper.readTree(request.body)
        val report: ProcessingReport = schema.get.validate(json)
        if (report.isSuccess) {
          Accepted(Map(
            "action" -> "validateDocument",
            "id" -> params("id"),
            "status" -> "success"
          ))
        } else {
          UnprocessableEntity(Map(
            "action"-> "validateDocument",
            "id"-> params("id"),
            "status"-> "error",
            "message"-> report.toString
          ))
        }
      } catch {
        case e: JsonParseException => BadRequest (Map(
            "action"-> "validateDocument",
            "id"-> params("id"),
            "status"-> "error",
            "message"-> "Invalid JSON"
        ))
      }
    }
  }
}
