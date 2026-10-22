package com.example.data.repository

import com.example.data.model.MusicTrack

data class VideoTemplate(
    val id: String,
    val title: String,
    val category: String, // "Fashion", "Cinematic", "Fitness", "Travel", "Lifestyle"
    val durationText: String,
    val shotsCount: Int,
    val description: String,
    val platformTag: String, // "Trending TikTok", "Reels Viral", "Shorts 60s"
    val hookIdea: String,
    val recommendedMusic: MusicTrack,
    val shotListSummary: List<String>
)

object TemplateRepository {

    val musicTracks = listOf(
        MusicTrack("track_1", "Midnight Neon Drive", "Cyberpunk Synthwave", "0:30", 128),
        MusicTrack("track_2", "Tokyo Rain Drops", "Chillhop Lofi", "0:25", 84),
        MusicTrack("track_3", "Golden Hour Acoustic", "Dreamy Indie Pop", "0:20", 96),
        MusicTrack("track_4", "Peak Velocity", "Trap Energy", "0:15", 140),
        MusicTrack("track_5", "Deep Horizon", "Cinematic Drone", "0:45", 72)
    )

    val templates = listOf(
        VideoTemplate(
            id = "tmpl_1",
            title = "3-Look Seamless Outfit Switch",
            category = "Fashion",
            durationText = "12s",
            shotsCount = 3,
            description = "High-retention fashion transition utilizing foot stomp / jump cut synced with beat drop.",
            platformTag = "Trending TikTok",
            hookIdea = "Cover camera lens with shoe, pull back in new fit",
            recommendedMusic = musicTracks[0],
            shotListSummary = listOf(
                "Look 1: Casual walk into frame + stomp shoe at lens (0-3s)",
                "Look 2: Pull shoe away revealing Streetwear fit + 360 spin (3-7s)",
                "Look 3: Snap fingers into Luxury Evening Chic + close-up smirk (7-12s)"
            )
        ),
        VideoTemplate(
            id = "tmpl_2",
            title = "Cinematic Day in the City",
            category = "Cinematic",
            durationText = "18s",
            shotsCount = 5,
            description = "Moody atmospheric urban reel with speed ramps, reflection shots, and deep color grade.",
            platformTag = "Reels Viral",
            hookIdea = "Extreme close-up pouring hot coffee with steam catching morning sun",
            recommendedMusic = musicTracks[1],
            shotListSummary = listOf(
                "Macro: Pouring coffee / lighting candle (0-2.5s)",
                "Medium: Strolling through bustling crosswalk in slow-mo (2.5-6s)",
                "Low angle: Architectural symmetry looking up at skyscrapers (6-10s)",
                "Profile: Sitting by rain-streaked cafe window (10-14s)",
                "Wide: Sunset silhouette against city skyline (14-18s)"
            )
        ),
        VideoTemplate(
            id = "tmpl_3",
            title = "High-Intensity Workout Motivation",
            category = "Fitness",
            durationText = "15s",
            shotsCount = 4,
            description = "Fast-paced kinetic fitness reel with heavy beat syncing and dramatic sweat specular highlights.",
            platformTag = "Shorts 60s",
            hookIdea = "Chalk dust clapping into air in super slow motion",
            recommendedMusic = musicTracks[3],
            shotListSummary = listOf(
                "Chalk explosion + heavy breath focus (0-3s)",
                "Barbell lift power phase with aggressive speed ramp (3-7s)",
                "Torso pump & intense focused eye contact (7-11s)",
                "Dumbbell drop & nod of satisfaction (11-15s)"
            )
        ),
        VideoTemplate(
            id = "tmpl_4",
            title = "Travel Wanderlust Reveal",
            category = "Travel",
            durationText = "15s",
            shotsCount = 4,
            description = "Sweeping landscape reveal: starting on subject's eyes, then panning out to stunning panoramic view.",
            platformTag = "Reels Viral",
            hookIdea = "Subject's back to camera looking at horizon, turns around with a wide smile",
            recommendedMusic = musicTracks[2],
            shotListSummary = listOf(
                "Close-up: Subject walking forward, wind blowing hair (0-3s)",
                "Drone/High angle: Revealing turquoise ocean or mountain peak (3-7s)",
                "Handheld: Running along beach shore barefoot (7-11s)",
                "Sunset toast with travel buddy or smiling at camera (11-15s)"
            )
        )
    )
}
