/*
  A simple trait that abstracts over the concept of an external key-value store
 */

trait KeyValueStore {
  /*
    returns the Some(value) associated with key if the key can be found in the database. None otherwise.
   */
  def get(key: String) : Option[String]

  /*
    Sets store[key] = value.
   */
  def set(key: String, value: String)
}
