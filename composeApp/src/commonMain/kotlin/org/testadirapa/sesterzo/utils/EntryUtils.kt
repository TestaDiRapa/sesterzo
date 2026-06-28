package org.testadirapa.sesterzo.utils

import org.testadirapa.sesterzo.model.DecryptedEntry

fun List<DecryptedEntry>.groupActiveByDay(): Map<Int, List<DecryptedEntry>> =
	filterNot { it.deleted }.groupBy { it.updated.toLocalDateTime().day }