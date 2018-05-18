import com.redis.RedisClient
import org.scalatra.LifeCycle
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {
    implicit val dataStore = new JSONSchemaStore(new RedisKeyValueStore(new RedisClient()))
    // mount servlets like this:
    context mount (new JSONValidatorServlet, "/*")
  }
}