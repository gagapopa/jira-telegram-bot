package com.github.mikesafonov.jira.telegram.service.telegram

import com.github.mikesafonov.jira.telegram.dao.State
import com.github.mikesafonov.jira.telegram.service.jira.JiraApiService
import com.github.mikesafonov.jira.telegram.service.telegram.handlers.AddUserTelegramCommandHandler
import com.github.mikesafonov.jira.telegram.service.telegram.handlers.CreateWebhookTelegramCommandHandler
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.mockk.every
import io.mockk.mockk

class CreateWebhookTelegramCommandHandlerSpec: BehaviorSpec({
    val jiraApiService = mockk<JiraApiService>()
    val telegramClient = mockk<TelegramClient>()

    Given("/add_project_subscription telegram command handler") {
        val handler = CreateWebhookTelegramCommandHandler(jiraApiService, telegramClient)

        When("incoming message contain wrong command") {
            val command: TelegramCommand = mockk {
                every { text } returns Gen.string().random().first()
                every { hasText } returns true
                every { chat } returns mockk {
                    every { state } returns State.INIT
                }
                every { isInState(State.INIT) } returns true
                every { isMatchText(any()) } returns false
            }

            Then("isHandle returns false") {
                handler.isHandle(command) shouldBe false
            }
        }
    }

})
