package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.typesafe.config.{Config, ConfigFactory}

class apigee extends Simulation {

  val env = System.getProperty("env", "dev")
  val config: Config = ConfigFactory.parseResources(s"environments/${env}.conf")

  val baseUrl = config.getString("baseUrl")

  val httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("application/json")

  val scn = scenario("Get API Request")
    .exec(
      http("Get Single User")
        .get("/users?page=2")
        .check(status.is(200))
      //          jsonPath("$.data.first_name").is("Janet"))

    )

  setUp(scn.inject(constantUsersPerSec(100).during(60))
    .protocols(httpProtocol))
}
