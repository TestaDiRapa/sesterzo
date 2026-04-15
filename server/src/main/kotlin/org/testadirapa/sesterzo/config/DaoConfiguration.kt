package org.testadirapa.sesterzo.config

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.install
import io.ktor.server.application.log
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.ktor.ext.getKoin
import org.testadirapa.sesterzo.annotations.Index
import org.testadirapa.sesterzo.dao.GenericMultiCollectionDAO
import org.testadirapa.sesterzo.dao.GenericSingleCollectionDAO
import org.testadirapa.sesterzo.dao.SpaceDAO
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf

@OptIn(KoinInternalApi::class)
private inline fun <reified T> getDAOsOfType(koin: Koin): List<T> = koin.instanceRegistry.instances.values
	.map { it.beanDefinition }
	.filter { it.kind == Kind.Singleton }
	.filter { it.primaryType.isSubclassOf(T::class) }
	.map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) as T }

@OptIn(KoinInternalApi::class)
val databaseInitializationPlugin =
	createApplicationPlugin("DatabaseInitializer") {
		on(MonitoringEvent(ApplicationStarted)) { application ->
			application.log.info("Starting database configuration")
			val koin = application.getKoin()
			run {
				// Init indexes on single collection DAOs
				val singleCollectionDaoList = getDAOsOfType<GenericSingleCollectionDAO<*>>(koin)

				runBlocking {
					singleCollectionDaoList.forEach { dao ->
						dao::class.functions
							.filter { fn -> fn.annotations.any { it.annotationClass == Index::class } }
							.flatMap { fn -> fn.annotations.filterIsInstance<Index>() }
							.forEach { index ->
								dao.initIndex(index.property, index.name, index.unique)?.let {
									application.log.info("Created index $it on ${dao::class.simpleName}")
								}
							}
					}
				}

				// Init indexes on multi-collection DAOs
				val multiCollectionDaoList = getDAOsOfType<GenericMultiCollectionDAO<*>>(koin)
				val spaceDAO = koin.get<SpaceDAO>()
				runBlocking {
					spaceDAO.get().collect { space ->
						multiCollectionDaoList.forEach { dao ->
							dao.initIndexes(spaceId = space.id)
							application.log.info("Initialized indexes on ${dao::class.simpleName} for space ${space.id}")
						}
					}

				}
			}
			application.log.info("Database configuration completed")
		}
	}



fun Application.configureDAO() {
	install(databaseInitializationPlugin)
}
