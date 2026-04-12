package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.Space

interface SpaceApi {

	suspend fun getSpaces(): List<Space>

	suspend fun createSpace(name: String, picture: ByteArray?): Space
}