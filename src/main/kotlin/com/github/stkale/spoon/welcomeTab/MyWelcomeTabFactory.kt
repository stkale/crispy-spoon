package com.github.stkale.spoon.welcomeTab

import com.github.stkale.spoon.actions.BrowseAssignmentsAction
import com.intellij.ide.DataManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.PresentationFactory
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.wm.WelcomeScreen
import com.intellij.openapi.wm.WelcomeScreenTab
import com.intellij.openapi.wm.WelcomeTabFactory
import com.intellij.openapi.wm.impl.welcomeScreen.TabbedWelcomeScreen.DefaultWelcomeScreenTab
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.util.ui.GridBag
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class MyWelcomeTabFactory : WelcomeTabFactory {
    override fun createWelcomeTabs(ws: WelcomeScreen, parentDisposable: Disposable): MutableList<WelcomeScreenTab> {
        return mutableListOf(object : DefaultWelcomeScreenTab("My Assignments") {
            override fun buildComponent(): JComponent {
                return MyWelcomeTabPane()
            }
        })
    }

    class MyWelcomeTabPane : JBScrollPane() {
        private val cardLayout: CardLayout = CardLayout()
        private val mainPanel = JPanel(cardLayout)

        init {
            mainPanel.add(createEmptyPanel(), "empty")
            setViewportView(mainPanel)

            cardLayout.show(mainPanel, "empty")
        }

        private fun createEmptyPanel(): JPanel {
            val contentPanel = NonOpaquePanel(VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 0, false, false))
                .apply {
                    add(createStartButtonPanel())
                }

            return JPanel().apply {
                layout = GridBagLayout()
                val gridBag = GridBag().setDefaultAnchor(GridBagConstraints.CENTER)
                add(contentPanel, gridBag.nextLine())
            }
        }

        private fun createStartButtonPanel(): JPanel {
            val button = object : JButton("Browse Assignments") {
                override fun isDefaultButton(): Boolean = true
            }

            button.addActionListener { e ->
                val action = ActionManager.getInstance().getAction(BrowseAssignmentsAction.ACTION_ID)
                    ?: error("Unable to find action by ID ${BrowseAssignmentsAction.ACTION_ID}")
                val event = AnActionEvent(
                    null,
                    DataManager.getInstance().getDataContext(this),
                    ActionPlaces.WELCOME_SCREEN,
                    PresentationFactory().getPresentation(action),
                    ActionManager.getInstance(),
                    e.modifiers
                )
                action.actionPerformed(event)
            }

            return NonOpaquePanel(BorderLayout()).apply {
                add(button)
            }
        }
    }
}
