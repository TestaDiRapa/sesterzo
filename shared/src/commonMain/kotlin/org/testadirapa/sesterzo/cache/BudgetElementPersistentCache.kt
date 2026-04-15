package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement

interface BudgetElementPersistentCache : PersistenceOperator<EncryptedBudgetElement, CachedBudgetElement>