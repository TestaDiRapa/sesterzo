package org.testadirapa.sesterzo.config

import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.getAs
import io.ktor.util.logging.Logger
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.testadirapa.sesterzo.components.mail.Mailer
import org.testadirapa.sesterzo.components.mail.impl.HermesMailer
import org.testadirapa.sesterzo.components.mail.MailerConfig
import org.testadirapa.sesterzo.components.mail.impl.LocalMailer
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.components.mongodb.MongoDBCredentials
import org.testadirapa.sesterzo.components.security.BCryptPasswordEncoder
import org.testadirapa.sesterzo.components.security.PasswordEncoder
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.dao.impl.SpaceDAOImpl
import org.testadirapa.sesterzo.dao.impl.UserDAOImpl
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.logic.impl.AuthenticationLogicImpl
import org.testadirapa.sesterzo.security.JWTConfig
import org.testadirapa.sesterzo.security.JWTManager

fun applicationModules(
	dbCredentials: MongoDBCredentials,
	jwtConfig: JWTConfig,
	mailerConfig: MailerConfig,
	config: ApplicationConfig,
	logger: Logger,
) = module {
	single<JWTManager> { JWTManager(jwtConfig) }
	single<DBClient> { DBClient(dbCredentials) }
	single<PasswordEncoder> { BCryptPasswordEncoder() }

	single<Mailer> {
		when (mailerConfig) {
			is MailerConfig.HermesMailerConfig -> HermesMailer(mailerConfig)
			MailerConfig.LocalMailerConfig -> LocalMailer()
		}
	}

	// DAOs
	single<SpaceDAO> { SpaceDAOImpl(client = get()) }
	single<UserDAO> { UserDAOImpl(client = get()) }

	// Logics
	single<AuthenticationLogic> {
		AuthenticationLogicImpl(
			tokenLength = config.property("sesterzo.security.tokenLength").getAs<Int>(),
			mailer = get(),
			userDAO = get(),
			spaceDAO = get(),
			jwtManager = get()
		)
	}

}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {
	val dbCredentials = MongoDBCredentials.fromConfig(environment.config)
	val jwtConfig = JWTConfig.fromConfig(environment.config)
	val mailerConfig = MailerConfig.fromConfig(environment.config)

	install(Koin) {
		slf4jLogger()
		modules(applicationModules(dbCredentials, jwtConfig, mailerConfig, environment.config, log))
	}
}
