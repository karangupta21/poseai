package com.example.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiService
import com.example.data.model.AspectRatioPreset
import com.example.data.model.CaptionSegment
import com.example.data.model.ExpressionAnalysisResult
import com.example.data.model.ExpressionGuide
import com.example.data.model.ExpressionMood
import com.example.data.model.MusicTrack
import com.example.data.model.PoseAnalysisResult
import com.example.data.model.PoseCategory
import com.example.data.model.PoseFilterState
import com.example.data.model.PoseReference
import com.example.data.model.PracticeStep
import com.example.data.model.StoryboardPlan
import com.example.data.model.VideoClipItem
import com.example.data.model.VideoEditState
import com.example.data.model.VideoFilter
import com.example.data.model.VideoTransition
import com.example.data.repository.ExpressionRepository
import com.example.data.repository.PoseRepository
import com.example.data.repository.TemplateRepository
import com.example.data.repository.VideoTemplate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val title: String, val iconName: String) {
    HOME("Home", "Home"),
    POSES("Poses", "AccessibilityNew"),
    EXPRESSIONS("Expressions", "Face"),
    CAMERA_COACH("Camera Coach", "CameraAlt"),
    EDITOR("Video Editor", "MovieCreation"),
    ASSISTANT("Assistant", "AutoAwesome"),
    TEMPLATES("Templates", "DashboardCustomize"),
    PROFILE("Profile", "Person")
}

enum class CameraCoachMode(val title: String, val subtitle: String, val icon: String) {
    SOLO("Solo Creator", "Hands-free countdowns & eye tracking", "👤"),
    COUPLES("Couples & Friends", "Interactive duo pose alignment", "👥"),
    FASHION("Fashion & OOTD", "Full body silhouette & garment motion", "👗"),
    TRAVEL("Travel & Nature", "Landscape framing & rule of thirds", "✈️"),
    FITNESS("Fitness & Athletic", "Dynamic angles & kinetic lines", "🏋️"),
    PORTRAITS("Pro Portraits", "Golden hour lighting & jawline contour", "💼")
}

data class PoseFlowUiState(
    val currentTab: AppTab = AppTab.HOME,

    // Pose Generator state
    val poseFilterState: PoseFilterState = PoseFilterState(),
    val selectedPose: PoseReference = PoseRepository.allPoses.first(),
    val savedPoseIds: Set<String> = emptySet(),
    val showPoseSequenceDialog: Boolean = false,
    val uploadedPoseImage: Bitmap? = null,
    val poseAnalysisResult: PoseAnalysisResult? = null,
    val isAnalyzingPose: Boolean = false,
    val showPoseOverlay: Boolean = true,

    // Expression Coach state
    val selectedMood: ExpressionMood = ExpressionMood.CONFIDENT,
    val expressionGuide: ExpressionGuide = ExpressionRepository.getGuide(ExpressionMood.CONFIDENT),
    val uploadedExpressionImage: Bitmap? = null,
    val expressionAnalysisResult: ExpressionAnalysisResult? = null,
    val isAnalyzingExpression: Boolean = false,
    val isPracticingExpressionSequence: Boolean = false,
    val currentPracticeStepIndex: Int = 0,
    val practiceCountdownSeconds: Int = 3,

    // Camera Coach Live Viewfinder state
    val cameraCoachMode: CameraCoachMode = CameraCoachMode.SOLO,
    val isCameraGuidanceActive: Boolean = true,
    val cameraTimerCountdown: Int? = null,
    val cameraWireframeAlpha: Float = 0.75f,
    val isHandsFreeTimerRunning: Boolean = false,
    val cameraShotAngleTip: String = "Waist level, 10° upward tilt for elongated leg proportions",
    val cameraLightingTip: String = "Turn 20° towards key light to create soft cheekbone shadow",
    val cameraFramingTip: String = "Align subject head on upper horizontal third intersection",

    // Video Editor state
    val videoEditState: VideoEditState = VideoEditState(),
    val isGeneratingCaptions: Boolean = false,
    val lastExecutedCommand: String? = null,
    val exportSuccessMessage: String? = null,

    // Creator Assistant state
    val promptInput: String = "15-second moody street fashion transition in rainy downtown",
    val selectedPlatform: String = "Instagram Reels",
    val selectedDuration: Int = 15,
    val storyboardPlan: StoryboardPlan? = null,
    val isGeneratingStoryboard: Boolean = false,

    // Templates state
    val selectedTemplate: VideoTemplate? = null,

    // Privacy & Preferences
    val onDeviceAiPreferred: Boolean = false,
    val analyticsConsentGranted: Boolean = true,
    val allowPhotoUploadToCloud: Boolean = true,
    val isDarkMode: Boolean = true
)

