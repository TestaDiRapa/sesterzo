package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.EntryLogic
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.model.dto.BulkEntryUpdate
import org.testadirapa.sesterzo.security.authenticateGetInSpace
import org.testadirapa.sesterzo.security.authenticatedDeleteInSpace
import org.testadirapa.sesterzo.security.authenticatedPostInSpace
import org.testadirapa.sesterzo.utils.getPathParameter
import kotlin.getValue

fun Routing.expenseController() = route("/entry") {

	val entryLogic by inject<EntryLogic>()

	authenticateGetInSpace("/forBudget/{budgetId}/all") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		call.respond(
			entryLogic.getEntriesForBudget(spaceId = spaceId, budgetId = budgetId)
		)
	}

	authenticateGetInSpace("/forBudget/{budgetId}/after/{timestamp}") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		val timestamp = call.getPathParameter("timestamp").toLong()
		call.respond(
			entryLogic.getEntriesForBudgetAfter(spaceId = spaceId, budgetId = budgetId, after = timestamp)
		)
	}

	authenticatedDeleteInSpace("/{entryId}") { spaceId ->
		val entryId = call.getPathParameter("entryId")
		call.respond(
			entryLogic.deleteEntry(spaceId = spaceId, entryId = entryId)
		)
	}

	authenticatedPostInSpace("") { spaceId ->
		val entry = call.receive<EncryptedEntry>()
		call.respond(
			entryLogic.createEntry(spaceId = spaceId, entry = entry)
		)
	}

	authenticatedPostInSpace("bulk") { spaceId ->
		val entries = call.receive<BulkEntryUpdate>()
		call.respond(
			entryLogic.updateBuiltInEntries(
				spaceId = spaceId,
				entriesToCreate = entries.entriesToCreate,
				entryIdsToDelete = entries.entryIdsToDelete
			)
		)
	}
}