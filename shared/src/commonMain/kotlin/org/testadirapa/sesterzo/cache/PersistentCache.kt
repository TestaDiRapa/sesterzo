package org.testadirapa.sesterzo.cache

interface PersistentCache {

	val space: SpacePersistentCache
	val user: UserPersistentCache

}