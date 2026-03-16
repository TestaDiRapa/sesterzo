package org.testadirapa.sesterzo.model

interface EncryptedData {
	val encryptedSelf: Base64String
	val accessKeys: Set<AccessKey>
}