import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}

class JSONSchemaStore(keyValueStore: KeyValueStore){

  def set(id: String, schema: String) = {
    JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromString(schema))
    keyValueStore.set(id, schema)
  }


  def getSchema(id: String) : Option[JsonSchema] =
    try {
      getSchemaText(id)
        .map(JsonLoader.fromString)
        .map(JsonSchemaFactory.byDefault().getJsonSchema)
    } catch {
      case e: Exception => return None
    }

  def getSchemaText(id: String) =
    try {
      keyValueStore
        .get(id)
        .map(JsonLoader.fromString)
        .map(JsonSchemaFactory.byDefault().getJsonSchema)
      keyValueStore.get(id)
    } catch {
      case e: Exception => None
    }


}
