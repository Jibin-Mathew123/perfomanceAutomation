package simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.AuthTokenGenerator

import scala.concurrent.duration._

class microservice extends Simulation{
  val authToken: String = AuthTokenGenerator.generateToken()

  val env = System.getProperty("env", "qa")
  val config: Config = ConfigFactory.parseResources(s"environments/${env}.conf")

  val baseUrl = config.getString("baseUrl")
  //protocol

  val httpProtocol = http.baseUrl(baseUrl)
//    .header("Authorization", "#{authToken}") // Using the generated token
  val users: Int = Integer.getInteger("users", 10)
  val rampDuration: Int = Integer.getInteger("ramp", 10)
  val testDuration: Int = Integer.getInteger("duration", 60)

  //scenario

  val scenario1 = scenario("Get List Users")
    .exec(
      http("Get List Users")
        .get("/users?page=2")
        .check(status.is(200))
      //          jsonPath("$.data.first_name").is("Janet"))

    )


  val scenario2 = scenario("Get Single User")
    .exec(
      http("Get Single User")
        .get("/users/2")
        .check(status.is(200))
      //          jsonPath("$.data.first_name").is("Janet"))

    )

  al scenario3 = scenario("Single user not found")
    .exec(
      http("Single user not found")
        .get("/users/23")
        .check(status.is(404))
      //          jsonPath("$.data.first_name").is("Janet"))

    )

  val scenario4 = scenario("List Users")
    .exec(
      http("List Users")
        .get("/unknown")
        .check(status.is(200))
      //          jsonPath("$.data.first_name").is("Janet"))

    )

  setUp(
    scenario1.inject(rampUsers(users) during (rampDuration.seconds)),
    scenario2.inject(rampUsers(users) during (rampDuration.seconds)),
    scenario3.inject(rampUsers(users) during (rampDuration.seconds)),
    scenario4.inject(rampUsers(users) during (rampDuration.seconds))

  ).protocols(httpProtocol)
    .maxDuration(testDuration.seconds)
}
