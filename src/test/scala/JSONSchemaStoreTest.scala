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
    assert(jsonSchemaStore.get("not_present").isEmpty)
  }

  test("getting a bad json schema value returns none") {
    assert(jsonSchemaStore.get("example1").isEmpty)
  }

  test("correct json schema returns a json schema") {
    assert(jsonSchemaStore.get("example2").isDefined);
  }


}
