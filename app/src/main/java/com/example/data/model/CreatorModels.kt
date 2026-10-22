package com.example.data.model

data class StoryboardScene(
    val sceneNumber: Int,
    val durationText: String,
    val shotType: String, // e.g., "Wide Establishing", "Medium Close-up", "Low-Angle Hero Shot"
    val cameraMovement: String, // e.g., "Slow Push-in", "Pan Right", "Static with subtle hand-held breathing"
    val poseInstruction: String, // e.g., "Slight turn, right hand casually in pocket, weight shifted to back foot"
    val facialExpression: String, // e.g., "Warm smile turning into confident direct eye contact"
    val lightingAndAtmosphere: String, // e.g., "Backlit by window golden glow, rim light on hair"
    val visualHookOrVoiceover: String // e.g., "The secret nobody told you about golden hour..."
)

data class StoryboardPlan(
    val title: String,
    val conceptOverview: String,
    val targetPlatform: String,
    val aestheticTheme: String,
    val recommendedPacing: String,
    val trendingAudioVibe: String,
    val scenes: List<StoryboardScene>
)
