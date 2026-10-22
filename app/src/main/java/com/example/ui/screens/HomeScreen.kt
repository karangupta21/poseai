package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ExpressionMood
import com.example.data.model.PoseCategory
import com.example.data.model.PoseReference
import com.example.data.repository.PoseRepository
import com.example.data.repository.TemplateRepository
import com.example.ui.AppTab
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.PoseWireframeCanvas
import com.example.ui.components.ScoreBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    uiState: PoseFlowUiState,
    onNavigateTab: (AppTab) -> Unit,
    onSelectPose: (PoseReference) -> Unit,
    onSelectMood: (ExpressionMood) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // Hero Studio Banner
        item {
            HeroStudioCard(
                onStartPoseClick = { onNavigateTab(AppTab.POSES) },
                onVideoEditorClick = { onNavigateTab(AppTab.EDITOR) }
            )
        }

        // Quick Creator Stats Row
        item {
            CreatorMetricsRow()
        }

        // Core AI Modes Grid
        item {
            SectionHeader(
                title = "AI Creative Studios",
                subtitle = "Discover flattering poses, perfect expressions, and edit videos",
                badgeText = "Core AI"
            )
            Spacer(modifier = Modifier.height(10.dp))
            CoreModesGrid(onNavigateTab = onNavigateTab)
        }

        // Featured Pose of the Day
        item {
            val featuredPose = PoseRepository.allPoses.firstOrNull() ?: return@item
            SectionHeader(
                title = "Pose of the Day",
                subtitle = "Handpicked for effortless silhouette and flattering angles",
                actionText = "Explore All",
                onActionClick = { onNavigateTab(AppTab.POSES) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeaturedPoseCard(
                pose = featuredPose,
                onClick = {
                    onSelectPose(featuredPose)
                    onNavigateTab(AppTab.POSES)
                }
            )
        }

        // Daily Expression Coaching Challenge
        item {
            SectionHeader(
                title = "Facial Expression Coach",
                subtitle = "Master the Hollywood 'Squinch' and natural eye crinkles",
                actionText = "Open Mirror",
                onActionClick = {
                    onSelectMood(ExpressionMood.CINEMATIC_DRAMATIC)
                    onNavigateTab(AppTab.EXPRESSIONS)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ExpressionChallengeCard(
                onClick = {
                    onSelectMood(ExpressionMood.CINEMATIC_DRAMATIC)
                    onNavigateTab(AppTab.EXPRESSIONS)
                }
            )
        }

        // Trending Viral Video Templates
        item {
            SectionHeader(
                title = "Viral Video Templates",
                subtitle = "Beat-synced transitions and hook pacing designed for TikTok & Reels",
                actionText = "View All",
                onActionClick = { onNavigateTab(AppTab.TEMPLATES) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(TemplateRepository.templates) { template ->
                    GlassCard(
                        modifier = Modifier
                            .width(220.dp)
                            .clickable {
                                onNavigateTab(AppTab.TEMPLATES)
                            },
                        backgroundColor = DarkSurfaceElevated
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(listOf(DarkSurfaceBorder, DarkSurface))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = template.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${template.shotsCount} cuts • ${template.durationText}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = template.platformTag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NeonViolet
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStudioCard(
    onStartPoseClick: () -> Unit,
    onVideoEditorClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_studio_card"),
        cornerRadius = 20.dp,
        borderColor = ElectricCyan.copy(alpha = 0.35f)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.poseflow_hero),
                contentDescription = "PoseFlow Studio Ambient Scene",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                ObsidianBlack.copy(alpha = 0.85f),
                                ObsidianBlack
                            ),
                            startY = 60f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .align(Alignment.BottomStart)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "POSEFLOW AI STUDIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricCyan,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Look Flattering.\nShoot Cinematic.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GradientButton(
                        text = "Explore Poses",
                        onClick = onStartPoseClick,
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AccessibilityNew
                    )

                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onVideoEditorClick),
                        backgroundColor = DarkSurfaceElevated.copy(alpha = 0.9f),
                        borderColor = NeonViolet.copy(alpha = 0.4f),
                        cornerRadius = 14.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MovieCreation,
                                contentDescription = null,
                                tint = NeonViolet,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AI Video Edit",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreatorMetricsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ScoreBadge(
            score = 94,
            label = "Photogenic Index",
            modifier = Modifier.weight(1f),
            accentColor = ElectricCyan
        )
        ScoreBadge(
            score = 88,
            label = "Posture Accuracy",
            modifier = Modifier.weight(1f),
            accentColor = NeonViolet
        )
        ScoreBadge(
            score = 96,
            label = "Expression Vitality",
            modifier = Modifier.weight(1f),
            accentColor = HotPink
        )
    }
}

@Composable
private fun CoreModesGrid(onNavigateTab: (AppTab) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeTile(
                title = "Pose Studio",
                subtitle = "Guides & Real-time Overlays",
                icon = Icons.Default.AccessibilityNew,
                accentColor = ElectricCyan,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AppTab.POSES) }
            )
            ModeTile(
                title = "Expression Coach",
                subtitle = "Squinch & Smile Alignment",
                icon = Icons.Default.Face,
                accentColor = HotPink,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AppTab.EXPRESSIONS) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeTile(
                title = "Video Editor",
                subtitle = "Trim, Speed & Color Grade",
                icon = Icons.Default.MovieCreation,
                accentColor = NeonViolet,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AppTab.EDITOR) }
            )
            ModeTile(
                title = "Creator Assistant",
                subtitle = "Natural Language Storyboards",
                icon = Icons.Default.AutoAwesome,
                accentColor = Color(0xFFFBBF24),
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AppTab.ASSISTANT) }
            )
        }
    }
}

@Composable
private fun ModeTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.clickable(onClick = onClick),
        backgroundColor = DarkSurfaceElevated,
        borderColor = accentColor.copy(alpha = 0.25f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun FeaturedPoseCard(
    pose: PoseReference,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = ElectricCyan.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini wireframe visual preview
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(1.dp, ElectricCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(6.dp)
            ) {
                PoseWireframeCanvas(
                    wireframeType = pose.wireframeType,
                    alpha = 0.95f,
                    showGrid = false
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pose.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${pose.difficultyLevel}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pose.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pose.subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tap to view instructions & camera guide →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ElectricCyan
                )
            }
        }
    }
}

@Composable
private fun ExpressionChallengeCard(onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = HotPink.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(HotPink.copy(alpha = 0.15f))
                    .border(1.dp, HotPink.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎬", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "THE CINEMATIC SMOLDER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = HotPink,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "The 'Squinch' Eye & Jaw Trick",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Raise lower eyelids 15% and press tongue to palate to define your facial structure instantly.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = HotPink,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
