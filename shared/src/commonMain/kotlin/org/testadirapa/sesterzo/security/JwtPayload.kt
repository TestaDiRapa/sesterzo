package org.testadirapa.sesterzo.security

import com.icure.kryptom.utils.base64Decode
import io.ktor.util.date.GMTDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.UserSpaceRole
import org.testadirapa.sesterzo.security.JwtPayload.Companion.isJwtExpiredOrInvalid
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.SPACES_KEY
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.USER_ID_KEY
import org.testadirapa.sesterzo.serialization.Serialization
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class JwtPayload(
	@SerialName(USER_ID_KEY) override val userId: String,
	@SerialName(SPACES_KEY) override val spaces: Map<String, UserSpaceRole>,
	val exp: Long,
) : UserJwtClaims {

	companion object {
		fun decodeClaims(jwt: String): JwtPayload {
			val parts = jwt.split(".").also {
				check(it.size == 3) { "Invalid JWT token" }
			}
			return Serialization.lenientJson.decodeFromString<JwtPayload>(base64Decode(parts[1]).decodeToString())
		}

		fun isJwtExpiredOrInvalid(jwt: String, refreshPadding: Duration = 0L.seconds): Boolean = runCatching {
			val payload = decodeClaims(jwt)
			(payload.exp * 1000) < (GMTDate().timestamp - refreshPadding.inWholeMilliseconds)
		}.getOrDefault(false)
	}
}