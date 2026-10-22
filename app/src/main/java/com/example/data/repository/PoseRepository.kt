package com.example.data.repository

import com.example.data.model.PoseAnalysisResult
import com.example.data.model.PoseCategory
import com.example.data.model.PoseReference
import com.example.data.model.WireframeType

object PoseRepository {

    val allPoses = listOf(
        PoseReference(
            id = "pose_mirror_1",
            title = "The Full-Length Mirror Hip Pop",
            category = PoseCategory.SELFIE_MIRROR,
            subtitle = "Effortless outfit check that creates hourglass curves and elongates legs",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Casual Aesthetic",
            bodyPositionAndWeightDistribution = "Stand 3-4 feet from mirror. Shift 85% of your weight onto your back leg, pushing that hip gently outward to create a natural S-curve.",
            handAndLegPlacement = "Hold phone at chest height with dominant hand, tilted slightly downward. Free hand rests lightly on upper hip or grazes jean pocket. Front leg crosses slightly over back leg with toe pointed.",
            headAngleAndGaze = "Tilt head 10° towards the phone shoulder. Look directly into the phone screen reflection or glance at the mirror glass for a candid look.",
            cameraAngleAndDistance = "Chest height, angled 15° down, 3 to 4 feet distance from mirror.",
            facialExpressionSuggestion = "Relaxed micro-smile with lips parted 1-2mm to soften jaw tension.",
            bestBackgroundAndLighting = "Clean, clutter-free room with warm side window light or soft ambient lamp glow behind you.",
            naturalTips = "Never lock both knees. Keep the front knee softly bent so the stance looks breezy and effortless.",
            wireframeType = WireframeType.MIRROR_SELFIE_LEAN,
            poseSequence = listOf(
                "Step 1: Casual walk up to mirror, check hair with free hand",
                "Step 2: Pop back hip and point front toe on beat drop",
                "Step 3: Lower phone slightly and deliver confident glance into mirror"
            )
        ),
        PoseReference(
            id = "pose_selfie_2",
            title = "The 45° Golden Hour Selfie",
            category = PoseCategory.SELFIE_MIRROR,
            subtitle = "Sculpted cheekbones with natural glowing catchlights in the eyes",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Aesthetic Glow",
            bodyPositionAndWeightDistribution = "Upper torso turned 30° away from the main light source. Sit or stand with spine tall and shoulders rolled back.",
            handAndLegPlacement = "One hand holds the phone with elbow tucked in for stabilization. Opposite hand lightly frames the collarbone or grazes hair strand.",
            headAngleAndGaze = "Push chin slightly forward and down by 1 inch. Gaze directly into the front camera lens dot (not your preview face).",
            cameraAngleAndDistance = "Hold phone roughly 6 inches above eye level, angled down 20° at arm's length (about 18-24 inches).",
            facialExpressionSuggestion = "Soft, warm Duchenne smile with eye crinkles and relaxed brow.",
            bestBackgroundAndLighting = "Late afternoon sunset sun (Golden Hour) hitting front-side of face. Simple bokeh background.",
            naturalTips = "Press your tongue flat against the palate of your mouth—this naturally sharpens the submental jawline area.",
            wireframeType = WireframeType.HEAD_TILT_SELFIE,
            poseSequence = listOf(
                "Step 1: Face away from sun, then turn slowly into the golden light",
                "Step 2: Bring hand up to touch sunglasses or hair",
                "Step 3: Soft exhale through lips right as shutter fires"
            )
        ),
        PoseReference(
            id = "pose_fashion_1",
            title = "The S-Curve Blazer Stride",
            category = PoseCategory.FASHION_OOTD,
            subtitle = "High-fashion street showcase highlighting tailoring and movement",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Editorial Chic",
            bodyPositionAndWeightDistribution = "Walking motion frozen mid-stride. 70% weight on the rear leg as the front leg takes an elongated, deliberate step.",
            handAndLegPlacement = "Lead hand holds sunglasses or clutches handbag strap; rear hand is loosely in coat pocket. Lead foot steps directly forward on a straight runway line.",
            headAngleAndGaze = "Head turned 15° away from camera, eyes looking straight forward past the lens.",
            cameraAngleAndDistance = "Low camera angle (waist to knee height) from 8-10 feet away using 1x or 2x zoom for compressed proportions.",
            facialExpressionSuggestion = "Poised, serious editorial composure with relaxed mouth and piercing focus.",
            bestBackgroundAndLighting = "Urban limestone facade, modern minimalist architecture, or crosswalk in soft overcast daylight.",
            naturalTips = "Walk at half-speed! A slow, exaggerated walk gives the camera photographer plenty of time to catch the sharpest frame.",
            wireframeType = WireframeType.WALKING_STRIDE,
            poseSequence = listOf(
                "Step 1: Walk toward camera from 15 feet away",
                "Step 2: Spin jacket lapel or adjust bag strap on cue",
                "Step 3: Sudden confident stop, weight on back hip, direct eye contact"
            )
        ),
        PoseReference(
            id = "pose_street_1",
            title = "The Urban Corner Wall Lean",
            category = PoseCategory.STREET,
            subtitle = "Relaxed street-smart composition creating dynamic triangular negative space",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Streetwear",
            bodyPositionAndWeightDistribution = "Rest one shoulder and hip lightly against a textured wall. Weight distributed evenly across back foot flat against ground and wall foot.",
            handAndLegPlacement = "Raise outer leg and place sole of sneaker flat against the wall behind you. Cross arms loosely across chest or slide fingers into hoodie pouch.",
            headAngleAndGaze = "Head tilted down 5°, eyes glancing sideways towards oncoming street traffic.",
            cameraAngleAndDistance = "Medium-wide shot from 6-8 feet away, leveled at subject's chest height.",
            facialExpressionSuggestion = "Cool, enigmatic smirk or effortless neutral expression.",
            bestBackgroundAndLighting = "Exposed brick, neon cafe sign, or graffiti mural in moody ambient shade.",
            naturalTips = "Don't press your entire body flat into the wall. Keep daylight visible between your torso and the wall.",
            wireframeType = WireframeType.STREET_LEAN_WALL,
            poseSequence = listOf(
                "Step 1: Stroll past corner, casually plant sneaker on wall",
                "Step 2: Cross arms and look down at sneakers",
                "Step 3: Slow head lift to look straight into camera"
            )
        ),
        PoseReference(
            id = "pose_travel_1",
            title = "The Panoramic Horizon Turn",
            category = PoseCategory.TRAVEL_NATURE,
            subtitle = "Cinematic wanderlust composition connecting the subject to grand scenery",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Cinematic Nature",
            bodyPositionAndWeightDistribution = "Start with back to camera, facing panoramic vista. Shift weight onto one foot and turn upper torso 90° over your shoulder.",
            handAndLegPlacement = "One hand holds wide-brim sun hat or touches collar; other arm rests naturally at side or holds camera strap. Feet shoulder-width apart.",
            headAngleAndGaze = "Chin elevated 10° towards the sky or horizon line with dreamy, adventurous eye focus.",
            cameraAngleAndDistance = "Wide landscape framing (16:9 or 9:16) from 12-15 feet away, placing subject in lower third of frame.",
            facialExpressionSuggestion = "Radiant genuine wonder; soft open smile celebrating the view.",
            bestBackgroundAndLighting = "Mountain overlook, ocean coastline, or desert dunes during sunrise or sunset.",
            naturalTips = "Let the natural breeze interact with your hair or clothing—movement breathes life into landscape photography.",
            wireframeType = WireframeType.CINEMATIC_OVER_SHOULDER,
            poseSequence = listOf(
                "Step 1: Walk away from camera admiring horizon",
                "Step 2: Stop and breathe in the fresh air with arms wide",
                "Step 3: Turn head back with a radiant smile as if called by name"
            )
        ),
        PoseReference(
            id = "pose_portrait_1",
            title = "The Cafe Window Elbow Lean",
            category = PoseCategory.PORTRAITS,
            subtitle = "Intimate storytelling portrait utilizing foreground reflections and warm tones",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Moody Warmth",
            bodyPositionAndWeightDistribution = "Sitting at cafe table at 45° angle. Lean upper body forward from hips, resting one forearm flat on the wooden tabletop.",
            handAndLegPlacement = "Both hands cupping warm ceramic coffee mug, or one hand casually resting against chin without pressing into skin. Legs crossed under table.",
            headAngleAndGaze = "Gaze directed downward towards cup or daydreaming out the glass window.",
            cameraAngleAndDistance = "Eye-level close-medium shot from 4-5 feet away, shooting through foliage or window reflection.",
            facialExpressionSuggestion = "Gentle contemplative expression with relaxed brow and soft lips.",
            bestBackgroundAndLighting = "Diffused north window light illuminating side of face, cafe plants in background blur.",
            naturalTips = "Do not rest your full head weight on your hand! Hover your fingertips lightly against the cheek/jaw.",
            wireframeType = WireframeType.SITTING_LEAN,
            poseSequence = listOf(
                "Step 1: Take a gentle sip from the cup and set it down",
                "Step 2: Rest chin lightly on knuckles and look outside",
                "Step 3: Catch the photographer's eye with a warm smile"
            )
        ),
        PoseReference(
            id = "pose_fitness_1",
            title = "The Athletic Torque Lunge",
            category = PoseCategory.FITNESS,
            subtitle = "Powerful dynamic pose emphasizing muscular definition and athletic focus",
            difficultyLevel = "Intermediate",
            aestheticTag = "Athletic Energy",
            bodyPositionAndWeightDistribution = "Deep forward runner's lunge. 70% weight on lead foot, back leg straight with heel raised, core firmly braced.",
            handAndLegPlacement = "Lead hand rests on front knee; back hand holds gym towel or grips kettlebell. Torso rotated 20° towards camera lens.",
            headAngleAndGaze = "Intense forward laser gaze; chin parallel to floor.",
            cameraAngleAndDistance = "Ground-level low angle pointing up 25° from 6 feet away to accentuate power.",
            facialExpressionSuggestion = "Determined, focused athletic game-face; jaw firmly set.",
            bestBackgroundAndLighting = "Gym barbell rack, running track, or concrete studio with dramatic rim lighting.",
            naturalTips = "Exhale sharply to contract core abdominal muscles before the photo is taken.",
            wireframeType = WireframeType.ATHLETIC_LUNGE,
            poseSequence = listOf(
                "Step 1: Drop smoothly into lunge",
                "Step 2: Adjust grip on dumbbell or kettlebell",
                "Step 3: Lock eyes on lens with intense determination"
            )
        ),
        PoseReference(
            id = "pose_couples_1",
            title = "The Whispered Secret Laugh",
            category = PoseCategory.COUPLES_FRIENDS,
            subtitle = "Spontaneous, laughter-filled capture eliminating awkward stiffness",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Romantic Warmth",
            bodyPositionAndWeightDistribution = "Partners stand side-by-side, angled 30° toward each other with shoulders touching and hips aligned.",
            handAndLegPlacement = "Partner A wraps arm around Partner B's waist; Partner B holds Partner A's forearm or rests hand on their shoulder.",
            headAngleAndGaze = "Partner A leans in close to Partner B's ear; Partner B throws head back in genuine laughter.",
            cameraAngleAndDistance = "Chest level from 7-9 feet away with 50mm or 85mm portrait focal length.",
            facialExpressionSuggestion = "Authentic, uncontrolled laughter showing genuine joy.",
            bestBackgroundAndLighting = "Sunlit park, festival lights, or cozy living room string lights.",
            naturalTips = "Whisper your favorite absurd breakfast food in a seductive voice—it triggers real, uncontrollable laughter every single time!",
            wireframeType = WireframeType.COUPLE_EMBRACE,
            poseSequence = listOf(
                "Step 1: Walk together arm-in-arm towards camera",
                "Step 2: Sudden stop and lean in to whisper a silly joke",
                "Step 3: Genuine laughter with foreheads bumping softly"
            )
        ),
        PoseReference(
            id = "pose_cinematic_1",
            title = "The Cyberpunk Neon Smolder",
            category = PoseCategory.AESTHETIC_CINEMATIC,
            subtitle = "Moody high-contrast portrait inspired by neo-noir cinema",
            difficultyLevel = "Beginner Friendly",
            aestheticTag = "Cinematic Noir",
            bodyPositionAndWeightDistribution = "Stand in profile with shoulders squared 45° to the light. Weight centered with hips slightly back.",
            handAndLegPlacement = "One hand slowly adjusts collar, sunglasses, or earphone wire; other arm relaxed at side.",
            headAngleAndGaze = "Turn head slowly into the key light, chin tilted down 10° with eyes looking up through eyebrows (The Squinch).",
            cameraAngleAndDistance = "Tight portrait framing from 4 feet away at eye level.",
            facialExpressionSuggestion = "Mysterious smolder with lower eyelids engaged 15% and lips parted 2mm.",
            bestBackgroundAndLighting = "Neon storefront signs (cyan and magenta/amber split) reflecting on wet pavement or car glass.",
            naturalTips = "Mist light water onto face or jacket to create glossy specular reflections from the neon light.",
            wireframeType = WireframeType.STANDING_EDITORIAL,
            poseSequence = listOf(
                "Step 1: Stand looking down street in darkness",
                "Step 2: Step into neon pool of light",
                "Step 3: Slow cinematic head turn with direct squinch focus"
            )
        )
    )

