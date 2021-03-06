package com.github.mikesafonov.jira.telegram.generators

import com.github.mikesafonov.jira.telegram.dto.Priority
import io.kotlintest.properties.Gen

/**
 * @author Mike Safonov
 */
class PriorityGen : Gen<Priority> {
    companion object {
        fun generateDefault(): Priority {
            return PriorityGen().generateOne()
        }

        fun empty(): Priority? {
            return null
        }
    }

    override fun constants(): Iterable<Priority> {
        return emptyList()
    }

    override fun random(): Sequence<Priority> {
        return generateSequence {
            generateOne()
        }
    }

    fun generateOne(name : String = Gen.string().random().first(),
                    iconUrl : String = Gen.string().random().first()) : Priority{
        return Priority(name, iconUrl)
    }

}