class PoseFlowViewModel : ViewModel() {

    private val geminiService = GeminiService()

    private val _uiState = MutableStateFlow(PoseFlowUiState())
    val uiState: StateFlow<PoseFlowUiState> = _uiState.asStateFlow()

    // Undo / Redo stacks for Video Editor
    private val videoUndoStack = mutableListOf<VideoEditState>()
    private val videoRedoStack = mutableListOf<VideoEditState>()

    private var countdownJob: Job? = null
    private var expressionPracticeJob: Job? = null

    init {
        // Pre-save initial featured pose for UX demo
        _uiState.update {
            it.copy(
                selectedPose = PoseRepository.allPoses.first(),
                savedPoseIds = setOf("pose_fashion_1")
            )
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    // ==========================================
    // AI POSE GENERATOR METHODS
    // ==========================================

    fun setPoseCategory(category: PoseCategory) {
        _uiState.update {
            it.copy(
                poseFilterState = it.poseFilterState.copy(selectedCategory = category),
                selectedPose = PoseRepository.getPosesByCategory(category).firstOrNull() ?: it.selectedPose
            )
        }
    }

    fun selectPose(pose: PoseReference) {
        _uiState.update { it.copy(selectedPose = pose) }
    }

    fun showAnotherPose() {
        val currentId = _uiState.value.selectedPose.id
        val category = _uiState.value.poseFilterState.selectedCategory
        val pool = PoseRepository.getPosesByCategory(category)
        val next = pool.filter { it.id != currentId }.randomOrNull() ?: PoseRepository.getRandomPose(currentId)
        _uiState.update { it.copy(selectedPose = next) }
    }

    fun toggleSavePose(poseId: String) {
        _uiState.update { state ->
            val set = state.savedPoseIds.toMutableSet()
            if (set.contains(poseId)) {
                set.remove(poseId)
            } else {
                set.add(poseId)
            }
            state.copy(savedPoseIds = set)
        }
    }

    fun openPoseSequenceDialog() {
        _uiState.update { it.copy(showPoseSequenceDialog = true) }
    }

    fun closePoseSequenceDialog() {
        _uiState.update { it.copy(showPoseSequenceDialog = false) }
    }

    fun usePoseForVideo(pose: PoseReference) {
        _uiState.update {
            it.copy(
                currentTab = AppTab.EDITOR,
                videoEditState = it.videoEditState.copy(
                    customTextOverlay = pose.title
                )
            )
        }
    }

    fun togglePoseOverlay() {
        _uiState.update { it.copy(showPoseOverlay = !it.showPoseOverlay) }
    }

    fun onPosePhotoSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                } ?: throw IllegalArgumentException("The selected image could not be opened.")
                if (!bitmap.isRecycled) {
                    _uiState.update {
                        it.copy(
                            uploadedPoseImage = bitmap,
                            isAnalyzingPose = true,
                            poseAnalysisResult = null
                        )
                    }
                    val result = geminiService.analyzePoseMultimodal(
                        bitmap = bitmap,
                        userContext = "Analyze user pose against ${uiState.value.selectedPose.title}"
                    )
                    _uiState.update {
                        it.copy(
                            isAnalyzingPose = false,
                            poseAnalysisResult = result
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { it.copy(isAnalyzingPose = false) }
            }
        }
    }

    fun analyzeSamplePose() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingPose = true) }
            delay(700)
            val result = PoseRepository.generateMockPoseAnalysis("Sample portrait studio pose")
            _uiState.update {
                it.copy(
                    isAnalyzingPose = false,
                    poseAnalysisResult = result
                )
            }
        }
    }

    fun clearPoseAnalysis() {
        _uiState.update {
            it.copy(uploadedPoseImage = null, poseAnalysisResult = null)
        }
    }

    // ==========================================
    // AI FACIAL EXPRESSION COACH METHODS
    // ==========================================

    fun selectExpressionMood(mood: ExpressionMood) {
        _uiState.update {
            it.copy(
                selectedMood = mood,
                expressionGuide = ExpressionRepository.getGuide(mood),
                expressionAnalysisResult = null
            )
        }
    }

    fun startExpressionPracticeSequence() {
        expressionPracticeJob?.cancel()
        _uiState.update {
            it.copy(
                isPracticingExpressionSequence = true,
                currentPracticeStepIndex = 0,
                practiceCountdownSeconds = 3
            )
        }

        expressionPracticeJob = viewModelScope.launch {
            val sequence = ExpressionRepository.defaultPracticeSequence
            for ((index, step) in sequence.withIndex()) {
                _uiState.update {
                    it.copy(
                        currentPracticeStepIndex = index,
                        selectedMood = step.mood,
                        expressionGuide = ExpressionRepository.getGuide(step.mood),
                        practiceCountdownSeconds = 3
                    )
                }

                // 3-second countdown
                for (sec in 3 downTo 1) {
                    _uiState.update { it.copy(practiceCountdownSeconds = sec) }
                    delay(1000)
                }
                _uiState.update { it.copy(practiceCountdownSeconds = 0) }
                delay(step.durationSeconds * 1000L)
            }

            _uiState.update { it.copy(isPracticingExpressionSequence = false) }
        }
    }

    fun stopExpressionPracticeSequence() {
        expressionPracticeJob?.cancel()
        _uiState.update { it.copy(isPracticingExpressionSequence = false) }
    }

    fun onExpressionPhotoSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    _uiState.update {
                        it.copy(
                            uploadedExpressionImage = bitmap,
                            isAnalyzingExpression = true,
                            expressionAnalysisResult = null
                        )
                    }
                    val result = geminiService.analyzeExpressionMultimodal(
                        bitmap = bitmap,
                        targetMood = _uiState.value.selectedMood.title
                    )
                    _uiState.update {
                        it.copy(
                            isAnalyzingExpression = false,
                            expressionAnalysisResult = result
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isAnalyzingExpression = false) }
            }
        }
    }

    fun analyzeExpressionSample() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingExpression = true) }
            delay(600)
            val result = ExpressionAnalysisResult(
                detectedMood = _uiState.value.selectedMood.title,
                confidenceScore = 93,
                naturalnessScore = 91,
                eyeEngagementScore = 94,
                jawlineDefinitionScore = 89,
                overallRating = "Camera Ready & High-Definition",
                positiveObservations = listOf(
                    "Great Duchenne eye activation: genuine micro-crinkles at outer corners",
                    "Tongue pressed gently to palate cleanly defining the jawline",
                    "Head tilted naturally at 12° giving flattering facial depth"
                ),
                gentleSuggestions = listOf(
                    "Exhale softly through parted lips to release lower lip tension",
                    "Keep eye gaze directly centered on the lens glass for captivating connection"
                )
            )
            _uiState.update {
                it.copy(
                    isAnalyzingExpression = false,
                    expressionAnalysisResult = result
                )
            }
        }
    }

    // ==========================================
    // AI CAMERA COACH METHODS
    // ==========================================

    fun setCameraCoachMode(mode: CameraCoachMode) {
        val (heightTip, lightingTip) = when (mode) {
            CameraCoachMode.SOLO -> "Chest height, arm's length (24 inches) with 10° downward angle" to "Face soft ambient window light directly"
            CameraCoachMode.COUPLES -> "Eye level between partners from 7-8 feet distance" to "Warm side lighting casting soft shared shadows"
            CameraCoachMode.FASHION -> "Waist to knee level, 10-12 feet away with 1.5x zoom" to "Even diffused daylight with architectural rim flare"
            CameraCoachMode.TRAVEL -> "Low ground angle from 15 feet to capture landscape horizon" to "Golden hour direct sunlight starburst flare"
            CameraCoachMode.FITNESS -> "Ground level pointing up 25° for commanding presence" to "High-contrast rim backlight to accentuate definition"
            CameraCoachMode.PORTRAITS -> "Direct eye level from 5 feet away with 2x zoom portrait lens" to "45° key light creating Rembrandt triangle shadow"
        }
        _uiState.update {
            it.copy(
                cameraCoachMode = mode,
                cameraShotAngleTip = heightTip,
                cameraLightingTip = lightingTip
            )
        }
    }

    fun startHandsFreeTimer(seconds: Int) {
        val safeSeconds = seconds.coerceIn(1, 60)
        countdownJob?.cancel()
        _uiState.update {
            it.copy(isHandsFreeTimerRunning = false, cameraTimerCountdown = null)
        }
        val job = viewModelScope.launch {
            try {
                for (sec in safeSeconds downTo 1) {
                    _uiState.update {
                        it.copy(isHandsFreeTimerRunning = true, cameraTimerCountdown = sec)
                    }
                    delay(1000)
                }
                // This is only a countdown. The current screen has no camera capture callback.
                _uiState.update {
                    it.copy(isHandsFreeTimerRunning = false, cameraTimerCountdown = 0)
                }
                delay(500)
            } catch (e: CancellationException) {
                throw e
            } finally {
                if (countdownJob === coroutineContext[Job]) {
                    _uiState.update {
                        it.copy(isHandsFreeTimerRunning = false, cameraTimerCountdown = null)
                    }
                }
            }
        }
        countdownJob = job
    }

    fun setCameraWireframeAlpha(alpha: Float) {
        _uiState.update { it.copy(cameraWireframeAlpha = alpha) }
    }

    // ==========================================
    // PROFESSIONAL AI VIDEO EDITOR METHODS
    // ==========================================

    private fun saveVideoStateForUndo() {
        videoUndoStack.add(_uiState.value.videoEditState.copy())
        if (videoUndoStack.size > 20) videoUndoStack.removeAt(0)
        videoRedoStack.clear()
    }

    fun undoVideoEdit() {
        if (videoUndoStack.isNotEmpty()) {
            val previous = videoUndoStack.removeAt(videoUndoStack.size - 1)
            videoRedoStack.add(_uiState.value.videoEditState.copy())
            _uiState.update { it.copy(videoEditState = previous) }
        }
    }

    fun redoVideoEdit() {
        if (videoRedoStack.isNotEmpty()) {
            val next = videoRedoStack.removeAt(videoRedoStack.size - 1)
            videoUndoStack.add(_uiState.value.videoEditState.copy())
            _uiState.update { it.copy(videoEditState = next) }
        }
    }

    fun executeVideoCommand(command: String) {
        saveVideoStateForUndo()
        val lower = command.lowercase()

        when {
            lower.contains("cinematic") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Applied Cinematic Grade & 24fps Letterboxing",
                        videoEditState = it.videoEditState.copy(
                            activeFilter = VideoFilter.CINEMATIC_TEAL_ORANGE,
                            activeTransition = VideoTransition.SMOOTH_FADE,
                            aspectRatio = AspectRatioPreset.WIDESCREEN,
                            contrast = 1.25f,
                            saturation = 1.15f
                        )
                    )
                }
            }
            lower.contains("20-second") || lower.contains("aesthetic reel") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Formatted for 20-second 9:16 Viral Reel",
                        videoEditState = it.videoEditState.copy(
                            trimStartSec = 0f,
                            trimEndSec = 20f,
                            aspectRatio = AspectRatioPreset.TIKTOK_REELS,
                            activeFilter = VideoFilter.WARM_NATURAL,
                            customTextOverlay = "⚡ Daily Aesthetic Shift"
                        )
                    )
                }
            }
            lower.contains("pause") || lower.contains("silence") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Removed dead pauses and trimmed silence",
                        videoEditState = it.videoEditState.copy(
                            isSilenceRemoved = true,
                            trimEndSec = (it.videoEditState.trimEndSec * 0.85f).coerceAtLeast(5f)
                        )
                    )
                }
            }
            lower.contains("transition") || lower.contains("subtitle") -> {
                generateAiCaptions()
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Added whip pan transitions & auto-subtitles",
                        videoEditState = it.videoEditState.copy(
                            activeTransition = VideoTransition.WHIP_PAN
                        )
                    )
                }
            }
            lower.contains("warm") || lower.contains("natural") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Graded with Warm Golden Hour tone",
                        videoEditState = it.videoEditState.copy(
                            activeFilter = VideoFilter.WARM_NATURAL,
                            brightness = 6f,
                            saturation = 1.2f
                        )
                    )
                }
            }
            lower.contains("music") || lower.contains("sync") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Quantized clip cuts to 128 BPM music beats",
                        videoEditState = it.videoEditState.copy(
                            isBeatSynced = true,
                            selectedMusic = TemplateRepository.musicTracks.first()
                        )
                    )
                }
            }
            lower.contains("slow-motion") || lower.contains("highlight") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Slowed feature clip to 0.5x cinematic speed",
                        videoEditState = it.videoEditState.copy(
                            playbackSpeed = 0.5f
                        )
                    )
                }
            }
            lower.contains("reframe") || lower.contains("vertical") -> {
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Reframed subject tracking to 9:16 vertical",
                        videoEditState = it.videoEditState.copy(
                            aspectRatio = AspectRatioPreset.TIKTOK_REELS,
                            isSmartReframingEnabled = true
                        )
                    )
                }
            }
            lower.contains("pose") || lower.contains("next shot") -> {
                _uiState.update {
                    it.copy(
                        currentTab = AppTab.CAMERA_COACH,
                        lastExecutedCommand = "Opened Camera Coach for next scene pose"
                    )
                }
            }
            else -> {
                // Generic smart polish
                _uiState.update {
                    it.copy(
                        lastExecutedCommand = "Executed: '$command'",
                        videoEditState = it.videoEditState.copy(
                            activeFilter = VideoFilter.CINEMATIC_TEAL_ORANGE,
                            isAutoStabilizationEnabled = true
                        )
                    )
                }
            }
        }
    }

    fun setVideoUri(uri: Uri) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(
                videoEditState = it.videoEditState.copy(
                    videoUri = uri,
                    trimStartSec = 0f,
                    trimEndSec = 20f
                )
            )
        }
    }

    fun setAspectRatio(aspectRatio: AspectRatioPreset) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(aspectRatio = aspectRatio))
        }
    }

    fun setVideoFilter(filter: VideoFilter) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(activeFilter = filter))
        }
    }

    fun setVideoTransition(transition: VideoTransition) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(activeTransition = transition))
        }
    }

    fun updateTrim(startSec: Float, endSec: Float) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(
                videoEditState = it.videoEditState.copy(
                    trimStartSec = startSec,
                    trimEndSec = endSec
                )
            )
        }
    }

    fun updatePlaybackSpeed(speed: Float) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(playbackSpeed = speed))
        }
    }

    fun updateBrightness(brightness: Float) {
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(brightness = brightness))
        }
    }

    fun updateContrast(contrast: Float) {
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(contrast = contrast))
        }
    }

    fun updateSaturation(saturation: Float) {
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(saturation = saturation))
        }
    }

    fun setCustomTextOverlay(text: String) {
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(customTextOverlay = text))
        }
    }

    fun selectMusic(track: MusicTrack) {
        saveVideoStateForUndo()
        _uiState.update {
            it.copy(videoEditState = it.videoEditState.copy(selectedMusic = track))
        }
    }

    fun setExportConfig(resolution: String, fps: String, quality: String) {
        _uiState.update {
            it.copy(
                videoEditState = it.videoEditState.copy(
                    exportResolution = resolution,
                    exportFrameRate = fps,
                    exportQuality = quality
                )
            )
        }
    }

    fun generateAiCaptions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingCaptions = true) }
            val captions = geminiService.generateVideoCaptions(
                theme = "aesthetic fashion and cinematic mood",
                targetDurationSec = _uiState.value.videoEditState.trimEndSec - _uiState.value.videoEditState.trimStartSec
            )
            val segments = captions.mapIndexed { index, text ->
                CaptionSegment(
                    id = "cap_$index",
                    text = text,
                    timestampFormatted = "00:${String.format("%02d", index * 3)}"
                )
            }
            _uiState.update {
                it.copy(
                    isGeneratingCaptions = false,
                    videoEditState = it.videoEditState.copy(captions = segments)
                )
            }
        }
    }

    fun exportVideo() {
        // Do not report a fake MP4: rendering/muxing is not implemented in this editor yet.
        _uiState.update {
            it.copy(
                videoEditState = it.videoEditState.copy(
                    isProcessing = false,
                    exportProgress = 0f,
                    exportedFilePath = null
                ),
                exportSuccessMessage = "Video export is not implemented yet. Your edits are a preview; no MP4 file was created."
            )
        }
    }

    fun dismissExportMessage() {
        _uiState.update { it.copy(exportSuccessMessage = null) }
    }

    // ==========================================
    // CREATOR ASSISTANT & STORYBOARD
    // ==========================================

    fun updatePromptInput(prompt: String) {
        _uiState.update { it.copy(promptInput = prompt) }
    }

    fun updateSelectedPlatform(platform: String) {
        _uiState.update { it.copy(selectedPlatform = platform) }
    }

    fun updateSelectedDuration(duration: Int) {
        _uiState.update { it.copy(selectedDuration = duration) }
    }

    fun generateStoryboard() {
        val prompt = _uiState.value.promptInput
        val platform = _uiState.value.selectedPlatform
        val duration = _uiState.value.selectedDuration

        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingStoryboard = true) }
            val plan = geminiService.generateStoryboard(prompt, platform, duration)
            _uiState.update {
                it.copy(
                    isGeneratingStoryboard = false,
                    storyboardPlan = plan
                )
            }
        }
    }

    // ==========================================
    // TEMPLATES & SETTINGS
    // ==========================================

    fun selectTemplate(template: VideoTemplate) {
        _uiState.update {
            it.copy(
                selectedTemplate = template,
                currentTab = AppTab.EDITOR,
                videoEditState = it.videoEditState.copy(
                    selectedMusic = template.recommendedMusic,
                    customTextOverlay = template.hookIdea,
                    trimEndSec = 18f
                )
            )
        }
    }

    fun toggleOnDeviceAi(preferred: Boolean) {
        _uiState.update { it.copy(onDeviceAiPreferred = preferred) }
    }

    fun toggleAnalyticsConsent(consent: Boolean) {
        _uiState.update { it.copy(analyticsConsentGranted = consent) }
    }

    fun toggleAllowPhotoUpload(allow: Boolean) {
        _uiState.update { it.copy(allowPhotoUploadToCloud = allow) }
    }

    fun clearAllData() {
        _uiState.update {
            it.copy(
                uploadedPoseImage = null,
                poseAnalysisResult = null,
                uploadedExpressionImage = null,
                expressionAnalysisResult = null,
                videoEditState = VideoEditState()
            )
        }
    }
}
