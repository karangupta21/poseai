package com.example.data.model

enum class PoseCategory(val displayName: String, val iconName: String) {
    ALL("All Poses", "Grid"),
    SELFIE_MIRROR("Selfie & Mirror Selfie", "Face"),
    FASHION_OOTD("Fashion & Outfit Showcase", "Checkroom"),
    STREET("Street Photography", "DirectionsWalk"),
    TRAVEL_NATURE("Travel & Nature", "Flight"),
    PORTRAITS("Sitting & Standing Portraits", "Person"),
    FITNESS("Gym & Fitness", "FitnessCenter"),
    COUPLES_FRIENDS("Couples & Friends", "Favorite"),
    AESTHETIC_CINEMATIC("Cute, Confident & Cinematic", "AutoAwesome")
}

enum class WireframeType {
    STANDING_CASUAL,
    STANDING_EDITORIAL,
    SITTING_LEAN,
    WALKING_STRIDE,
    CROSS_LEGGED,
    HEAD_TILT_SELFIE,
    ATHLETIC_LUNGE,
    COUPLE_EMBRACE,
    STREET_LEAN_WALL,
    MIRROR_SELFIE_LEAN,
    CINEMATIC_OVER_SHOULDER
}

data class PoseReference(
    val id: String,
    val title: String,
    val category: PoseCategory,
    val subtitle: String,
    val difficultyLevel: String, // "Beginner Friendly", "Intermediate", "Pro"
    val aestheticTag: String, // "Casual", "Editorial", "Moody", "Athletic", "Cinematic"

    // Detailed required breakdown
    val bodyPositionAndWeightDistribution: String,
    val handAndLegPlacement: String,
    val headAngleAndGaze: String,
    val cameraAngleAndDistance: String,
    val facialExpressionSuggestion: String,
    val bestBackgroundAndLighting: String,
    val naturalTips: String,

    val wireframeType: WireframeType,
    val isSaved: Boolean = false,
    val poseSequence: List<String> = emptyList() // 3-4 fluid steps for transition video
)

data class PoseAnalysisResult(
    val score: Int, // 0 - 100
    val detectedBodyPosition: String,
    val cameraAngleFeedback: String,
    val postureFeedback: String,
    val handPlacementFeedback: String,
    val headAndGazeFeedback: String,
    val backgroundContext: String,
    val keyStrengths: List<String>,
    val actionableImprovements: List<String>,
    val recommendedPoses: List<PoseReference>
)

data class PoseFilterState(
    val selectedCategory: PoseCategory = PoseCategory.ALL,
    val selectedAesthetic: String = "All",
    val selectedDifficulty: String = "All",
    val searchQuery: String = ""
)
