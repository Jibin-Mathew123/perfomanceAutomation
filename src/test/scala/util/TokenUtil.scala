package util

import utils.AuthTokenGenerator
import io.gatling.core.session.Session // your Java class
class TokenUtil {
  object TokenUtil {
    def generateTokenSession(session: Session): Session = {
      val token = AuthTokenGenerator.generateToken()
      session.set("authToken", token)
    }
  }

}
