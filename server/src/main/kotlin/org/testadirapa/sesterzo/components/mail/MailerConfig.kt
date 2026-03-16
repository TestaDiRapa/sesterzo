package org.testadirapa.sesterzo.components.mail

import io.ktor.server.config.ApplicationConfig

data class MailerConfig(
	val homunculusUrl: String,
	val hermesUrl: String,
	val resetPasswordTemplateId: String,
	val inviteTemplateId: String,
	val alertTemplateId: String,
	val reportTemplateId: String,
) {
	companion object {
		fun fromConfig(config: ApplicationConfig) =
			MailerConfig(
				homunculusUrl = config.property("mailer.homunculusUrl").getString(),
				hermesUrl = config.property("mailer.hermesUrl").getString(),
				resetPasswordTemplateId = config.property("mailer.resetPasswordTemplateId").getString(),
				inviteTemplateId = config.property("mailer.inviteTemplateId").getString(),
				alertTemplateId = config.property("mailer.alertTemplateId").getString(),
				reportTemplateId = config.property("mailer.reportTemplateId").getString(),
			)
	}
}