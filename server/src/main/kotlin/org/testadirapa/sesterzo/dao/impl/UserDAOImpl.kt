package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Filters.not
import com.mongodb.client.model.Filters.exists
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import org.testadirapa.sesterzo.annotations.Index
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.User

class UserDAOImpl(client: DBClient) : UserDAO(client) {
	@Index(name = "by_email", property = "email", unique = true)
	override suspend fun getByEmail(email: String): User? = get(eq(User::email.name, email))

	override suspend fun setPublicKey(userId: String, publicKey: Base64String): User? =
		collection.findOneAndUpdate(
			filter = and(
				eq("_id", userId),
				or(
					not(exists(User::publicKey.name)),
					eq(User::publicKey.name, null),
				)
			),
			update = Updates.set(User::publicKey.name, publicKey),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)

	override suspend fun setBackupConfirmation(userId: String): User? =
		collection.findOneAndUpdate(
			filter = eq("_id", userId),
			update = Updates.set(User::hasBackup.name, true),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)

	override suspend fun setName(userId: String, name: String): User? =
		collection.findOneAndUpdate(
			filter = eq("_id", userId),
			update = Updates.set(User::name.name, name),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)

	override suspend fun setCurrency(userId: String, currency: Currency): User? =
		collection.findOneAndUpdate(
			filter = eq("_id", userId),
			update = Updates.set(User::preferredCurrency.name, currency),
			options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
		)
}
