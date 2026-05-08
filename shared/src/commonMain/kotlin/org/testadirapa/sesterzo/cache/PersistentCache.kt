package org.testadirapa.sesterzo.cache

interface PersistentCache {

	val attachment: AttachmentPersistentCache
	val budget: BudgetPersistentCache
	val budgetElement: BudgetElementPersistentCache
	val entry: EntryPersistentCache
	val space: SpacePersistentCache
	val user: UserPersistentCache

}