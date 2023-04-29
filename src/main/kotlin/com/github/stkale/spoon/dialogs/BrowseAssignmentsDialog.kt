package com.github.stkale.spoon.dialogs

import com.github.stkale.spoon.domain.Charon
import com.github.stkale.spoon.services.MyApplicationService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import org.jetbrains.annotations.Nullable
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
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
        // loadAssignments()
        loadHardcodedAssignments()

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

    private fun loadHardcodedAssignments() {
        val assignments = listOf(
            Charon(1, "EX00 - Intro"),
            Charon(2, "EX01 - Hello"),
            Charon(3, "EX02 - Matemaatilised avaldised"),
            Charon(4, "EX03 - ID-code"),
            Charon(5, "EX04 - Andmestruktuurid list ja tuple"),
            Charon(6, "EX05 - Hobbies"),
            Charon(7, "EX06 - Regex"),
            Charon(8, "EX07 - Failist lugemine, faili kirjutamine"),
            Charon(9, "EX08 - Testimine (1)"),
            Charon(10, "EX08 - Testimine (2)"),
            Charon(11, "EX08 - Testimine (3)"),
        )

        updateAssignments(assignments)
    }

    private fun updateAssignments(data: List<Charon>) {
        if (data.isNotEmpty()) {
            thisLogger().warn("got ${data.size} assignment" +
                    if (data.size > 1) "s" else "")
        } else {
            thisLogger().warn("got no assignments")
        }

        val assignmentList = JBList(data)
        assignmentList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val assignmentScroller = JBScrollPane(assignmentList)
        assignmentScroller.preferredSize = Dimension(500, 490)

        dialogPanel.add(assignmentScroller)
    }
}
