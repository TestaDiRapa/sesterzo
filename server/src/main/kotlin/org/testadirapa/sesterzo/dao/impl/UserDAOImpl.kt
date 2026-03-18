package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters.eq
import org.testadirapa.sesterzo.annotations.Index
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.model.User

class UserDAOImpl(client: DBClient) : UserDAO(client) {
	@Index(name = "by_email", property = "email", unique = true)
	override suspend fun getByEmail(email: String): User? = get(eq(User::email.name, email))
}
