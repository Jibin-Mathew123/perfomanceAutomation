package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class microservice extends Simulation{

  //protocol

  val httpProtocol = http.baseUrl("https://reqres.in/api")

  //scenario

  val scn = scenario("Get API Request")
    .exec(
      http("Get Single User")
        .get("/users?page=2" )
        .check(status.is(200))
//          jsonPath("$.data.first_name").is("Janet"))

    )

  //setup

  setUp(scn.inject(constantUsersPerSec(100).during(600))
    .protocols(httpProtocol))
}
