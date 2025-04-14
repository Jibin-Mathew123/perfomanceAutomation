package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.typesafe.config.{Config, ConfigFactory}

class apigee extends Simulation {

  val env = System.getProperty("env", "qa")
  val config: Config = ConfigFactory.parseResources(s"environments/${env}.conf")

  val baseUrl = config.getString("baseUrl")
  val users: Int = Integer.getInteger("users", 10)
  val rampDuration: Int = Integer.getInteger("ramp", 10)
  val testDuration: Int = Integer.getInteger("duration", 60)

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

  setUp(scn.inject( rampUsers(users) during (rampDuration.seconds))
    .protocols(httpProtocol)).maxDuration(testDuration.seconds)
}
