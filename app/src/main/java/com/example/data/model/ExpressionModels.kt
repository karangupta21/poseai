package com.example.data.model

enum class ExpressionMood(
    val title: String,
    val vibeTag: String,
    val emoji: String,
    val description: String
) {
    CONFIDENT("Confident", "Empowered & Direct", "✨", "Direct laser gaze, relaxed brow, defined jawline angle"),
    NATURAL_SMILE("Natural Smile", "Authentic & Warm", "😊", "Engaged cheek muscles, Duchenne eye crinkles, soft open teeth"),
    SERIOUS_MODEL("Serious & Model-like", "High-Fashion Editorial", "🕶️", "Piercing focus, parted lips, elongated neck, sculptural cheekbones"),
    CUTE_PLAYFUL("Cute & Playful", "Spontaneous Charm", "🌟", "Subtle head tilt, sparkling eyes, youthful micro-smile"),
    ROMANTIC("Romantic", "Soft & Dreamy", "💫", "Lowered eyelids, gentle head angle towards shoulder, tender smile"),
    MYSTERIOUS("Mysterious", "Subtle Intrigue", "🌙", "Indirect gaze, 3/4 profile, Mona Lisa micro-expression"),
    HAPPY_ENERGETIC("Happy & Energetic", "Radiant & Vibrant", "⚡", "Wide joyful smile, lifted brows, dynamic head lift"),
    CALM_RELAXED("Calm & Relaxed", "Serene & Grounded", "🌿", "Soft focus, zero brow tension, smooth even breathing"),
    PROFESSIONAL("Professional", "Executive Credibility", "👔", "Level chin, confident gentle smile, engaging open eye contact"),
    CINEMATIC_DRAMATIC("Cinematic & Dramatic", "Deep Hollywood Mood", "🎬", "The 'Squinch' eye technique, dropped chin, rim lighting contour")
}

data class ExpressionGuide(
    val mood: ExpressionMood,
    val eyeDirection: String,
    val eyebrowPosition: String,
    val lipsGuidance: String,
    val smileIntensity: Int, // Scale 1 to 10
    val headTilt: String,
    val postureGuidance: String,
    val breathingTip: String,
    val encouragementNote: String
)

data class ExpressionAnalysisResult(
    val detectedMood: String,
    val confidenceScore: Int, // 0 - 100
    val naturalnessScore: Int, // 0 - 100
    val eyeEngagementScore: Int, // 0 - 100
    val jawlineDefinitionScore: Int, // 0 - 100
    val overallRating: String, // "High-Fashion Poise", "Warm & Approachable", etc.
    val positiveObservations: List<String>,
    val gentleSuggestions: List<String>
)

data class PracticeStep(
    val stepIndex: Int,
    val mood: ExpressionMood,
    val durationSeconds: Int = 4,
    val audioCuePrompt: String
)
