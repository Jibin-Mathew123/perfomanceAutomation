package simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class microservice extends Simulation{

  val env = System.getProperty("env", "dev")
  val config: Config = ConfigFactory.parseResources(s"environments/${env}.conf")

  val baseUrl = config.getString("baseUrl")
  //protocol

  val httpProtocol = http.baseUrl(baseUrl)

  //scenario

  val scn = scenario("Get API Request")
    .exec(
      http("Get Single User")
        .get("/users?page=2" )
        .check(status.is(200))
//          jsonPath("$.data.first_name").is("Janet"))

    )

  //setup

  setUp(scn.inject(constantUsersPerSec(100).during(60))
    .protocols(httpProtocol))
}
