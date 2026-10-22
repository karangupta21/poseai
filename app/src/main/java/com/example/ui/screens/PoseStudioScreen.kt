package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PoseCategory
import com.example.data.model.PoseReference
import com.example.data.repository.PoseRepository
import com.example.ui.AppTab
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.PoseWireframeCanvas
import com.example.ui.components.ScoreBadge
import com.example.ui.components.SectionHeader
import com.example.ui.components.TagChip
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
fun PoseStudioScreen(
    uiState: PoseFlowUiState,
    onCategorySelected: (PoseCategory) -> Unit,
    onSelectPose: (PoseReference) -> Unit,
    onShowAnotherPose: () -> Unit,
    onToggleSavePose: (String) -> Unit,
    onOpenPoseSequence: () -> Unit,
    onClosePoseSequence: () -> Unit,
    onUsePoseForVideo: (PoseReference) -> Unit,
    onPhotoSelected: (Uri) -> Unit,
    onAnalyzeSample: () -> Unit,
    onClearAnalysis: () -> Unit,
    onOpenLiveCameraCoach: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onPhotoSelected(uri)
        }
    }

    val currentPose = uiState.selectedPose
    val isSaved = uiState.savedPoseIds.contains(currentPose.id)

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 36.dp)
        ) {
            // Category Selector Chips Row
            item {
                SectionHeader(
                    title = "Photoshoot Category",
                    subtitle = "Select an archetype or upload your photo for instant pose recommendations",
                    badgeText = "AI Generator"
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PoseCategory.values()) { category ->
                        TagChip(
                            text = category.displayName,
                            isSelected = uiState.poseFilterState.selectedCategory == category,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }

            // Quick Photo Upload & Live Camera Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        borderColor = ElectricCyan.copy(alpha = 0.35f),
                        backgroundColor = DarkSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Analyze My Photo",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onOpenLiveCameraCoach),
                        borderColor = NeonViolet.copy(alpha = 0.35f),
                        backgroundColor = DarkSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = NeonViolet,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Open Camera Coach",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // AI Pose Evaluation Card (if user uploaded or ran sample analysis)
            if (uiState.isAnalyzingPose || uiState.poseAnalysisResult != null) {
                item {
                    PoseAnalysisCard(
                        isLoading = uiState.isAnalyzingPose,
                        result = uiState.poseAnalysisResult,
                        uploadedBitmap = uiState.uploadedPoseImage,
                        onClear = onClearAnalysis
                    )
                }
            }

            // ==========================================
            // PRIMARY GENERATED POSE CARD (DETAILED VIEW)
            // ==========================================
            item {
                PoseDetailedCard(
                    pose = currentPose,
                    isSaved = isSaved,
                    onToggleSave = { onToggleSavePose(currentPose.id) },
                    onShowAnotherPose = onShowAnotherPose,
                    onGenerateSequence = onOpenPoseSequence,
                    onUseForVideo = { onUsePoseForVideo(currentPose) }
                )
            }

            // Secondary: Browse other poses in this category
            item {
                SectionHeader(
                    title = "More ${uiState.poseFilterState.selectedCategory.displayName}",
                    subtitle = "Tap any pose below to switch primary focus",
                    badgeText = "Library"
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(PoseRepository.getPosesByCategory(uiState.poseFilterState.selectedCategory)) { pose ->
                        PoseThumbCard(
                            pose = pose,
                            isSelected = pose.id == currentPose.id,
                            onClick = { onSelectPose(pose) }
                        )
                    }
                }
            }
        }

        // ==========================================
        // POSE SEQUENCE DIALOG / MODAL
        // ==========================================
        if (uiState.showPoseSequenceDialog) {
            PoseSequenceDialog(
                pose = currentPose,
                onClose = onClosePoseSequence,
                onUseForVideo = {
                    onClosePoseSequence()
                    onUsePoseForVideo(currentPose)
                }
            )
        }
    }
}

