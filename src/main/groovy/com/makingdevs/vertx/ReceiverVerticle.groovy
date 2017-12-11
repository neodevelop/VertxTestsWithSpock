package com.makingdevs.vertx

def eb = vertx.eventBus()

eb.consumer("com.makingdevs.ping") { msg ->

  eb.send("com.makingdevs.pong", "Pong!")
}
