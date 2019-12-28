package com.github.mikesafonov.jira.telegram.service.telegram.handlers

import com.github.mikesafonov.jira.telegram.config.conditional.ConditionalOnJiraOAuth
import com.github.mikesafonov.jira.telegram.dao.State
import com.github.mikesafonov.jira.telegram.service.jira.JiraApiService
import com.github.mikesafonov.jira.telegram.service.telegram.TelegramClient
import com.github.mikesafonov.jira.telegram.service.telegram.TelegramCommand
import org.springframework.stereotype.Service

@Service
@ConditionalOnJiraOAuth
class CreateWebhookTelegramCommandHandler(
    private val jiraApiService: JiraApiService,
    telegramClient: TelegramClient
    ): BaseCommandHandler(telegramClient) {

    override fun isHandle(command: TelegramCommand): Boolean {
        return command.isInState(State.INIT) && command.isMatchText("/add_project_subscription")
    }

    override fun handle(command: TelegramCommand): State {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}