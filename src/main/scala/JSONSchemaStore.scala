import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}

class JSONSchemaStore(keyValueStore: KeyValueStore){
  def get(id: String) : Option[JsonSchema] =
    try {
      return keyValueStore
        .get(id)
        .map(JsonLoader.fromString)
        .map(JsonSchemaFactory.byDefault().getJsonSchema)
    } catch {
      case e: Exception => return None
    }



}
