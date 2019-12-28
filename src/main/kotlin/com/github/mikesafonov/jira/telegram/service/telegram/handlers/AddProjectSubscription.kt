package com.github.mikesafonov.jira.telegram.service.telegram.handlers

import com.github.mikesafonov.jira.telegram.config.conditional.ConditionalOnJiraOAuth
import com.github.mikesafonov.jira.telegram.dao.State
import com.github.mikesafonov.jira.telegram.service.jira.JiraApiService
import com.github.mikesafonov.jira.telegram.service.telegram.TelegramClient
import com.github.mikesafonov.jira.telegram.service.telegram.TelegramCommand
import org.springframework.stereotype.Service

@Service
@ConditionalOnJiraOAuth
class AddProjectSubscription(
    private val jiraApiService: JiraApiService,
    telegramClient: TelegramClient
) : BaseCommandHandler(telegramClient) {

    private val commandPrefix = "/add_project_subscription"

    override fun isHandle(command: TelegramCommand): Boolean {
        return command.isInState(State.INIT) && command.isMatchText(commandPrefix)
    }

    override fun handle(command: TelegramCommand): State {
        val id = command.chatId
        val commandArgs = command.text!!.split(" ")
        if (commandArgs.size < 2) {
            telegramClient.sendTextMessage(
                id,
                "Wrong command syntax\n Should be: $commandPrefix <projectName>"
            )
        }

        return State.INIT
    }
}