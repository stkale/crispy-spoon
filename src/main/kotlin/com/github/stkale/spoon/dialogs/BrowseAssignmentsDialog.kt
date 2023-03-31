package com.github.stkale.spoon.dialogs

import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.annotations.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class BrowseAssignmentsDialog : DialogWrapper(true) {

    init {
        title = "Browse Assignments"
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(BorderLayout())

        val label = JLabel("*Assignments show up*")
        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.CENTER)

        return dialogPanel
    }
}
