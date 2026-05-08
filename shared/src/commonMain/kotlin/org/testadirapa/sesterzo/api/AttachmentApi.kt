package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.DecryptedAttachment

interface AttachmentApi {

	/**
	 * @param spaceId the id of the space.
	 * @param attachmentId the id of the attachment.
	 * @param bypassCache whether to bypass the local cache.
	 * @return a [DecryptedAttachment] if one was found in the space with the specified id, null otherwise
	 */
	suspend fun getAttachmentInSpace(spaceId: String, attachmentId: String, bypassCache: Boolean): DecryptedAttachment?
}