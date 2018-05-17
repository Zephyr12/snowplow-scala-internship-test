import com.redis._;

/*
  A Redis-Backed implementation of a KeyValueStore
 */

class RedisKeyValueStore(connection: RedisClient) extends KeyValueStore {

  /*
    returns the Some(value) associated with key if the key can be found in the redis instance. None otherwise.
  */
  override def get(key: String): Option[String] = connection.get(key)

  /*
    Sets store[key] = value.
   */
  override def set(key: String, value: String): Unit = connection.set(key, value)
}

object RedisKeyValueStore {

  /*
    Creates a new KeyValueStore backed by redis using the configuration found in Configuration
   */
  def getDefaultKVStore(): KeyValueStore = {
    var connection = new RedisClient(
      Configuration.getKeyValueStoreHost,
      Configuration.getKeyValueStorePort)

    Configuration.redisStartUpCommands.foreach(command => connection.send(command))
    return new RedisKeyValueStore(connection);
  }
}
