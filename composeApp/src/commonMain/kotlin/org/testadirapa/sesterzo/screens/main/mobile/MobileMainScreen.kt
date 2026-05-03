package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.components.mobile.main.BottomMenu
import org.testadirapa.sesterzo.components.mobile.main.HeaderBar
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen
import org.testadirapa.sesterzo.screens.main.mobile.template.MobileTemplateScreen

enum class Page { Budget, Template }

@Composable
fun MobileMainScreen(
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	var currentPage by remember { mutableStateOf(Page.Budget) }
	var space by remember { mutableStateOf(initialSpace) }
	Scaffold(
		topBar = {
			HeaderBar(
				space = space,
				onCreateSpace = onCreateSpace,
				onSwitchSpace = { space = it },
				onError = onError,
			)
		},
		bottomBar = {
			BottomMenu(
				currentPage = currentPage,
				onPageChange = { currentPage = it },
			)
		}
	) { innerPadding ->
		when(currentPage) {
			Page.Budget -> {
				MobileBudgetScreen(
					spaceId = space.id,
					onError = onError,
					contentPadding = innerPadding,
				)
			}
			Page.Template -> {
				MobileTemplateScreen()
			}
		}
	}
}