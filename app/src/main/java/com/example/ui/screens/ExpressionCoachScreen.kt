package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpressionGuide
import com.example.data.model.ExpressionMood
import com.example.data.repository.ExpressionRepository
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.ScoreBadge
import com.example.ui.components.SectionHeader
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
fun ExpressionCoachScreen(
    uiState: PoseFlowUiState,
    onMoodSelected: (ExpressionMood) -> Unit,
    onPhotoSelected: (Uri) -> Unit,
    onAnalyzeSample: () -> Unit,
    onStartPractice: () -> Unit,
    onStopPractice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onPhotoSelected(uri)
        }
    }

    val guide = uiState.expressionGuide

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 36.dp)
    ) {
        // Mood Selector Carousel with all 10 Moods
        item {
            SectionHeader(
                title = "Select Desired Mood",
                subtitle = "Choose from 10 expression archetypes with calibrated biomechanics",
                badgeText = uiState.selectedMood.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ExpressionMood.values()) { mood ->
                    MoodTile(
                        mood = mood,
                        isSelected = uiState.selectedMood == mood,
                        onClick = { onMoodSelected(mood) }
                    )
                }
            }
        }

        // Live Facial Mirror / Reticle or Practice Mode Active View
        item {
            if (uiState.isPracticingExpressionSequence) {
                PracticeSequenceActiveCard(
                    uiState = uiState,
                    onStop = onStopPractice
                )
            } else {
                FacialAlignmentMirrorCard(
                    guide = guide,
                    onUploadPhoto = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onAnalyzeSample = onAnalyzeSample,
                    onStartPracticeMode = onStartPractice
                )
            }
        }

        // Gentle AI Expression Feedback (if analyzed)
        if (uiState.isAnalyzingExpression || uiState.expressionAnalysisResult != null) {
            item {
                GentleExpressionFeedbackCard(
                    isLoading = uiState.isAnalyzingExpression,
                    result = uiState.expressionAnalysisResult,
                    uploadedBitmap = uiState.uploadedExpressionImage
                )
            }
        }

        // Detailed Biomechanics Guidance Breakdown
        item {
            SectionHeader(
                title = "Facial Biomechanics Guidance",
                subtitle = "Personalized settings for ${uiState.selectedMood.title}",
                badgeText = "Micro-coaching"
            )
            Spacer(modifier = Modifier.height(10.dp))
            DetailedBiomechanicsCard(guide = guide)
        }

        // Positive Encouragement Note
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DarkSurface,
                borderColor = HotPink.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "💖", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "COACH'S ENCOURAGEMENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HotPink,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = guide.encouragementNote,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodTile(
    mood: ExpressionMood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) HotPink else DarkSurfaceBorder
    val bg = if (isSelected) {
        Brush.verticalGradient(listOf(HotPink.copy(alpha = 0.25f), DarkSurfaceElevated))
    } else {
        Brush.verticalGradient(listOf(DarkSurfaceElevated, DarkSurfaceElevated))
    }

    Box(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            Text(text = mood.emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = mood.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isSelected) HotPink else TextPrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = mood.vibeTag,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FacialAlignmentMirrorCard(
    guide: ExpressionGuide,
    onUploadPhoto: () -> Unit,
    onAnalyzeSample: () -> Unit,
    onStartPracticeMode: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("facial_mirror_card"),
        borderColor = HotPink.copy(alpha = 0.35f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Live Mirror Box with Reticles
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, HotPink.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Oval Face Reticle
                Box(
                    modifier = Modifier
                        .size(width = 125.dp, height = 165.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .border(
                            2.dp,
                            Brush.verticalGradient(listOf(HotPink, ElectricCyan)),
                            RoundedCornerShape(60.dp)
                        )
                )

                // Eye Alignment Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(1.dp)
                        .background(ElectricCyan.copy(alpha = 0.5f))
                )

                // Reticle Status Badge
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianBlack.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldGlow))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ALIGNMENT GUIDE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }

                // Smile intensity meter tag
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianBlack.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Smile: ${guide.smileIntensity}/10",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = HotPink
                    )
                }

                // Overlay prompt
                Text(
                    text = "Center eyes on crosshair • ${guide.headTilt}",
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianBlack.copy(alpha = 0.85f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Practice Mode + Upload / Sample
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GradientButton(
                    text = "Practice Countdown",
                    onClick = onStartPracticeMode,
                    modifier = Modifier.weight(1.2f),
                    icon = Icons.Default.Timer,
                    gradientColors = listOf(HotPink, NeonViolet),
                    textColor = Color.White
                )

                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onUploadPhoto),
                    backgroundColor = DarkSurface,
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
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = HotPink,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Analyze Photo",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "⚡ Tap for instant sample expression evaluation",
                fontSize = 11.sp,
                color = HotPink,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable(onClick = onAnalyzeSample)
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun PracticeSequenceActiveCard(
    uiState: PoseFlowUiState,
    onStop: () -> Unit
) {
    val step = ExpressionRepository.defaultPracticeSequence.getOrNull(uiState.currentPracticeStepIndex)
        ?: ExpressionRepository.defaultPracticeSequence.first()

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("practice_sequence_card"),
        borderColor = HotPink,
        backgroundColor = DarkSurfaceElevated
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VIDEO EXPRESSION PRACTICE MODE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = HotPink,
                    letterSpacing = 1.sp
                )
                IconButton(onClick = onStop, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop", tint = HotPink)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large Countdown Indicator
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(HotPink.copy(alpha = 0.15f))
                    .border(2.dp, HotPink, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.practiceCountdownSeconds > 0) "${uiState.practiceCountdownSeconds}" else "POSE!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = if (uiState.practiceCountdownSeconds > 0) HotPink else EmeraldGlow
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Step ${step.stepIndex} of ${ExpressionRepository.defaultPracticeSequence.size}: ${step.mood.title}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = step.audioCuePrompt,
                fontSize = 13.sp,
                color = ElectricCyan,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { (uiState.currentPracticeStepIndex + 1).toFloat() / ExpressionRepository.defaultPracticeSequence.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = HotPink,
                trackColor = DarkSurfaceBorder
            )
        }
    }
}

