package org.testadirapa.sesterzo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.config.PlatformContext
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.UserAccessKey
import org.testadirapa.sesterzo.model.UserSpaceRole

@Composable
fun CreateSpaceScreen(
	isFirst: Boolean
) {
	val scope = rememberCoroutineScope()
	var space by remember { mutableStateOf<Space?>(null) }
	LaunchedEffect("test") {
		PlatformContext.persistentCache().space.upsert(
			Space(
				id = "id2",
				version = 0,
				name = "name",
				owner = "owner",
				fixedExpensesTemplateId = "fixedExpensesTemplateId",
				incomeSourcesTemplateId = "incomeSourcesTemplateId",
				savingsTemplateId = "savingsTemplateId",
				users = mapOf(
					"user" to UserAccessKey(accessLevel = UserSpaceRole.User, encryptedKey = "")
				),
				picture = null
			)
		)
	}
	Column {
		FormButton(
			onClick = {
				println("CreateSpaceScreen clicked!")
				scope.launch {
					val result = PlatformContext.persistentCache().space.getById("id2")?.space
					println(result)
					space = result
				}
			},
			text = "Create a space",
		)
		space?.let {
			Text("${it.id} - ${it.version} - ${it.owner} - ${it.fixedExpensesTemplateId} - ${it.users}")
		}
	}

}