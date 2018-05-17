/*
  An object that stores configuration values that are needed for application execution
 */

object Configuration {

  def getKeyValueStoreHost = "localhost"
  def getKeyValueStorePort = 6379

  /*
    Returns a list of commands that are used to configure the default redis connection
   */
  def redisStartUpCommands = List(
    "appendonly yes" // used to enable more a durable persistence mode
  )
}