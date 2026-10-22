package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.TemplateRepository
import com.example.data.repository.VideoTemplate
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TemplatesScreen(
    onSelectTemplate: (VideoTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        item {
            SectionHeader(
                title = "Viral Short-Form Templates",
                subtitle = "Pre-timed cuts, speed ramps, and transition hooks optimized for TikTok & Reels",
                badgeText = "Trending"
            )
        }

        items(TemplateRepository.templates) { template ->
            TemplateCard(
                template = template,
                onUseTemplate = { onSelectTemplate(template) }
            )
        }
    }
}

@Composable
private fun TemplateCard(
    template: VideoTemplate,
    onUseTemplate: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("template_card_${template.id}"),
        backgroundColor = DarkSurfaceElevated,
        borderColor = NeonViolet.copy(alpha = 0.35f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonViolet.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = template.platformTag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonViolet
                    )
                }

                Text(
                    text = "${template.shotsCount} Shots • ${template.durationText}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = template.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = template.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hook idea
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🎣 Hook: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                Text(text = template.hookIdea, fontSize = 11.sp, color = TextPrimary)
            }

            // Recommended audio
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${template.recommendedMusic.name} (${template.recommendedMusic.bpm} BPM)", fontSize = 11.sp, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(14.dp))

            GradientButton(
                text = "Use This Template in Editor",
                onClick = onUseTemplate,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.MovieCreation,
                gradientColors = listOf(NeonViolet, ElectricCyan)
            )
        }
    }
}
