package com.github.stkale.spoon.dialogs

import com.github.stkale.spoon.domain.Assignment
import com.github.stkale.spoon.services.MyApplicationService
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import org.jetbrains.annotations.Nullable
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.ListSelectionModel
import kotlin.coroutines.CoroutineContext

class BrowseAssignmentsDialog : DialogWrapper(true), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Swing
    private val dialogPanel: JPanel = JPanel()

    init {
        title = "Browse Assignments"
        dialogPanel.preferredSize = Dimension(500, 500)

        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        loadAssignments()

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

    private fun updateAssignments(data: List<Assignment>) {
        val assignmentList = JBList(data)
        assignmentList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val assignmentScroller = JBScrollPane(assignmentList)
        assignmentScroller.preferredSize = Dimension(500, 490)

        dialogPanel.add(assignmentScroller)
    }
}
