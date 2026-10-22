package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StoryboardPlan
import com.example.data.model.StoryboardScene
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreatorAssistantScreen(
    uiState: PoseFlowUiState,
    onPromptChange: (String) -> Unit,
    onPlatformChange: (String) -> Unit,
    onDurationChange: (Int) -> Unit,
    onGenerateStoryboard: () -> Unit,
    onSendToVideoEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val platforms = listOf("Instagram Reels", "TikTok", "YouTube Shorts")
    val durations = listOf(15, 30, 60)

    val sampleIdeas = listOf(
        "15-second coffee shop morning routine in rainy city",
        "Streetwear outfit transition reel in Tokyo",
        "Intense athletic gym PR motivation short",
        "Golden hour cinematic travel walk and turn"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // Idea Prompt Input Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth().testTag("creator_assistant_input_card"),
                borderColor = Color(0xFFFBBF24).copy(alpha = 0.4f),
                backgroundColor = DarkSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFBBF24))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SMART CREATOR ASSISTANT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFBBF24),
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Describe your video idea in plain words",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "PoseFlow AI generates a complete shot list, pose plans, facial expression guides, camera angles, and hook audio.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.promptInput,
                        onValueChange = onPromptChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("prompt_input_field"),
                        placeholder = { Text("E.g., 20-second cinematic workout reel...", fontSize = 13.sp, color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFBBF24),
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sample Idea Quick-Fill
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(sampleIdeas) { idea ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurface)
                                    .clickable { onPromptChange(idea) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = idea, fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Platform & Duration Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Platform", fontSize = 10.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                platforms.forEach { plat ->
                                    val isSel = uiState.selectedPlatform == plat
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSel) Color(0xFFFBBF24).copy(alpha = 0.2f) else DarkSurface)
                                            .clickable { onPlatformChange(plat) }
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = plat.split(" ")[0], fontSize = 10.sp, color = if (isSel) Color(0xFFFBBF24) else TextSecondary)
                                    }
                                }
                            }
                        }

                        Column {
                            Text(text = "Length", fontSize = 10.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                durations.forEach { dur ->
                                    val isSel = uiState.selectedDuration == dur
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSel) Color(0xFFFBBF24).copy(alpha = 0.2f) else DarkSurface)
                                            .clickable { onDurationChange(dur) }
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = "${dur}s", fontSize = 10.sp, color = if (isSel) Color(0xFFFBBF24) else TextSecondary)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GradientButton(
                        text = "Generate AI Storyboard",
                        onClick = onGenerateStoryboard,
                        isLoading = uiState.isGeneratingStoryboard,
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(Color(0xFFFBBF24), HotPink),
                        textColor = Color.Black
                    )
                }
            }
        }

        // Generated Storyboard Plan Display
        if (uiState.storyboardPlan != null) {
            val plan = uiState.storyboardPlan
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader(
                        title = plan.title,
                        subtitle = "${plan.targetPlatform} • ${plan.aestheticTheme}",
                        badgeText = "${plan.scenes.size} Scenes"
                    )

                    IconButton(onClick = {
                        val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cb.setPrimaryClip(ClipData.newPlainText("Storyboard", formatPlanText(plan)))
                        Toast.makeText(context, "Storyboard copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = ElectricCyan)
                    }
                }
            }

            items(plan.scenes) { scene ->
                SceneBreakdownCard(scene = scene)
            }

            item {
                GradientButton(
                    text = "Send Storyboard to Video Editor",
                    onClick = onSendToVideoEditor,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.MovieCreation
                )
            }
        }
    }
}

@Composable
private fun SceneBreakdownCard(scene: StoryboardScene) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DarkSurfaceElevated,
        borderColor = DarkSurfaceBorder
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(ElectricCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${scene.sceneNumber}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = scene.shotType, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Text(text = scene.durationText, fontSize = 11.sp, color = NeonViolet, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            SceneDetailRow(label = "Camera Movement", value = scene.cameraMovement)
            SceneDetailRow(label = "Pose Plan", value = scene.poseInstruction)
            SceneDetailRow(label = "Expression", value = scene.facialExpression)
            SceneDetailRow(label = "Lighting", value = scene.lightingAndAtmosphere)
            SceneDetailRow(label = "Hook / Audio", value = scene.visualHookOrVoiceover, isHighlight = true)
        }
    }
}

@Composable
private fun SceneDetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(text = label, fontSize = 10.sp, color = if (isHighlight) ElectricCyan else TextSecondary, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
    }
}

private fun formatPlanText(plan: StoryboardPlan): String {
    val sb = StringBuilder()
    sb.appendLine("🎬 ${plan.title}")
    sb.appendLine("Platform: ${plan.targetPlatform} | Theme: ${plan.aestheticTheme}")
    sb.appendLine("Audio Vibe: ${plan.trendingAudioVibe}")
    sb.appendLine()
    plan.scenes.forEach { scene ->
        sb.appendLine("Scene ${scene.sceneNumber} (${scene.durationText}): ${scene.shotType}")
        sb.appendLine("  Camera: ${scene.cameraMovement}")
        sb.appendLine("  Pose: ${scene.poseInstruction}")
        sb.appendLine("  Expression: ${scene.facialExpression}")
        sb.appendLine("  Lighting: ${scene.lightingAndAtmosphere}")
        sb.appendLine("  Hook: ${scene.visualHookOrVoiceover}")
        sb.appendLine()
    }
    return sb.toString()
}
