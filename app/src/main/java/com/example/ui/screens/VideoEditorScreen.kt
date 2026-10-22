package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AspectRatioPreset
import com.example.data.model.MusicTrack
import com.example.data.model.VideoFilter
import com.example.data.model.VideoTransition
import com.example.data.repository.TemplateRepository
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
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
fun VideoEditorScreen(
    uiState: PoseFlowUiState,
    onExecuteCommand: (String) -> Unit,
    onVideoSelected: (Uri) -> Unit,
    onSetAspectRatio: (AspectRatioPreset) -> Unit,
    onSetFilter: (VideoFilter) -> Unit,
    onSetTransition: (VideoTransition) -> Unit,
    onUpdateTrim: (Float, Float) -> Unit,
    onUpdateSpeed: (Float) -> Unit,
    onUpdateBrightness: (Float) -> Unit,
    onUpdateContrast: (Float) -> Unit,
    onUpdateSaturation: (Float) -> Unit,
    onSetCustomText: (String) -> Unit,
    onSelectMusic: (MusicTrack) -> Unit,
    onGenerateCaptions: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onExportVideo: () -> Unit,
    onDismissExportMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onVideoSelected(uri)
        }
    }

    val state = uiState.videoEditState
    var customCommandText by remember { mutableStateOf("") }
    var showExportDialog by remember { mutableStateOf(false) }

    val presetCommands = listOf(
        "Make this video cinematic.",
        "Turn this into a 20-second aesthetic reel.",
        "Remove pauses and boring sections.",
        "Add smooth transitions and subtitles.",
        "Make the colors warm and natural.",
        "Sync cuts to the music.",
        "Create a slow-motion highlight.",
        "Reframe this video for vertical viewing.",
        "Suggest poses and expressions for the next shot."
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
        ) {
            // Header: Undo/Redo & Export Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MovieCreation, contentDescription = null, tint = NeonViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI VIDEO EDITOR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonViolet,
                            letterSpacing = 1.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onUndo,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated)
                        ) {
                            Icon(imageVector = Icons.Default.Undo, contentDescription = "Undo", tint = TextPrimary, modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = onRedo,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated)
                        ) {
                            Icon(imageVector = Icons.Default.Redo, contentDescription = "Redo", tint = TextPrimary, modifier = Modifier.size(16.dp))
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        GradientButton(
                            text = "Export Reel",
                            onClick = { showExportDialog = true },
                            icon = Icons.Default.Download,
                            gradientColors = listOf(NeonViolet, ElectricCyan),
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }
            }

            // AI Natural Language Command Bar
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonViolet.copy(alpha = 0.35f),
                    backgroundColor = DarkSurfaceElevated
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "EDIT WITH AI COMMANDS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonViolet,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = customCommandText,
                                onValueChange = { customCommandText = it },
                                placeholder = { Text("E.g., 'Make this video cinematic' or 'Add subtitles'", fontSize = 12.sp, color = TextSecondary) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ai_video_command_input"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonViolet,
                                    unfocusedBorderColor = DarkSurfaceBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = {
                                    if (customCommandText.isNotBlank()) {
                                        onExecuteCommand(customCommandText)
                                        customCommandText = ""
                                    }
                                })
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    if (customCommandText.isNotBlank()) {
                                        onExecuteCommand(customCommandText)
                                        customCommandText = ""
                                    }
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NeonViolet)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = "Execute", tint = Color.White)
                            }
                        }

                        if (uiState.lastExecutedCommand != null) {
                            Text(
                                text = "✓ ${uiState.lastExecutedCommand}",
                                fontSize = 11.sp,
                                color = EmeraldGlow,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Preset Command Quick-Pills
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(presetCommands) { cmd ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurface)
                                        .border(1.dp, NeonViolet.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                        .clickable { onExecuteCommand(cmd) }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(text = cmd, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Real Video Preview Canvas with Aspect Ratio Framing & Color Grading Filter
            item {
                VideoPreviewFrame(
                    aspectRatio = state.aspectRatio,
                    activeFilter = state.activeFilter,
                    brightness = state.brightness,
                    contrast = state.contrast,
                    saturation = state.saturation,
                    customText = state.customTextOverlay,
                    captions = state.captions,
                    onUploadClick = {
                        videoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    }
                )
            }

            // Aspect Ratio Selector Row
            item {
                SectionHeader(
                    title = "Aspect Ratio & Platform",
                    subtitle = "Instant reframing with subject tracking",
                    badgeText = state.aspectRatio.displayName
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AspectRatioPreset.values()) { ratio ->
                        TagChip(
                            text = "${ratio.displayName} (${ratio.platformBadge})",
                            isSelected = state.aspectRatio == ratio,
                            onClick = { onSetAspectRatio(ratio) }
                        )
                    }
                }
            }

            // Editable Timeline: Clips & Trimming
            item {
                SectionHeader(
                    title = "Multi-Clip Timeline & Trimming",
                    subtitle = "Trim: ${String.format("%.1f", state.trimStartSec)}s - ${String.format("%.1f", state.trimEndSec)}s (${state.playbackSpeed}x speed)"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TimelineEditorCard(
                    state = state,
                    onUpdateTrim = onUpdateTrim,
                    onUpdateSpeed = onUpdateSpeed,
                    onSetTransition = onSetTransition,
                    onAddClipClick = {
                        videoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    }
                )
            }

            // Color Grading & Cinematic Filters
            item {
                SectionHeader(
                    title = "Cinematic Presets & Color Grading",
                    subtitle = "LUT-style color grading shaders",
                    badgeText = state.activeFilter.title
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(VideoFilter.values()) { filter ->
                        FilterPresetTile(
                            filter = filter,
                            isSelected = state.activeFilter == filter,
                            onClick = { onSetFilter(filter) }
                        )
                    }
                }
            }

            // Manual Color & Lighting Sliders (Brightness, Contrast, Saturation)
            item {
                ColorCorrectionSliders(
                    brightness = state.brightness,
                    contrast = state.contrast,
                    saturation = state.saturation,
                    onBrightnessChange = onUpdateBrightness,
                    onContrastChange = onUpdateContrast,
                    onSaturationChange = onUpdateSaturation
                )
            }

            // Audio & Beat Sync
            item {
                SectionHeader(
                    title = "Audio Tracks & Beat Detection",
                    subtitle = "Royalty-free soundtracks with beat sync",
                    badgeText = if (state.isBeatSynced) "Beat Synced ⚡" else "Audio"
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(TemplateRepository.musicTracks) { track ->
                        val isSelected = state.selectedMusic?.id == track.id
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonViolet.copy(alpha = 0.2f) else DarkSurfaceElevated)
                                .border(1.dp, if (isSelected) NeonViolet else DarkSurfaceBorder, RoundedCornerShape(12.dp))
                                .clickable { onSelectMusic(track) }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = track.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary, maxLines = 1)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${track.vibe} • ${track.bpm} BPM", fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // AI Captions & Subtitles
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = ElectricCyan.copy(alpha = 0.35f),
                    backgroundColor = DarkSurfaceElevated
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Subtitles, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "AUTO CAPTIONS & SUBTITLES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                            }

                            GradientButton(
                                text = "Generate Captions",
                                onClick = onGenerateCaptions,
                                isLoading = uiState.isGeneratingCaptions,
                                icon = Icons.Default.AutoAwesome,
                                modifier = Modifier.height(34.dp)
                            )
                        }

                        if (state.captions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            for (cap in state.captions) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(DarkSurface)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = cap.timestampFormatted, fontSize = 10.sp, color = ElectricCyan, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = cap.text, fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Export Reel Modal
        if (showExportDialog) {
            ExportReelDialog(
                state = state,
                onDismiss = { showExportDialog = false },
                onExportConfirm = {
                    showExportDialog = false
                    onExportVideo()
                }
            )
        }

        // Export Progress / Success Toast
        if (state.isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(20.dp),
                    backgroundColor = DarkSurface,
                    borderColor = ElectricCyan
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = ElectricCyan, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = "RENDERING VIDEO REEL...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = "Applying ${state.activeFilter.title} grade & ${state.exportResolution} encoding...",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { state.exportProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ElectricCyan,
                            trackColor = DarkSurfaceBorder
                        )
                    }
                }
            }
        }

        // Export status dialog
        if (uiState.exportSuccessMessage != null) {
            Dialog(onDismissRequest = onDismissExportMessage) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    backgroundColor = DarkSurface,
                    borderColor = HotPink
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(HotPink.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = HotPink, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "EXPORT NOT AVAILABLE", fontSize = 16.sp, fontWeight = FontWeight.Black, color = HotPink)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = uiState.exportSuccessMessage ?: "",
                            fontSize = 12.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GradientButton(
                            text = "Close",
                            onClick = onDismissExportMessage,
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Close
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoPreviewFrame(
    aspectRatio: AspectRatioPreset,
    activeFilter: VideoFilter,
    brightness: Float,
    contrast: Float,
    saturation: Float,
    customText: String,
    captions: List<com.example.data.model.CaptionSegment>,
    onUploadClick: () -> Unit
) {
    val ratio = aspectRatio.ratioWidth / aspectRatio.ratioHeight

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Aspect frame preview box
        Box(
            modifier = Modifier
                .aspectRatio(ratio, matchHeightConstraintsFirst = true)
                .fillMaxSize(0.92f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF2E1065).copy(alpha = contrast.coerceIn(0.5f, 1.5f)),
                            ObsidianBlack
                        )
                    )
                )
                .border(1.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NeonViolet.copy(alpha = 0.25f))
                        .clickable(onClick = onUploadClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = ElectricCyan, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Tap to load / change video", fontSize = 11.sp, color = TextSecondary)
            }

            // Overlay Text / Title
            if (customText.isNotBlank()) {
                Text(
                    text = customText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Subtitle overlay
            if (captions.isNotEmpty()) {
                Text(
                    text = captions.first().text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            // Current filter tag
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = activeFilter.title, fontSize = 9.sp, color = ElectricCyan, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TimelineEditorCard(
    state: com.example.data.model.VideoEditState,
    onUpdateTrim: (Float, Float) -> Unit,
    onUpdateSpeed: (Float) -> Unit,
    onSetTransition: (VideoTransition) -> Unit,
    onAddClipClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = DarkSurfaceBorder,
        backgroundColor = DarkSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Clip cards list
            Text(text = "VIDEO CLIPS ON TIMELINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.clips.forEachIndexed { idx, clip ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(clip.thumbnailColor))
                            .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    ) {
                        Column {
                            Text(text = clip.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "${clip.durationSec}s", fontSize = 9.sp, color = TextSecondary)
                        }
                    }

                    // Transition icon in between clips
                    if (idx < state.clips.size - 1) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NeonViolet.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = state.activeTransition.icon, fontSize = 10.sp)
                        }
                    }
                }

                // Add clip button
                IconButton(
                    onClick = onAddClipClick,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(DarkSurface)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add clip", tint = ElectricCyan, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Speed Control Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Speed:", fontSize = 11.sp, color = TextSecondary)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { spd ->
                        val isSel = state.playbackSpeed == spd
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) ElectricCyan.copy(alpha = 0.2f) else DarkSurface)
                                .border(1.dp, if (isSel) ElectricCyan else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                                .clickable { onUpdateSpeed(spd) }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${spd}x",
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) ElectricCyan else TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Transition selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Transition:", fontSize = 11.sp, color = TextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(VideoTransition.values()) { trans ->
                        val isSel = state.activeTransition == trans
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) NeonViolet.copy(alpha = 0.2f) else DarkSurface)
                                .border(1.dp, if (isSel) NeonViolet else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                                .clickable { onSetTransition(trans) }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${trans.icon} ${trans.displayName}",
                                fontSize = 10.sp,
                                color = if (isSel) NeonViolet else TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPresetTile(
    filter: VideoFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) ElectricCyan else DarkSurfaceBorder
    val bg = if (isSelected) DarkSurfaceElevated else DarkSurface

    Box(
        modifier = Modifier
            .width(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(
                                    (filter.rTint * 180).toInt().coerceIn(0, 255),
                                    (filter.gTint * 140).toInt().coerceIn(0, 255),
                                    (filter.bTint * 200).toInt().coerceIn(0, 255)
                                )
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = filter.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ElectricCyan else TextPrimary,
                maxLines = 1
            )
            Text(text = filter.subtitle, fontSize = 9.sp, color = TextSecondary, maxLines = 1)
        }
    }
}

