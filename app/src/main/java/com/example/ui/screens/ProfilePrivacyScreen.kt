package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.BuildConfig
import com.example.ui.PoseFlowUiState
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.SectionHeader
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
fun ProfilePrivacyScreen(
    uiState: PoseFlowUiState,
    onToggleOnDeviceAi: (Boolean) -> Unit,
    onToggleAnalyticsConsent: (Boolean) -> Unit,
    onToggleAllowCloudUpload: (Boolean) -> Unit,
    onClearAllData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasApiKey = !BuildConfig.GEMINI_API_KEY.isNullOrBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // Creator Profile Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth().testTag("profile_card"),
                borderColor = ElectricCyan.copy(alpha = 0.35f),
                backgroundColor = DarkSurfaceElevated
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(ElectricCyan.copy(alpha = 0.2f))
                            .border(2.dp, ElectricCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Creator Studio Pro", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "PoseFlow AI • Level 4 Photogenic Creator", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔥 7-Day Creative Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HotPink)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "📌 ${uiState.savedPoseIds.size} Saved Poses", fontSize = 11.sp, color = ElectricCyan)
                        }
                    }
                }
            }
        }

        // Privacy & User Consent Section (Mandatory Requirement)
        item {
            SectionHeader(
                title = "Privacy & User Consent Controls",
                subtitle = "You have complete authority over how your photos, facial scans, and videos are processed",
                badgeText = "Private"
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DarkSurfaceElevated,
                borderColor = DarkSurfaceBorder
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Control 1: Cloud AI Processing Consent
                    PrivacyToggleRow(
                        title = "Allow AI Cloud Processing",
                        description = "Enables Gemini 3.5 Flash multimodal pose and expression coaching. If disabled, app uses on-device rule heuristics.",
                        checked = uiState.allowPhotoUploadToCloud,
                        onCheckedChange = onToggleAllowCloudUpload
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Control 2: On-Device Processing Preference
                    PrivacyToggleRow(
                        title = "Prefer On-Device Local Processing",
                        description = "Keep all video renders, trims, and filters strictly on your device without transmitting data.",
                        checked = uiState.onDeviceAiPreferred,
                        onCheckedChange = onToggleOnDeviceAi
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Control 3: Anonymous Quality Analytics
                    PrivacyToggleRow(
                        title = "Anonymous App Diagnostics",
                        description = "Shares crash diagnostics only. No photos, videos, or personal identifiers are ever stored or shared.",
                        checked = uiState.analyticsConsentGranted,
                        onCheckedChange = onToggleAnalyticsConsent
                    )
                }
            }
        }

        // Security & API Key Status
        item {
            SectionHeader(
                title = "AI Engine Configuration",
                subtitle = "Multimodal vision & generative services status"
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DarkSurfaceElevated,
                borderColor = if (hasApiKey) EmeraldGlow.copy(alpha = 0.4f) else NeonViolet.copy(alpha = 0.4f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = if (hasApiKey) EmeraldGlow else NeonViolet)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Gemini AI API Status", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (hasApiKey) EmeraldGlow.copy(alpha = 0.2f) else NeonViolet.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (hasApiKey) "Active (Cloud AI)" else "Demo / Offline Mode",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasApiKey) EmeraldGlow else NeonViolet
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (hasApiKey)
                            "Your Gemini API key is configured. Pose analysis, facial expression coaching, and video caption generation are powered directly by Gemini 3.5 Flash."
                        else
                            "Running in high-fidelity offline mode. Curated pose libraries, facial biomechanics, video editing shaders, and storyboards are fully active. Add your GEMINI_API_KEY in the Secrets panel anytime for live Gemini multimodal cloud analysis.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Clear Local Storage & Cache
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DarkSurface,
                borderColor = DarkSurfaceBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Clear Local Data & Cache", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Delete uploaded photos, analysis results, and temporary video renders.", fontSize = 11.sp, color = TextSecondary)
                    }

                    GradientButton(
                        text = "Clear All",
                        onClick = {
                            onClearAllData()
                            Toast.makeText(context, "Local app data cleared", Toast.LENGTH_SHORT).show()
                        },
                        icon = Icons.Default.Delete,
                        gradientColors = listOf(Color(0xFFEF4444), HotPink),
                        textColor = Color.White,
                        modifier = Modifier.height(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacyToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = description, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp, modifier = Modifier.padding(top = 2.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ElectricCyan,
                checkedTrackColor = ElectricCyan.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = DarkSurface
            )
        )
    }
}
