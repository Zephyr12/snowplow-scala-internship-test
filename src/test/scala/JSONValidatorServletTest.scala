import org.junit.Before
import org.scalatest.FunSuiteLike
import org.scalatest.BeforeAndAfter
import org.scalatra.test.scalatest._;

class JSONValidatorServletTest extends ScalatraFunSuite with BeforeAndAfter {

  implicit var dataStore : JSONSchemaStore = new JSONSchemaStore(new KeyValueStore {
      var mapping = Map(
        "example2" ->
          """
            |{
            |"title": "Person",
            |"type": "object",
            |"properties": {
            |"firstName": {
            |"type": "string"
            |},
            |"age": {
            |"description": "Age in years",
            |"type": "integer",
            |"minimum": 0
            |}
            |},
            |"required": ["firstName"]
            |}
          """.stripMargin)


      override def get(key: String): Option[String] =
        mapping.get(key)

      override def set(key: String, value: String): Unit =
        mapping = mapping.updated(key, value)
    })

  addServlet(new JSONValidatorServlet, "/*")


  test("GET /schema/ with unknown id returns 404") {
    get("/schema/unknown") {
      assert(status === 404)
      assert(body.contains("invalid id"))
    }
  }

  test("GET /schema/ with known id returns valid response") {
    get("/schema/example2") {
      status should equal(200)
      body should equal(dataStore.getSchemaText("example2").get)
    }
  }

  test("POST /schema/ with valid data returns success") {
    post("/schema/example3", body=dataStore.getSchemaText("example2").get) {
      status should equal(200)
      body should include("success")
      body should include("example3")
      assert(dataStore.getSchemaText("example3").isDefined)
    }
  }

  test("POST /schema/ with invalid data returns failure") {
    post("/schema/example4", body="{}{}{}{}}}}") {
      status should equal(400)
      body should include("error")
      body should include("example4")
      body should include("Invalid JSON")
      assert(dataStore.getSchemaText("example4").isEmpty)
    }
  }

  test("POST /validate/ with invalid id returns failure") {
    post("/schema/unknown", body="{}") {
      assert(status === 404)
      assert(body.contains("schema not found"))
    }
  }

  test("POST /validate/ with invalid data returns failure") {
    post("/schema/example2", body="{\"age\": -20}") {
      assert(status === 422) // unprocessable entity
      body should include("action")
      body should include("id")
      body should include("status")
      body should include("message")
    }
  }

  test("POST /validate/ with valid data returns success") {
    post("/schema/example2", body="{\"firstName\": \"amartya\"}") {
      assert(status === 200)
      body should include("action")
      body should include("id")
      body should include("status")
    }
  }

  test("POST /validate/ with nullable valid data removes nulls and returns failure") {
    post("/schema/example2", body="{\"firstName\": null}") {
      assert(status === 422) // unprocessable entity
      body should include("action")
      body should include("id")
      body should include("status")
      body should include("message")
    }
  }

}
