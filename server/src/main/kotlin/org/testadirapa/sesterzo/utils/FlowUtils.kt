package org.testadirapa.sesterzo.utils

import kotlinx.coroutines.flow.Flow

suspend inline fun <F, S> Flow<Pair<F, S>>.toMap(): Map<F, S> {
	val result = mutableMapOf<F, S>()
	collect { (f, s) ->
		result[f] = s
	}
	return result
}