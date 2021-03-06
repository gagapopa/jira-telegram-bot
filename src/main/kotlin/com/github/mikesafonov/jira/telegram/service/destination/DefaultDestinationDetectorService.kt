package com.github.mikesafonov.jira.telegram.service.destination

import com.github.mikesafonov.jira.telegram.config.ApplicationProperties
import com.github.mikesafonov.jira.telegram.dto.*

/**
 * Default implementation of [DestinationDetectorService]
 * @author Mike Safonov
 */
class DefaultDestinationDetectorService(private val applicationProperties: ApplicationProperties) :
    BaseDestinationDetectorService() {

    /**
     * Find jira logins from [event] to send a telegram message
     */
    override fun findDestinations(event: Event): List<String> {
        val notificationProperties = applicationProperties.notification
        return if (notificationProperties.sendToMe) {
            if (event.issue != null && event.issueEventTypeName != null) {
                allIssueUsers(event.issue).plus(getMentionsFromComment(event.comment))
                    .distinct()
            } else {
                emptyList()
            }
        } else {
            requiredDestinations(event)
        }
    }


    /**
     * Collect **creator**, **reporter** and **assignee** logins from [issue] to list, ignoring *null* values and filtered
     * by [initiator] login if present.
     * @param issue jira issue
     * @param initiator user who fired this issues event
     * @return list of logins
     */
    override fun allIssueUsersWithoutInitiator(issue: Issue, initiator: User?, comment: Comment?): List<String> {
        return if (initiator == null) {
            allIssueUsers(issue).plus(getMentionsFromComment(comment))
                .distinct()
        } else {
            allIssueUsers(issue).plus(getMentionsFromComment(comment))
                .filter { it != initiator.name }
                .distinct()
        }
    }
}
