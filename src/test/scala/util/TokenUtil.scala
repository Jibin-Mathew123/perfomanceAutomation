package simulations.util

import utils.AuthTokenGenerator

class TokenUtil {

  import io.gatling.core.session.Session // your Java class

  object TokenUtil {
    def generateTokenSession(session: Session): Session = {
      val token = AuthTokenGenerator.generateToken()
      session.set("authToken", token)
    }
  }

}
