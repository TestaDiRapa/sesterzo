package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.security.SecurityContext.Companion.flowOnSecurityContext

class SpaceLogicImpl(
	private val spaceDAO: SpaceDAO
) : SpaceLogic {

	override fun getSpaces(): Flow<Space> = flowOnSecurityContext {
		emitAll(
			spaceDAO.getByIds(it.spaces.keys)
		)
	}


}