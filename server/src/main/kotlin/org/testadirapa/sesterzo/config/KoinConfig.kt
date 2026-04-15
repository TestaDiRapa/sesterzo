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
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.dao.RecoveryDAO
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.dao.impl.BudgetDAOImpl
import org.testadirapa.sesterzo.dao.impl.BudgetElementDAOImpl
import org.testadirapa.sesterzo.dao.impl.RecoveryDAOImpl
import org.testadirapa.sesterzo.dao.impl.SpaceDAOImpl
import org.testadirapa.sesterzo.dao.impl.UserDAOImpl
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.logic.BudgetElementLogic
import org.testadirapa.sesterzo.logic.BudgetLogic
import org.testadirapa.sesterzo.logic.CaptchaLogic
import org.testadirapa.sesterzo.logic.RecoveryLogic
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.logic.impl.AuthenticationLogicImpl
import org.testadirapa.sesterzo.logic.impl.BudgetElementLogicImpl
import org.testadirapa.sesterzo.logic.impl.BudgetLogicImpl
import org.testadirapa.sesterzo.logic.impl.CaptchaLogicImpl
import org.testadirapa.sesterzo.logic.impl.RecoveryLogicImpl
import org.testadirapa.sesterzo.logic.impl.SpaceLogicImpl
import org.testadirapa.sesterzo.logic.impl.UserLogicImpl
import org.testadirapa.sesterzo.security.JwtConfig
import org.testadirapa.sesterzo.security.JwtManager

fun applicationModules(
	config: ApplicationConfig,
	logger: Logger,
) = module {
	single<JwtManager> { JwtManager(config = JwtConfig.fromConfig(config)) }
	single<DBClient> { DBClient(dbCredentials = MongoDBCredentials.fromConfig(config)) }
	single<PasswordEncoder> { BCryptPasswordEncoder() }

	single<Mailer> {
		when (val mailerConfig = MailerConfig.fromConfig(config)) {
			is MailerConfig.HermesMailerConfig -> HermesMailer(mailerConfig)
			MailerConfig.LocalMailerConfig -> LocalMailer(logger)
		}
	}

	// DAOs
	single<BudgetDAO> { BudgetDAOImpl(client = get()) }
	single<BudgetElementDAO> { BudgetElementDAOImpl(client = get()) }
	single<RecoveryDAO> { RecoveryDAOImpl(client = get()) }
	single<SpaceDAO> { SpaceDAOImpl(client = get()) }
	single<UserDAO> { UserDAOImpl(client = get()) }

	// Logics
	single<BudgetLogic> { BudgetLogicImpl(budgetDAO = get()) }
	single<BudgetElementLogic> { BudgetElementLogicImpl(budgetElementDAO = get()) }
	single<CaptchaLogic> { CaptchaLogicImpl(config = CaptchaLogic.Config.fromConfig(config)) }
	single<AuthenticationLogic> {
		AuthenticationLogicImpl(
			tokenLength = config.property("sesterzo.security.tokenLength").getAs<Int>(),
			mailer = get(),
			userDAO = get(),
			spaceDAO = get(),
			captchaLogic = get(),
			jwtManager = get(),
			passwordEncoder = get()
		)
	}
	single<RecoveryLogic> { RecoveryLogicImpl(recoveryDAO = get()) }
	single<SpaceLogic> { SpaceLogicImpl(budgetElementDAO = get(), spaceDAO = get()) }
	single<UserLogic> { UserLogicImpl(userDAO = get()) }

}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {

	install(Koin) {
		slf4jLogger()
		modules(applicationModules(environment.config, log))
	}
}
