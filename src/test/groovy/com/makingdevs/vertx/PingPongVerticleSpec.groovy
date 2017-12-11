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
  PollingConditions conditions = new PollingConditions(timeout: 10, initialDelay: 1.5, factor: 1.25)

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

  def "Simple specification"(){
    given:
      def message = "Ping!"
    when:
      def size = message.size()
    then:
      size == 5
  }

}