    fun getPosesByCategory(category: PoseCategory): List<PoseReference> {
        return if (category == PoseCategory.ALL) allPoses else allPoses.filter { it.category == category }
    }

    fun getRandomPose(excludeId: String? = null): PoseReference {
        val candidates = if (excludeId != null) allPoses.filter { it.id != excludeId } else allPoses
        return candidates.randomOrNull() ?: allPoses.first()
    }

    fun generateMockPoseAnalysis(context: String): PoseAnalysisResult {
        return PoseAnalysisResult(
            score = 91,
            detectedBodyPosition = "Standing 3/4 dynamic posture with weight on rear leg",
            cameraAngleFeedback = "Camera height is balanced at chest level. Good vertical alignment.",
            postureFeedback = "Shoulders are nicely dropped and chest is open. Great spinal neutrality.",
            handPlacementFeedback = "Hands are active and non-clenched. Good negative space between torso and arms.",
            headAndGazeFeedback = "Head is angled 15° towards key light with chin slightly tucked.",
            backgroundContext = "Background has subtle architectural depth with flattering subject isolation.",
            keyStrengths = listOf(
                "Natural confidence in torso alignment",
                "Strong jawline definition achieved by chin tuck",
                "Great separation between arms and waist creating waistline slimming",
                "Soft, authentic eye contact"
            ),
            actionableImprovements = listOf(
                "Shift 10% more weight to the rear leg to exaggerate hip contour",
                "Drop your front shoulder by 1 inch to break square symmetry",
                "Part lips subtly on exhale to eliminate lower lip tension"
            ),
            recommendedPoses = allPoses.take(3)
        )
    }
}
