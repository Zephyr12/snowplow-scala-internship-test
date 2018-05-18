import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import com.github.fge.jsonschema.main._;

class JSONSchemaStoreTest extends FunSuite with BeforeAndAfter {
  var jsonSchemaStore : JSONSchemaStore = _

  before {
    var mockDataStore: KeyValueStore = new KeyValueStore {

      var mapping = Map(
        "example1" -> "{'a' : 2}",
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
    }
    jsonSchemaStore = new JSONSchemaStore(mockDataStore)
  }

  test("getting an unknown value returns none") {
    assert(jsonSchemaStore.getSchema("not_present").isEmpty)
  }

  test("getting a bad json schema value returns none") {
    assert(jsonSchemaStore.getSchema("example1").isEmpty)
  }

  test("correct json schema returns a json schema") {
    assert(jsonSchemaStore.getSchema("example2").isDefined)
  }

  test("set sets new value in datastore") {
    jsonSchemaStore.set("example3", jsonSchemaStore.getSchemaText("example2").get)
    assert(jsonSchemaStore.getSchema("example3").isDefined)
  }

  test("set raises exception on invalid schema") {
    assertThrows[com.fasterxml.jackson.core.JsonParseException](jsonSchemaStore.set("example3", "{'a' : 2}"))
  }

}
