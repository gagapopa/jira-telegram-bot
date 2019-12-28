package com.github.mikesafonov.jira.telegram.service.telegram

import com.github.mikesafonov.jira.telegram.dao.State
import com.github.mikesafonov.jira.telegram.service.jira.JiraApiService
import com.github.mikesafonov.jira.telegram.service.telegram.handlers.AddProjectSubscription
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.mockk.*

class AddProjectSubscriptionSpec : BehaviorSpec({
    val jiraApiService = mockk<JiraApiService>()
    val telegramClient = mockk<TelegramClient>()

    Given("/add_project_subscription telegram command handler") {
        val handler = AddProjectSubscription(jiraApiService, telegramClient)

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

        When("incoming message contain right command and wrong state") {
            val command: TelegramCommand = mockk {
                every { text } returns "/add_project_subscription"
                every { hasText } returns true
                every { chat } returns mockk {
                    every { state } returns State.WAIT_APPROVE
                }
                every { isInState(State.INIT) } returns false
                every { isMatchText("/add_project_subscription") } returns true
            }
            Then("isHandle returns false") {
                handler.isHandle(command) shouldBe false
            }
        }

        When("incoming message contain right command") {
            val command: TelegramCommand = mockk {
                every { text } returns "/add_project_subscription"
                every { hasText } returns true
                every { chat } returns mockk {
                    every { state } returns State.INIT
                }
                every { isInState(State.INIT) } returns true
                every { isMatchText("/add_project_subscription") } returns true
                every { authorization } returns mockk()
            }
            Then("isHandle returns true") {
                handler.isHandle(command) shouldBe true
            }
        }

        When("incoming message not contain expected arguments") {
            val messageChatId = Gen.long().random().first()
            val command: TelegramCommand = mockk {
                every { chatId } returns messageChatId
                every { text } returns "/add_project_subscription"
            }
            every { telegramClient.sendTextMessage(any(), any()) } just Runs

            Then("returns message with text about command incorrect syntax") {
                handler.handle(
                    command
                ) shouldBe State.INIT

                verify {
                    telegramClient.sendTextMessage(
                        messageChatId,
                        "Wrong command syntax\n Should be: /add_project_subscription <projectName>"
                    )
                }

                handler.handle(
                    mockk {
                        every { text } returns "/add_project_subscription myProject"
                        every { chatId } returns messageChatId
                    }) shouldBe State.INIT
            }
        }

        When("project from incoming message not found") {
            val messageChatId = Gen.long().random().first()
            val command: TelegramCommand = mockk {
                every { chatId } returns messageChatId
                every { text } returns "/add_project_subscription myProject"
            }
            every { telegramClient.sendTextMessage(any(), any()) } just Runs

            Then("returns message with text project not found") {

            }
        }
    }
})
