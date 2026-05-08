package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.EncryptedAttachment

interface AttachmentLogic {

	suspend fun getAttachment(spaceId: String, attachmentId: String): EncryptedAttachment
}