@Composable
private fun DetailedBiomechanicsCard(guide: ExpressionGuide) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DarkSurfaceElevated,
        borderColor = DarkSurfaceBorder
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BiomechanicItem(
                emoji = "👁️",
                label = "Eye Direction",
                detail = guide.eyeDirection
            )
            BiomechanicItem(
                emoji = "〰️",
                label = "Eyebrow Position",
                detail = guide.eyebrowPosition
            )
            BiomechanicItem(
                emoji = "👄",
                label = "Lips Guidance",
                detail = guide.lipsGuidance
            )
            BiomechanicItem(
                emoji = "✨",
                label = "Smile Intensity",
                detail = "Level ${guide.smileIntensity} of 10 — calibrated for ${guide.mood.title}"
            )
            BiomechanicItem(
                emoji = "📐",
                label = "Head Tilt",
                detail = guide.headTilt
            )
            BiomechanicItem(
                emoji = "🧍",
                label = "Posture Guidance",
                detail = guide.postureGuidance
            )
            BiomechanicItem(
                emoji = "💨",
                label = "Breathing Tip",
                detail = guide.breathingTip
            )
        }
    }
}

@Composable
private fun BiomechanicItem(emoji: String, label: String, detail: String) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
        Text(
            text = detail,
            fontSize = 13.sp,
            color = TextPrimary,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 2.dp, start = 20.dp)
        )
    }
}

@Composable
private fun GentleExpressionFeedbackCard(
    isLoading: Boolean,
    result: com.example.data.model.ExpressionAnalysisResult?,
    uploadedBitmap: android.graphics.Bitmap?
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("gentle_expression_feedback_card"),
        borderColor = if (isLoading) HotPink else EmeraldGlow.copy(alpha = 0.5f),
        backgroundColor = DarkSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (isLoading) HotPink else EmeraldGlow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isLoading) "ANALYZING FACIAL EXPRESSION..." else "POSITIVE COACHING FEEDBACK",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLoading) HotPink else EmeraldGlow
                )
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = HotPink, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Evaluating micro-expressions and eye warmth...", fontSize = 12.sp, color = TextSecondary)
                }
            } else if (result != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ScoreBadge(score = result.confidenceScore, label = "Confidence", modifier = Modifier.weight(1f), accentColor = HotPink)
                    ScoreBadge(score = result.naturalnessScore, label = "Naturalness", modifier = Modifier.weight(1f), accentColor = EmeraldGlow)
                    ScoreBadge(score = result.eyeEngagementScore, label = "Eye Focus", modifier = Modifier.weight(1f), accentColor = ElectricCyan)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Positive Observations:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow
                )

                for (obs in result.positiveObservations) {
                    Row(modifier = Modifier.padding(top = 2.dp), verticalAlignment = Alignment.Top) {
                        Text(text = "✓", color = EmeraldGlow, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = obs, fontSize = 12.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Gentle Suggestions:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = HotPink
                )

                for (sug in result.gentleSuggestions) {
                    Row(modifier = Modifier.padding(top = 2.dp), verticalAlignment = Alignment.Top) {
                        Text(text = "💡", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = sug, fontSize = 12.sp, color = TextPrimary)
                    }
                }
            }
        }
    }
}
