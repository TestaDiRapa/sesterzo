package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.CachedUser
import org.testadirapa.sesterzo.model.User

interface UserPersistentCache : PersistenceOperator<User, CachedUser>