# To run

On a linux like system. The app uses the redis key value store and sbt to function

```
$ sbt assembly
$ redis-server
$ java -jar target/scala-2.12/JSONValididatorAPI*
```

The application will be hosted on on localhost:8080

# Development Notes

* TDD

* Set up API
    * Need JSON Library
        * Standard lib
        * scala.util.parsing.json._
        * json4s
    * Need JSON Validator Library
        * json-schema-validator
    * Need HTTP API Library
        * Finch
        * http4s
* Set up Persistence
    * Only persistence is KEY - VALUE
    * Simple store like redis or memcached with persistence should be perfect
        * Redis has built-in persistent storage support
    * (REDIS)
* Set up Tests
    * Unit Level testing should be fine

