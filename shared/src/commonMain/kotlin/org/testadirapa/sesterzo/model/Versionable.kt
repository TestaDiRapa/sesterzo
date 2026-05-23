package org.testadirapa.sesterzo.model

interface Versionable : Identifiable {
	val version: Int

	companion object {
		fun idOf(entityId: String, version: Int) = "$entityId-$version"
	}
}