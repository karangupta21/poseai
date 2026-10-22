package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CameraCoachMode
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.PoseWireframeCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CameraCoachScreen(
    uiState: PoseFlowUiState,
    onSetMode: (CameraCoachMode) -> Unit,
    onStartTimer: (Int) -> Unit,
    onSetWireframeAlpha: (Float) -> Unit,
    onUsePoseForVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGridLines by remember { mutableStateOf(true) }
    var showWireframe by remember { mutableStateOf(true) }
    var wireframeAlpha by remember { mutableFloatStateOf(0.75f) }
    var selectedTimerSec by remember { mutableStateOf(3) }

    val currentPose = uiState.selectedPose
    val activeMode = uiState.cameraCoachMode

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("camera_coach_screen")
    ) {
        // Guide-only preview: CameraX preview and photo capture are not implemented yet
        Box(modifier = Modifier.fillMaxSize()) {
            if (showWireframe) {
                PoseWireframeCanvas(
                    wireframeType = currentPose.wireframeType,
                    alpha = wireframeAlpha,
                    showGrid = showGridLines,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(text = "Camera preview and photo capture are not connected yet", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.Center).padding(horizontal = 28.dp).background(DarkSurface.copy(alpha = 0.9f), RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 8.dp))

            // Countdown overlay only; it does not take a photo.
            if (uiState.cameraTimerCountdown != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(HotPink.copy(alpha = 0.25f))
                            .border(3.dp, HotPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.cameraTimerCountdown > 0) "${uiState.cameraTimerCountdown}" else "TIMER DONE",
                            fontSize = if (uiState.cameraTimerCountdown > 0) 54.sp else 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Top Navigation & Controls Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldGlow)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "POSE COACH PREVIEW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricCyan,
                        letterSpacing = 1.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { showGridLines = !showGridLines },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurface.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = "Grid",
                            tint = if (showGridLines) ElectricCyan else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { showWireframe = !showWireframe },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurface.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = if (showWireframe) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Guide",
                            tint = if (showWireframe) ElectricCyan else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(text = "DEMO", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mode Selector Carousel (Solo, Couples, Fashion, Travel, Fitness, Portraits)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CameraCoachMode.values()) { mode ->
                    val isSelected = activeMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ElectricCyan.copy(alpha = 0.2f) else DarkSurface.copy(alpha = 0.75f))
                            .border(1.dp, if (isSelected) ElectricCyan else DarkSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { onSetMode(mode) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = mode.icon, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = mode.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ElectricCyan else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Floating Real-Time Guidance Badges (Height, Angle, Lighting, Framing)
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, end = 80.dp)
                .fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CoachTipBadge(
                emoji = "📐",
                title = "Camera Height & Angle",
                content = uiState.cameraShotAngleTip
            )
            CoachTipBadge(
                emoji = "💡",
                title = "Lighting Direction",
                content = uiState.cameraLightingTip
            )
            CoachTipBadge(
                emoji = "🎯",
                title = "Rule of Thirds Framing",
                content = uiState.cameraFramingTip
            )
            CoachTipBadge(
                emoji = "👤",
                title = "Current Pose Target",
                content = "${currentPose.title}: ${currentPose.handAndLegPlacement}"
            )
        }

        // Bottom Controls Deck
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurface.copy(alpha = 0.92f))
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            // Opacity slider & timer buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Timer:", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    listOf(3, 5, 10).forEach { sec ->
                        val isSel = selectedTimerSec == sec
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) ElectricCyan.copy(alpha = 0.25f) else DarkSurfaceElevated)
                                .border(1.dp, if (isSel) ElectricCyan else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedTimerSec = sec }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${sec}s",
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) ElectricCyan else TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                // Opacity Slider
                Row(
                    modifier = Modifier.width(130.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Guide", fontSize = 10.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Slider(
                        value = wireframeAlpha,
                        onValueChange = {
                            wireframeAlpha = it
                            onSetWireframeAlpha(it)
                        },
                        valueRange = 0.2f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Shutter / Video Record Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Countdown only; no photo is captured
                GradientButton(
                    text = "Start Timer (${selectedTimerSec}s)",
                    onClick = {
                        onStartTimer(selectedTimerSec)
                    },
                    modifier = Modifier.weight(1.2f),
                    icon = Icons.Default.CameraAlt,
                    gradientColors = listOf(ElectricCyan, NeonViolet)
                )

                // Sequence Recording for Video
                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onUsePoseForVideo),
                    backgroundColor = DarkSurfaceElevated,
                    borderColor = HotPink.copy(alpha = 0.4f),
                    cornerRadius = 14.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = HotPink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Record Reel",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoachTipBadge(
    emoji: String,
    title: String,
    content: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurface.copy(alpha = 0.85f))
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            }
            Text(text = content, fontSize = 11.sp, color = TextPrimary, lineHeight = 14.sp)
        }
    }
}
