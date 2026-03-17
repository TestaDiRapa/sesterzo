package org.testadirapa.sesterzo.components.mail

import io.ktor.server.config.ApplicationConfig

sealed interface MailerConfig {

	companion object {
		fun fromConfig(config: ApplicationConfig) =
			if (config.propertyOrNull("mailer.hermesUrl") != null) {
				HermesMailerConfig(
					hermesUrl = config.property("mailer.hermesUrl").getString(),
					registrationTemplateId = config.property("mailer.registrationTemplateId").getString(),
					tokenTemplateId = config.property("mailer.tokenTemplateId").getString(),
				)
			} else LocalMailerConfig

	}

	data class HermesMailerConfig(
		val hermesUrl: String,
		val registrationTemplateId: String,
		val tokenTemplateId: String,
	): MailerConfig

	data object LocalMailerConfig : MailerConfig
}