package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.model.Space

class SpaceDAOImpl(client: DBClient) : SpaceDAO(client) {

	override fun getByParticipant(userId: String): Flow<Space> =
		find(Filters.exists("${Space::users.name}.$userId"))

	override fun getByOwner(userId: String): Flow<Space> =
		find(Filters.eq(Space::owner.name, userId))

	override suspend fun setSpaceThumbnail(spaceId: String, pictureRef: String): Space? =
		collection.findOneAndUpdate(
			filter = Filters.eq("_id", spaceId),
			update = Updates.combine(
				Updates.set(Space::pictureReference.name, pictureRef)
			),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)

	override suspend fun setSpaceNameAndColor(spaceId: String, name: String, color: RGBColor): Space? =
		collection.findOneAndUpdate(
			filter = Filters.eq("_id", spaceId),
			update = Updates.combine(
				Updates.set(Space::name.name, name),
				Updates.set(Space::color.name, color)
			),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)
}