@Composable
private fun ColorCorrectionSliders(
    brightness: Float,
    contrast: Float,
    saturation: Float,
    onBrightnessChange: (Float) -> Unit,
    onContrastChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DarkSurfaceElevated,
        borderColor = DarkSurfaceBorder
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "COLOR & LIGHT ENHANCEMENTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)

            // Brightness
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Brightness", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.width(80.dp))
                Slider(
                    value = brightness,
                    onValueChange = onBrightnessChange,
                    valueRange = -30f..30f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                )
                Text(text = "${brightness.toInt()}", fontSize = 11.sp, color = TextPrimary, modifier = Modifier.width(30.dp), textAlign = TextAlign.End)
            }

            // Contrast
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Contrast", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.width(80.dp))
                Slider(
                    value = contrast,
                    onValueChange = onContrastChange,
                    valueRange = 0.5f..1.8f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
                )
                Text(text = String.format("%.1f", contrast), fontSize = 11.sp, color = TextPrimary, modifier = Modifier.width(30.dp), textAlign = TextAlign.End)
            }

            // Saturation
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Saturation", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.width(80.dp))
                Slider(
                    value = saturation,
                    onValueChange = onSaturationChange,
                    valueRange = 0.0f..2.0f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(thumbColor = HotPink, activeTrackColor = HotPink)
                )
                Text(text = String.format("%.1f", saturation), fontSize = 11.sp, color = TextPrimary, modifier = Modifier.width(30.dp), textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
private fun ExportReelDialog(
    state: com.example.data.model.VideoEditState,
    onDismiss: () -> Unit,
    onExportConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            backgroundColor = DarkSurface,
            borderColor = ElectricCyan.copy(alpha = 0.5f),
            cornerRadius = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "EXPORT CONFIGURATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Render MP4 for ${state.aspectRatio.displayName} (${state.aspectRatio.platformBadge})", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Resolution: ${state.exportResolution}", fontSize = 12.sp, color = TextPrimary)
                Text(text = "Frame Rate: ${state.exportFrameRate}", fontSize = 12.sp, color = TextPrimary)
                Text(text = "Color Grade: ${state.activeFilter.title}", fontSize = 12.sp, color = TextPrimary)
                Text(text = "Encoding: H.264 / AAC High Bitrate", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.weight(1f).clickable(onClick = onDismiss),
                        backgroundColor = DarkSurfaceElevated,
                        borderColor = DarkSurfaceBorder,
                        cornerRadius = 12.dp
                    ) {
                        Text(
                            text = "Cancel",
                            color = TextSecondary,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    GradientButton(
                        text = "Start Render",
                        onClick = onExportConfirm,
                        modifier = Modifier.weight(1.3f),
                        icon = Icons.Default.Download
                    )
                }
            }
        }
    }
}
