# VertxTestsWithSpock

## Making async tests with Spockframework

This is a demosntration of the use of Spock with the verticles declared by Vert.x

This is the deal:

- We declare a Verticle like a `consumer`, that is our test subject
- We write a test, where:
    - We make the setup of the verticle, startup and shutdown
    - Send a simple message and respond to another `consumer`
    - Declare a `consumer` for receiving the response, this is like a Mock
    - We use `PollingConditions` and `eventually` method to check if we receive the message
    
 More content, inside the code...
