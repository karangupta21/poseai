package com.example.data.model

import android.net.Uri

enum class AspectRatioPreset(val displayName: String, val ratioWidth: Float, val ratioHeight: Float, val platformBadge: String) {
    TIKTOK_REELS("9:16", 9f, 16f, "TikTok / Reels / Shorts"),
    SQUARE_FEED("1:1", 1f, 1f, "Instagram Square"),
    PORTRAIT_POST("4:5", 4f, 5f, "IG Portrait Feed"),
    WIDESCREEN("16:9", 16f, 9f, "YouTube Landscape")
}

enum class VideoFilter(
    val title: String,
    val subtitle: String,
    val brightnessMultiplier: Float,
    val contrastMultiplier: Float,
    val saturationMultiplier: Float,
    val rTint: Float,
    val gTint: Float,
    val bTint: Float
) {
    ORIGINAL("Original", "Natural capture", 0f, 1f, 1f, 1f, 1f, 1f),
    CINEMATIC_TEAL_ORANGE("Cinematic", "Blockbuster grade", 5f, 1.25f, 1.2f, 1.05f, 0.95f, 1.15f),
    WARM_NATURAL("Warm & Natural", "Golden sunlight infusion", 8f, 1.15f, 1.25f, 1.2f, 1.08f, 0.85f),
    VINTAGE_35MM("Vintage 35mm", "Soft highlights & grain", 3f, 1.1f, 0.9f, 1.15f, 1.05f, 0.9f),
    CYBERPUNK("Cyber Neon", "Deep cyan shadows, neon violet", 0f, 1.35f, 1.45f, 0.9f, 1.15f, 1.3f),
    MOODY_NOIR("Film Noir B&W", "High contrast monochrome", -5f, 1.4f, 0f, 1f, 1f, 1f)
}

enum class VideoTransition(val displayName: String, val icon: String) {
    NONE("None", "—"),
    DISSOLVE("Dissolve", "✨"),
    WHIP_PAN("Whip Pan", "💨"),
    ZOOM_IN("Zoom In", "🔍"),
    GLITCH("Glitch Cut", "⚡"),
    SMOOTH_FADE("Smooth Fade", "🌑")
}

data class VideoClipItem(
    val id: String,
    val title: String,
    val durationSec: Float,
    val thumbnailColor: Long,
    val isSlowMo: Boolean = false,
    val transitionToNext: VideoTransition = VideoTransition.DISSOLVE
)

data class CaptionSegment(
    val id: String,
    val text: String,
    val timestampFormatted: String,
    val highlightKeyword: String? = null
)

data class MusicTrack(
    val id: String,
    val name: String,
    val vibe: String,
    val durationText: String,
    val bpm: Int
)

data class VideoEditState(
    val videoUri: Uri? = null,
    val videoDurationSec: Float = 20f,
    val trimStartSec: Float = 0f,
    val trimEndSec: Float = 20f,
    val playbackSpeed: Float = 1.0f,
    val aspectRatio: AspectRatioPreset = AspectRatioPreset.TIKTOK_REELS,
    val activeFilter: VideoFilter = VideoFilter.ORIGINAL,
    val brightness: Float = 0f, // -50 to 50
    val contrast: Float = 1.0f, // 0.5 to 2.0
    val saturation: Float = 1.0f, // 0.0 to 2.0
    val activeTransition: VideoTransition = VideoTransition.DISSOLVE,
    val isAutoStabilizationEnabled: Boolean = true,
    val isSmartReframingEnabled: Boolean = true,
    val isSilenceRemoved: Boolean = false,
    val isBeatSynced: Boolean = false,
    val captions: List<CaptionSegment> = emptyList(),
    val customTextOverlay: String = "",
    val selectedMusic: MusicTrack? = null,
    val clips: List<VideoClipItem> = listOf(
        VideoClipItem("clip_1", "Intro Hook", 4.5f, 0xFF1E293B, isSlowMo = false, VideoTransition.WHIP_PAN),
        VideoClipItem("clip_2", "Pose Transition", 8.0f, 0xFF334155, isSlowMo = true, VideoTransition.ZOOM_IN),
        VideoClipItem("clip_3", "Final Reveal", 7.5f, 0xFF0F172A, isSlowMo = false, VideoTransition.NONE)
    ),
    // Export configuration
    val exportResolution: String = "1080p Full HD", // "1080p Full HD", "4K Ultra HD", "720p Social"
    val exportFrameRate: String = "60 fps", // "30 fps", "60 fps"
    val exportQuality: String = "High Bitrate (Pro)",
    val isProcessing: Boolean = false,
    val exportProgress: Float = 0f,
    val exportedFilePath: String? = null
)