@Composable
private fun PoseDetailedCard(
    pose: PoseReference,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onShowAnotherPose: () -> Unit,
    onGenerateSequence: () -> Unit,
    onUseForVideo: () -> Unit
) {
    var showWireframeOverlay by remember { mutableStateOf(true) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pose_detailed_card"),
        borderColor = ElectricCyan.copy(alpha = 0.4f),
        backgroundColor = DarkSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Title, Category Badge, Save Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = pose.category.displayName.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricCyan,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonViolet.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = pose.difficultyLevel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonViolet
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pose.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = pose.subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }

                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSaved) HotPink.copy(alpha = 0.2f) else DarkSurface)
                        .border(1.dp, if (isSaved) HotPink else DarkSurfaceBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Pose",
                        tint = if (isSaved) HotPink else TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Visual Reference Illustration Area (Labeled vector wireframe & camera alignment)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, ElectricCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                if (showWireframeOverlay) {
                    PoseWireframeCanvas(
                        wireframeType = pose.wireframeType,
                        alpha = 1f,
                        showGrid = true
                    )
                }

                // Clearly labeled visual illustration banner
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ObsidianBlack.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📐 VISUAL POSE SKELETON GUIDE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                }

                // Toggle Wireframe visibility
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ObsidianBlack.copy(alpha = 0.8f))
                        .clickable { showWireframeOverlay = !showWireframeOverlay }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showWireframeOverlay) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showWireframeOverlay) "Guide On" else "Guide Off",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // DETAILED POSE BREAKDOWN (REQUIRED SPEC)
            // ==========================================

            PoseDetailRow(
                emoji = "⚖️",
                title = "Body Position & Weight Distribution",
                content = pose.bodyPositionAndWeightDistribution
            )

            PoseDetailRow(
                emoji = "🦵",
                title = "Hand & Leg Placement",
                content = pose.handAndLegPlacement
            )

            PoseDetailRow(
                emoji = "👤",
                title = "Head Angle & Gaze Direction",
                content = pose.headAngleAndGaze
            )

            PoseDetailRow(
                emoji = "📐",
                title = "Camera Angle & Distance",
                content = pose.cameraAngleAndDistance
            )

            PoseDetailRow(
                emoji = "✨",
                title = "Facial Expression Suggestion",
                content = pose.facialExpressionSuggestion
            )

            PoseDetailRow(
                emoji = "💡",
                title = "Best Background & Lighting",
                content = pose.bestBackgroundAndLighting
            )

            PoseDetailRow(
                emoji = "🌿",
                title = "Tips to Make Pose Look Natural",
                content = pose.naturalTips,
                isHighlight = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 4 REQUIRED INTERACTIVE ACTION BUTTONS
            // ==========================================

            // Button 1 & 2 Row: Show Another Pose & Save Pose
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("show_another_pose_button")
                        .clickable(onClick = onShowAnotherPose),
                    backgroundColor = DarkSurface,
                    borderColor = ElectricCyan.copy(alpha = 0.4f),
                    cornerRadius = 14.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Show Another Pose",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("save_pose_button")
                        .clickable(onClick = onToggleSave),
                    backgroundColor = DarkSurface,
                    borderColor = if (isSaved) HotPink else DarkSurfaceBorder,
                    cornerRadius = 14.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isSaved) HotPink else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSaved) "Pose Saved ✓" else "Save Pose",
                            color = if (isSaved) HotPink else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Button 3 & 4: Generate Pose Sequence & Use This Pose for My Video
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("generate_pose_sequence_button")
                        .clickable(onClick = onGenerateSequence),
                    backgroundColor = DarkSurface,
                    borderColor = NeonViolet.copy(alpha = 0.4f),
                    cornerRadius = 14.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListNumbered,
                            contentDescription = null,
                            tint = NeonViolet,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Generate Pose Sequence",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                GradientButton(
                    text = "Use for My Video",
                    onClick = onUseForVideo,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("use_pose_for_video_button"),
                    icon = Icons.Default.MovieCreation,
                    gradientColors = listOf(ElectricCyan, NeonViolet)
                )
            }
        }
    }
}

