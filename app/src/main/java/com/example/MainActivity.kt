package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.PoseFlowUiState
import com.example.ui.PoseFlowViewModel
import com.example.ui.screens.CameraCoachScreen
import com.example.ui.screens.CreatorAssistantScreen
import com.example.ui.screens.ExpressionCoachScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PoseStudioScreen
import com.example.ui.screens.ProfilePrivacyScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.screens.VideoEditorScreen
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HotPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PoseFlowTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PoseFlowViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            PoseFlowTheme(darkTheme = uiState.isDarkMode) {
                PoseFlowApp(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoseFlowApp(
    viewModel: PoseFlowViewModel,
    uiState: PoseFlowUiState
) {
    val context = LocalContext.current

    // BackHandler: Return to Home tab if inside sub-screens
    BackHandler(enabled = uiState.currentTab != AppTab.HOME) {
        viewModel.selectTab(AppTab.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricCyan.copy(alpha = 0.2f))
                                .border(1.dp, ElectricCyan, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "P", fontWeight = FontWeight.Black, fontSize = 16.sp, color = ElectricCyan)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "PoseFlow",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AI",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = ElectricCyan
                                )
                            }
                            Text(
                                text = when (uiState.currentTab) {
                                    AppTab.HOME -> "Creative Studio Hub"
                                    AppTab.POSES -> "AI Pose Generator & Library"
                                    AppTab.EXPRESSIONS -> "Facial Expression Coach"
                                    AppTab.CAMERA_COACH -> "Live Viewfinder & Guide"
                                    AppTab.EDITOR -> "Cinematic Reel Editor"
                                    AppTab.ASSISTANT -> "Smart Storyboard Assistant"
                                    AppTab.TEMPLATES -> "Trending Short Templates"
                                    AppTab.PROFILE -> "Creator Profile & Privacy"
                                },
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                actions = {
                    // Quick Action: Templates
                    IconButton(
                        onClick = { viewModel.selectTab(AppTab.TEMPLATES) },
                        modifier = Modifier.testTag("templates_nav_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DashboardCustomize,
                            contentDescription = "Templates",
                            tint = if (uiState.currentTab == AppTab.TEMPLATES) NeonViolet else TextSecondary
                        )
                    }

                    // Quick Action: Profile & Privacy
                    IconButton(
                        onClick = { viewModel.selectTab(AppTab.PROFILE) },
                        modifier = Modifier.testTag("profile_nav_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (uiState.currentTab == AppTab.PROFILE) ElectricCyan else TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface.copy(alpha = 0.95f),
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            PoseFlowBottomNavigationBar(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ObsidianBlack),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(modifier = Modifier.fillMaxSize().widthIn(max = 680.dp)) {
                when (uiState.currentTab) {
                    AppTab.HOME -> {
                        HomeScreen(
                            uiState = uiState,
                            onNavigateTab = { viewModel.selectTab(it) },
                            onSelectPose = { viewModel.selectPose(it) },
                            onSelectMood = { viewModel.selectExpressionMood(it) }
                        )
                    }

                    AppTab.POSES -> {
                        PoseStudioScreen(
                            uiState = uiState,
                            onCategorySelected = { viewModel.setPoseCategory(it) },
                            onSelectPose = { viewModel.selectPose(it) },
                            onShowAnotherPose = { viewModel.showAnotherPose() },
                            onToggleSavePose = { viewModel.toggleSavePose(it) },
                            onOpenPoseSequence = { viewModel.openPoseSequenceDialog() },
                            onClosePoseSequence = { viewModel.closePoseSequenceDialog() },
                            onUsePoseForVideo = { viewModel.usePoseForVideo(it) },
                            onPhotoSelected = { viewModel.onPosePhotoSelected(context, it) },
                            onAnalyzeSample = { viewModel.analyzeSamplePose() },
                            onClearAnalysis = { viewModel.clearPoseAnalysis() },
                            onOpenLiveCameraCoach = { viewModel.selectTab(AppTab.CAMERA_COACH) }
                        )
                    }

                    AppTab.EXPRESSIONS -> {
                        ExpressionCoachScreen(
                            uiState = uiState,
                            onMoodSelected = { viewModel.selectExpressionMood(it) },
                            onPhotoSelected = { viewModel.onExpressionPhotoSelected(context, it) },
                            onAnalyzeSample = { viewModel.analyzeExpressionSample() },
                            onStartPractice = { viewModel.startExpressionPracticeSequence() },
                            onStopPractice = { viewModel.stopExpressionPracticeSequence() }
                        )
                    }

                    AppTab.CAMERA_COACH -> {
                        CameraCoachScreen(
                            uiState = uiState,
                            onSetMode = { viewModel.setCameraCoachMode(it) },
                            onStartTimer = { viewModel.startHandsFreeTimer(it) },
                            onSetWireframeAlpha = { viewModel.setCameraWireframeAlpha(it) },
                            onUsePoseForVideo = {
                                viewModel.usePoseForVideo(uiState.selectedPose)
                            }
                        )
                    }

                    AppTab.EDITOR -> {
                        VideoEditorScreen(
                            uiState = uiState,
                            onExecuteCommand = { viewModel.executeVideoCommand(it) },
                            onVideoSelected = { viewModel.setVideoUri(it) },
                            onSetAspectRatio = { viewModel.setAspectRatio(it) },
                            onSetFilter = { viewModel.setVideoFilter(it) },
                            onSetTransition = { viewModel.setVideoTransition(it) },
                            onUpdateTrim = { s, e -> viewModel.updateTrim(s, e) },
                            onUpdateSpeed = { viewModel.updatePlaybackSpeed(it) },
                            onUpdateBrightness = { viewModel.updateBrightness(it) },
                            onUpdateContrast = { viewModel.updateContrast(it) },
                            onUpdateSaturation = { viewModel.updateSaturation(it) },
                            onSetCustomText = { viewModel.setCustomTextOverlay(it) },
                            onSelectMusic = { viewModel.selectMusic(it) },
                            onGenerateCaptions = { viewModel.generateAiCaptions() },
                            onUndo = { viewModel.undoVideoEdit() },
                            onRedo = { viewModel.redoVideoEdit() },
                            onExportVideo = { viewModel.exportVideo() },
                            onDismissExportMessage = { viewModel.dismissExportMessage() }
                        )
                    }

                    AppTab.ASSISTANT -> {
                        CreatorAssistantScreen(
                            uiState = uiState,
                            onPromptChange = { viewModel.updatePromptInput(it) },
                            onPlatformChange = { viewModel.updateSelectedPlatform(it) },
                            onDurationChange = { viewModel.updateSelectedDuration(it) },
                            onGenerateStoryboard = { viewModel.generateStoryboard() },
                            onSendToVideoEditor = { viewModel.selectTab(AppTab.EDITOR) }
                        )
                    }

                    AppTab.TEMPLATES -> {
                        TemplatesScreen(
                            onSelectTemplate = { viewModel.selectTemplate(it) }
                        )
                    }

                    AppTab.PROFILE -> {
                        ProfilePrivacyScreen(
                            uiState = uiState,
                            onToggleOnDeviceAi = { viewModel.toggleOnDeviceAi(it) },
                            onToggleAnalyticsConsent = { viewModel.toggleAnalyticsConsent(it) },
                            onToggleAllowCloudUpload = { viewModel.toggleAllowPhotoUpload(it) },
                            onClearAllData = { viewModel.clearAllData() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PoseFlowBottomNavigationBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    val navTabs = listOf(
        AppTab.HOME to Icons.Default.Home,
        AppTab.POSES to Icons.Default.AccessibilityNew,
        AppTab.EXPRESSIONS to Icons.Default.Face,
        AppTab.CAMERA_COACH to Icons.Default.CameraAlt,
        AppTab.EDITOR to Icons.Default.MovieCreation,
        AppTab.ASSISTANT to Icons.Default.AutoAwesome
    )

    NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        navTabs.forEach { (tab, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ElectricCyan,
                    selectedTextColor = ElectricCyan,
                    indicatorColor = ElectricCyan.copy(alpha = 0.15f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
