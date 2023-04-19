package com.github.stkale.spoon.dialogs

import com.github.stkale.spoon.domain.Charon
import com.github.stkale.spoon.services.MyApplicationService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.ui.DialogWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import org.jetbrains.annotations.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.coroutines.CoroutineContext

class BrowseAssignmentsDialog : DialogWrapper(true), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Swing

    init {
        title = "Browse Assignments"
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(BorderLayout())

        loadAssignments()

        val label = JLabel("*Assignments show up*")
        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.CENTER)

        return dialogPanel
    }

    private fun loadAssignments() {
        val service = service<MyApplicationService>()
            .getCharonService()

        val coroutineExceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }

        launch(Dispatchers.IO + coroutineExceptionHandler) {

            val assignments = service
                .getCharons()
                .body() ?: emptyList()

            withContext(Dispatchers.Swing) {
                updateAssignments(assignments)
            }
        }
    }

    private fun updateAssignments(assignments: List<Charon>) {
        if (assignments.isNotEmpty()) {
            thisLogger().info("got ${assignments.size} assignments")
        } else {
            thisLogger().info("got no assignments")
        }
    }
}
