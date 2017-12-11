package com.makingdevs.vertx

import io.vertx.core.Vertx
import spock.lang.*
import spock.util.concurrent.PollingConditions

@Stepwise
class PingPongVerticleSpec extends Specification {

  @Shared Vertx vertx
  @Shared String baseDir = "com/makingdevs/vertx"
  @Shared String verticleName = "ReceiverVerticle.groovy"
  @Shared String verticleId
  PollingConditions conditions = new PollingConditions()

  def setupSpec() {
    vertx = Vertx.vertx()
    vertx.deployVerticle("${baseDir}/${verticleName}") { deployResponse ->
      assert deployResponse.succeeded()
      verticleId = deployResponse.result
    }
    Thread.sleep 1000
  }

  def cleanupSpec() {
    Thread.sleep 2000
    vertx.undeploy(verticleId) { response ->
      assert response.succeeded()
      vertx.close()
    }
  }

  def "Making Ping to a Verticle"(){
    given:
      def message = "Ping!"
      def response = ""
    and:
      vertx.eventBus().consumer("com.makingdevs.pong") { msg ->
        response = msg.body()
      }
    when:
      vertx.eventBus().send("com.makingdevs.ping", message)
      conditions.eventually {
        assert response == "Pong!"
      }
    then:
      noExceptionThrown()
  }

}
