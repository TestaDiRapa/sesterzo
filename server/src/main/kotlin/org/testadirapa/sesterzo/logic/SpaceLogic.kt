package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.Space

interface SpaceLogic {

	/**
	 * Retrieves all the spaces available for the current user.
	 */
	fun getSpaces(): Flow<Space>

}