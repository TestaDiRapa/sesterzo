package org.testadirapa.sesterzo.security

object Bip39 {

	val wordlist: List<String> = loadWordlist()

	private fun loadWordlist(): List<String> {
		val stream = Bip39::class.java.getResourceAsStream("bip39_english.txt")
			?: error("Wordlist not found")
		return stream.bufferedReader().readLines()
	}
}