package org.testadirapa.sesterzo

import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.repository.PropertyRepository

object AppCtx {

	lateinit var propertyRepository: PropertyRepository

	lateinit var api: SesterzoApi
}