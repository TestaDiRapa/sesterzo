package org.testadirapa.sesterzo

import org.testadirapa.sesterzo.api.FullSesterzoApi
import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.repository.PropertyRepository

object AppCtx {

	lateinit var propertyRepository: PropertyRepository
	lateinit var api: FullSesterzoApi
	var currency: Currency = Currency.EUR
	var sendErrors: Boolean = false
}