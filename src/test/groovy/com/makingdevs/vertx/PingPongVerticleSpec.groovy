package com.makingdevs.vertx

import io.vertx.core.Vertx
import spock.lang.*
import spock.util.concurrent.PollingConditions

@Stepwise
class PingPongVerticleSpec extends Specification {

  // We use the vertx instance provided
  @Shared Vertx vertx
  // Where are the verticle files?
  @Shared String baseDir = "com/makingdevs/vertx"
  // The verticle filename
  @Shared String verticleName = "ReceiverVerticle.groovy"
  // We use this id for deploy and undeploy
  @Shared String verticleId
  // Look ma! The async magic...!
  PollingConditions conditions = new PollingConditions()

  /*
  * For all the specs inside this file (executing once):
  * - Deploying the verticle with the vertx instance
  * - `assert` the deployment identifier
  * - Waiting for deployment
  */
  def setupSpec() {
    vertx = Vertx.vertx()
    vertx.deployVerticle("${baseDir}/${verticleName}") { deployResponse ->
      assert deployResponse.succeeded()
      verticleId = deployResponse.result
    }
    Thread.sleep 1000
  }

  /*
  * At the end of all the specs:
  * - Waiting for attend all the messages
  * - Undeploying the verticle
  * - Closing vertx resource
  */
  def cleanupSpec() {
    Thread.sleep 2000
    vertx.undeploy(verticleId) { response ->
      assert response.succeeded()
      vertx.close()
    }
  }

  def "Making Ping to a Verticle"(){
    given: "A message for send, and an empty response"
      def message = "Ping!"
      def response = ""
    and: "A consumer in vertx for waiting the response"
      vertx.eventBus().consumer("com.makingdevs.pong") { msg ->
        response = msg.body()
      }
    when: "We send a message, and wait..."
      vertx.eventBus().send("com.makingdevs.ping", message)
      conditions.eventually { // This should happen...
        assert response == "Pong!"
      }
    then: "We shouldn't thrown exception because conditions was satisfied..."
      noExceptionThrown()
  }

}
