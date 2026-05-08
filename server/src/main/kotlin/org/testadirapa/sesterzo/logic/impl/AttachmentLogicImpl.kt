package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.AttachmentDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.AttachmentLogic
import org.testadirapa.sesterzo.model.EncryptedAttachment

class AttachmentLogicImpl(
	private val attachmentDAO: AttachmentDAO,
) : AttachmentLogic {

	override suspend fun getAttachment(
		spaceId: String,
		attachmentId: String
	): EncryptedAttachment = attachmentDAO.getById(spaceId = spaceId, id = attachmentId)
		?: throw EntityNotFoundException(attachmentId, ExceptionLabel.AttachmentNotFound)

}