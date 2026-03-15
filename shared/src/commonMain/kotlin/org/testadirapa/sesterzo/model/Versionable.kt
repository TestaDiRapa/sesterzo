package org.testadirapa.sesterzo.model

interface Versionable : Identifiable {
	val version: SemanticVersion
}