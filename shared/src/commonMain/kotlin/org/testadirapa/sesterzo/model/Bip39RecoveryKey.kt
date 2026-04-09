package org.testadirapa.sesterzo.model

data class Bip39RecoveryKey(
	val words: List<String>
) {

	init {
		require(words.size == 24) {
			"A bip 39 recovery key myst have 24 words."
		}
	}

	companion object {
		private val splitRegex = "([a-z]+)".toRegex()

		private fun splitBip39String(str: String): List<String> =
			splitRegex.findAll(str.lowercase()).map { it.groupValues[1] }.toList()

		fun fromString(str: String): Bip39RecoveryKey = Bip39RecoveryKey(
			words = splitBip39String(str)
		)

		fun String.isBip39Valid(): Boolean = splitBip39String(this).size == 24
	}

}