@Composable
private fun PoseDetailRow(
    emoji: String,
    title: String,
    content: String,
    isHighlight: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isHighlight) ElectricCyan.copy(alpha = 0.08f) else Color.Transparent)
            .padding(if (isHighlight) 8.dp else 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighlight) ElectricCyan else TextSecondary
            )
        }
        Text(
            text = content,
            fontSize = 13.sp,
            color = TextPrimary,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 2.dp, start = 20.dp)
        )
    }
}

@Composable
private fun PoseThumbCard(
    pose: PoseReference,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) ElectricCyan else DarkSurfaceBorder
    val bg = if (isSelected) DarkSurfaceElevated else DarkSurface

    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                PoseWireframeCanvas(
                    wireframeType = pose.wireframeType,
                    alpha = if (isSelected) 1f else 0.6f,
                    showGrid = false
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pose.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ElectricCyan else TextPrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = pose.difficultyLevel,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PoseAnalysisCard(
    isLoading: Boolean,
    result: com.example.data.model.PoseAnalysisResult?,
    uploadedBitmap: android.graphics.Bitmap?,
    onClear: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pose_analysis_card"),
        borderColor = if (isLoading) ElectricCyan else EmeraldGlow.copy(alpha = 0.5f),
        backgroundColor = DarkSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (isLoading) ElectricCyan else EmeraldGlow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isLoading) "ANALYZING YOUR PHOTO POSE..." else "AI POSE ANALYSIS FEEDBACK",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLoading) ElectricCyan else EmeraldGlow
                    )
                }

                if (!isLoading) {
                    IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
                    }
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = ElectricCyan, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Evaluating camera angle, spine contour, and limbs...", fontSize = 12.sp, color = TextSecondary)
                }
            } else if (result != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uploadedBitmap != null) {
                        Image(
                            bitmap = uploadedBitmap.asImageBitmap(),
                            contentDescription = "Uploaded Photo",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ElectricCyan, RoundedCornerShape(8.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Column {
                        Text(text = "Photogenic Score: ${result.score}/100", fontSize = 18.sp, fontWeight = FontWeight.Black, color = EmeraldGlow)
                        Text(text = result.detectedBodyPosition, fontSize = 12.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Key Improvements:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                for (imp in result.actionableImprovements) {
                    Row(modifier = Modifier.padding(top = 2.dp), verticalAlignment = Alignment.Top) {
                        Text(text = "•", color = ElectricCyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = imp, fontSize = 12.sp, color = TextPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun PoseSequenceDialog(
    pose: PoseReference,
    onClose: () -> Unit,
    onUseForVideo: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            backgroundColor = DarkSurface,
            borderColor = NeonViolet.copy(alpha = 0.5f),
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.FormatListNumbered, contentDescription = null, tint = NeonViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "POSE SEQUENCE FLOW", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonViolet)
                    }
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aesthetic multi-pose transition for 10-20s video reels based on ${pose.title}:",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                val sequenceSteps = if (pose.poseSequence.isNotEmpty()) pose.poseSequence else listOf(
                    "Step 1: Casual walk into frame checking sunglasses (0-3s)",
                    "Step 2: Sudden rhythmic stop, pop back hip into S-curve (3-7s)",
                    "Step 3: Head turn with confident eye-level smolder (7-12s)"
                )

                for ((idx, step) in sequenceSteps.withIndex()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurfaceElevated)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NeonViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${idx + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonViolet)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = step, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                GradientButton(
                    text = "Send Sequence to Video Editor",
                    onClick = onUseForVideo,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.MovieCreation
                )
            }
        }
    }
}
