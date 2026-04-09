package org.testadirapa.sesterzo.utils

class BitArray(val size: Int) {
	private val data = IntArray((size + 31) / 32)

	fun setBit(pos: Int, v: Boolean) {
		val mask = 1 shl (31 - pos % 32)
		if (v) data[pos / 32] = data[pos / 32] or mask
		else   data[pos / 32] = data[pos / 32] and mask.inv()
	}

	fun getBit(pos: Int): Boolean =
		(data[pos / 32] ushr (31 - pos % 32)) and 1 == 1

	fun setOctet(bytePos: Int, b: Byte) {
		val v = b.toInt() and 0xFF
		for (i in 0..7) setBit(bytePos * 8 + i, (v ushr (7 - i)) and 1 == 1)
	}

	fun getOctet(bytePos: Int): Byte {
		var v = 0
		for (i in 0..7) if (getBit(bytePos * 8 + i)) v = v or (1 shl (7 - i))
		return v.toByte()
	}

	fun setInt(bitPos: Int, len: Int, value: Int) {
		for (i in 0 until len) setBit(bitPos + i, (value ushr (len - 1 - i)) and 1 == 1)
	}

	fun getInt(bitPos: Int, len: Int): Int {
		var v = 0
		for (i in 0 until len) if (getBit(bitPos + i)) v = v or (1 shl (len - 1 - i))
		return v
	}
}