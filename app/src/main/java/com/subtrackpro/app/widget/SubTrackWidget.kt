package com.subtrackpro.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.FontWeight
import androidx.glance.color.ColorProvider
import androidx.compose.ui.graphics.Color
import com.subtrackpro.app.MainActivity
import com.subtrackpro.app.data.local.SubscriptionEntity
import com.subtrackpro.app.utils.DateUtils

object SubTrackWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = WidgetDataLoader.load(context)
        provideContent { WidgetContent(data) }
    }

    @Composable
    private fun WidgetContent(subs: List<SubscriptionEntity>) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFF6750A4)))
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("SubTrack", style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontWeight = FontWeight.Bold))
                Spacer(GlanceModifier.defaultWeight())
                Text("⟳", style = TextStyle(color = ColorProvider(Color.White)),
                    modifier = GlanceModifier.clickable(actionRunCallback<RefreshAction>()))
            }
            Spacer(GlanceModifier.height(8.dp))
            if (subs.isEmpty()) {
                Text("No upcoming", style = TextStyle(color = ColorProvider(Color.White)))
            } else {
                LazyColumn {
                    items(subs) { s ->
                        Column(modifier = GlanceModifier.padding(vertical = 4.dp)) {
                            Text(s.name, style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontWeight = FontWeight.Medium))
                            Text("${DateUtils.format(s.nextBillingDate)} • ₹${"%.2f".format(s.price)}",
                                style = TextStyle(color = ColorProvider(Color(0xFFE0E0E0))))
                        }
                    }
                }
            }
        }
    }
}
