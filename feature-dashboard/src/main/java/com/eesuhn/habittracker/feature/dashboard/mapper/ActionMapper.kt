package com.eesuhn.habittracker.feature.dashboard.mapper

import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.ActionHistory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import com.eesuhn.habittracker.core.database.entity.Action as ActionEntity

private const val RECENT_ACTIONS_PER_HABIT = 30 // Max number of days of any dashboard configs

fun actionsToRecentDays(actions: List<ActionEntity>): ImmutableList<Action> {
    val lastDay = LocalDate.now()

    val sortedActions = actions
        .sortedByDescending { action -> action.timestamp }
        .take(RECENT_ACTIONS_PER_HABIT)
    return (RECENT_ACTIONS_PER_HABIT - 1 downTo 0).map { i ->
        val targetDate = lastDay.minusDays(i.toLong())
        val actionOnDay = sortedActions.find { action ->
            val actionDate = LocalDateTime
                .ofInstant(action.timestamp, ZoneId.systemDefault())
                .toLocalDate()

            actionDate == targetDate
        }

        Action(
            id = actionOnDay?.id ?: 0,
            toggled = actionOnDay != null,
            actionOnDay?.timestamp
        )
    }.toImmutableList()
}

fun actionsToHistory(actions: List<ActionEntity>): ActionHistory {
    if (actions.isEmpty()) {
        return ActionHistory.Clean
    }

    val sortedActions = actions.sortedByDescending { action -> action.timestamp }
    val firstActionDate = LocalDateTime
        .ofInstant(sortedActions.first().timestamp, ZoneId.systemDefault())
        .toLocalDate()

    if (firstActionDate == LocalDate.now()) {
        // It's a streak
        var days = 0
        var previousDate = LocalDate.now().plusDays(1)
        for (action in sortedActions) {
            val actionDate = LocalDateTime
                .ofInstant(action.timestamp, ZoneId.systemDefault())
                .toLocalDate()
            if (previousDate.minusDays(1) == actionDate) {
                days++
                previousDate = previousDate.minusDays(1)
            } else {
                break
            }
        }
        return ActionHistory.Streak(days)
    } else if (firstActionDate.isBefore(LocalDate.now())) {
        // It's a missed day streak
        var days = 0
        var previousDate = LocalDate.now()

        while (previousDate != firstActionDate) {
            days++
            previousDate = previousDate.minusDays(1)
        }

        return ActionHistory.MissedDays(days)
    } else {
        // firstActionDate is in the future. This should never happen
        return ActionHistory.Clean
    }
}