package com.eesuhn.habittracker.feature.insights.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.ui.theme.CoreIcons
import com.eesuhn.habittracker.core.ui.theme.PreviewTheme
import com.eesuhn.habittracker.feature.insights.R
import com.eesuhn.habittracker.feature.insights.model.TopDayItem
import com.eesuhn.habittracker.feature.insights.ui.InsightsViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TopDays(viewModel: InsightsViewModel, navigateToHabitDetails: (HabitId) -> Unit) {
    val topDays by viewModel.habitTopDays.collectAsState()
}

@Composable
private fun TopDaysTable(
    items: ImmutableList<TopDayItem>,
    onHabitClick: (HabitId) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach {
            TopDaysRow(item = it, onClick = onHabitClick)
        }
    }
}

@Composable
private fun TopDaysRow(
    item: TopDayItem,
    onClick: (HabitId) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .weight(0.50f)
                .padding(start = 4.dp),
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = item.dayLabel,
            modifier = Modifier.weight(0.30f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = item.count.toString(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
        )

        IconButton(onClick = { onClick(item.habitId) }) {
            Icon(
                painter = CoreIcons.ChevronRight,
                contentDescription = stringResource(
                    R.string.insights_tophabits_navigate,
                    item.name
                )
            )
        }
    }
}

@Preview
@ShowkaseComposable(name = "Top days table", group = "Insights")
@Composable
fun PreviewTopDaysTable() {
    val topDays = persistentListOf(
        TopDayItem(
            habitId = 1,
            name = "Short name",
            count = 1567,
            dayLabel = "Monday"
        ),
        TopDayItem(
            habitId = 1,
            name = "Name",
            count = 153,
            dayLabel = "Thursday"
        ),
        TopDayItem(
            habitId = 1,
            name = "Loooong name lorem ipsum dolor sit amet",
            count = 10,
            dayLabel = "Sunday"
        ),
        TopDayItem(
            habitId = 1,
            name = "Meditation",
            count = 9,
            dayLabel = "Wednesday"
        ),
        TopDayItem(
            habitId = 1,
            name = "Workout",
            count = 3,
            dayLabel = "Saturday"
        )
    )

    PreviewTheme {
        TopDaysTable(
            items = topDays,
            onHabitClick = { }
        )
    }
}