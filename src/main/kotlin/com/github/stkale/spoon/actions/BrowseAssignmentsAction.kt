package com.github.stkale.spoon.actions

import com.github.stkale.spoon.dialogs.BrowseAssignmentsDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.annotations.NotNull

class BrowseAssignmentsAction : AnAction() {

    override fun actionPerformed(@NotNull e: AnActionEvent) {
        if (BrowseAssignmentsDialog().showAndGet()) {
            // user pressed OK
        }
    }

    companion object {
        const val ACTION_ID = "com.github.stkale.spoon.actions.BrowseAssignmentsAction"
    }
}
