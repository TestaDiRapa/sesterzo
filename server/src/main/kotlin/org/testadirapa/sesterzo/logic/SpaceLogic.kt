package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.SpaceStub
import org.testadirapa.sesterzo.exceptions.QuotaExceededException
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.model.RGBColor

interface SpaceLogic {

	/**
	 * Retrieves all the spaces available for the current user.
	 */
	fun getSpaces(): Flow<Space>

	/**
	 * Creates a new [Space] from a [SpaceStub]. It creates the templates for incomes, savings, and expenses, and
	 * initializes the indexes in the newly created collection for the spaces.
	 *
	 * @param spaceStub the [spaceStub] with the encrypted key.
	 * @return the newly created [Space].
	 * @throws QuotaExceededException if the user already created more than a fixed number of spaces.
	 */
	suspend fun createSpace(spaceStub: SpaceStub): Space
	suspend fun getSpace(spaceId: String): Space
	suspend fun setSpacePicture(spaceId: String, picture: EncryptedAttachment): Space
	suspend fun setSpaceNameAndColor(spaceId: String, name: String, color: RGBColor): Space
}