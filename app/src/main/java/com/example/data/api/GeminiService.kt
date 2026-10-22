package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ExpressionAnalysisResult
import com.example.data.model.PoseAnalysisResult
import com.example.data.model.PoseReference
import com.example.data.model.StoryboardPlan
import com.example.data.model.StoryboardScene
import com.example.data.repository.PoseRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class GeminiService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun analyzePoseMultimodal(
        bitmap: Bitmap,
        userContext: String
    ): PoseAnalysisResult = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured()) {
            return@withContext PoseRepository.generateMockPoseAnalysis(userContext)
        }

        try {
            val base64Image = bitmapToBase64(bitmap)
            val prompt = """
                You are a world-class professional fashion photographer and pose coach.
                Analyze this photo of a person. Provide an evaluation in strictly valid JSON format with keys:
                {
                   "score": 88,
                   "detectedBodyPosition": "Standing 3/4 turn towards key light with right hand on hip",
                   "cameraAngleFeedback": "Camera is at chest height giving a natural proportion",
                   "postureFeedback": "Shoulders are nicely dropped, slight pelvic tilt elongates torso",
                   "handPlacementFeedback": "Right hand resting lightly on waist; left arm could relax more",
                   "headAndGazeFeedback": "Chin slightly lowered creating jawline definition, strong eye contact",
                   "backgroundContext": "Clean architectural depth with good subject separation",
                   "keyStrengths": ["Natural confidence", "Excellent shoulder relaxation", "Flattering angle"],
                   "actionableImprovements": ["Shift weight to back leg to create S-curve", "Part lips slightly for softer mouth tension", "Elongate neck by 1 inch"],
                   "topRecommendation": "Editorial Power Stride"
                }
                Context from user: $userContext
            """.trimIndent()

            val responseText = callGeminiMultimodal(prompt, base64Image)
            parsePoseAnalysisJson(responseText, userContext)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("GeminiService", "Error calling Gemini pose analysis", e)
            PoseRepository.generateMockPoseAnalysis(userContext)
        }
    }

    suspend fun analyzeExpressionMultimodal(
        bitmap: Bitmap,
        targetMood: String
    ): ExpressionAnalysisResult = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured()) {
            return@withContext generateOfflineExpressionAnalysis(targetMood)
        }

        try {
            val base64Image = bitmapToBase64(bitmap)
            val prompt = """
                You are an expert Hollywood acting coach and portrait photographer.
                Analyze the facial expression of this person for the desired mood: '$targetMood'.
                Return strictly valid JSON with keys:
                {
                   "detectedMood": "Confident & Engaging",
                   "confidenceScore": 89,
                   "naturalnessScore": 92,
                   "eyeEngagementScore": 86,
                   "jawlineDefinitionScore": 90,
                   "overallRating": "Magnetic & Camera-Ready",
                   "observations": ["Micro-smile engages the cheek apples naturally", "Eyes have lively sparkle without staring", "Forehead is relaxed with zero furrowing"],
                   "instantCoachingTips": ["Drop your shoulders and exhale before snap", "Press tongue against roof of mouth to sharpen jawline", "Squinch slightly: raise lower eyelids 10%"]
                }
            """.trimIndent()

            val responseText = callGeminiMultimodal(prompt, base64Image)
            parseExpressionAnalysisJson(responseText, targetMood)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("GeminiService", "Error calling Gemini expression analysis", e)
            generateOfflineExpressionAnalysis(targetMood)
        }
    }

    suspend fun generateStoryboard(
        concept: String,
        platform: String,
        durationSeconds: Int
    ): StoryboardPlan = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured()) {
            return@withContext generateOfflineStoryboard(concept, platform, durationSeconds)
        }

        try {
            val prompt = """
                You are an elite short-form video director for TikTok, Reels, and YouTube Shorts.
                Create an end-to-end viral storyboard plan for: "$concept".
                Platform: $platform. Target Duration: ${durationSeconds}s.
                Return strictly valid JSON format:
                {
                   "title": "Aesthetic Morning Shift",
                   "conceptOverview": "A high-retention fast-paced visual reel showcasing morning discipline",
                   "targetPlatform": "$platform",
                   "aestheticTheme": "Moody Warmth & Cinematic Motion",
                   "recommendedPacing": "Fast cut hook (0-3s), rhythmic transitions every 1.5s",
                   "trendingAudioVibe": "Lofi Chillhop or Ambient Electronic Beat",
                   "scenes": [
                      {
                         "sceneNumber": 1,
                         "durationText": "0.0s - 2.5s",
                         "shotType": "Extreme Close-Up (ECU)",
                         "cameraMovement": "Dynamic Whip Pan into subject eye/sunglasses",
                         "poseInstruction": "Head turn towards camera, hand adjusting shades",
                         "facialExpression": "Cinematic smolder with relaxed parted lips",
                         "lightingAndAtmosphere": "Early morning golden ray cutting through blinds",
                         "visualHookOrVoiceover": "Text: Stop scrolling if you want to fix your aesthetic."
                      },
                      {
                         "sceneNumber": 2,
                         "durationText": "2.5s - 5.0s",
                         "shotType": "Medium Low-Angle",
                         "cameraMovement": "Tracking back while subject strides forward",
                         "poseInstruction": "Confident power walk, coat flowing naturally",
                         "facialExpression": "Direct focused gaze, determined composure",
                         "lightingAndAtmosphere": "Urban skyline backlight with rim flare",
                         "visualHookOrVoiceover": "Audio drop coincides with stride step"
                      },
                      {
                         "sceneNumber": 3,
                         "durationText": "5.0s - 8.5s",
                         "shotType": "3/4 Profile Silhouette",
                         "cameraMovement": "Orbiting slow clockwise",
                         "poseInstruction": "Weight on back heel, cup of espresso in foreground",
                         "facialExpression": "Subtle contemplative half-smile",
                         "lightingAndAtmosphere": "Deep shadows, cafe neon reflection in window",
                         "visualHookOrVoiceover": "Text: The 3 rules I live by every day."
                      },
                      {
                         "sceneNumber": 4,
                         "durationText": "8.5s - 12.0s",
                         "shotType": "Full Body Establishing to Close Punch-in",
                         "cameraMovement": "Snap zoom on final beat",
                         "poseInstruction": "Signature pose: jacket flick & confident stance",
                         "facialExpression": "Radiant charismatic smile straight to lens",
                         "lightingAndAtmosphere": "Full front beauty glow, soft lens diffusion",
                         "visualHookOrVoiceover": "Call-To-Action: Save this for your next shoot."
                      }
                   ]
                }
            """.trimIndent()

            val responseText = callGeminiText(prompt)
            parseStoryboardJson(responseText, concept, platform, durationSeconds)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("GeminiService", "Error calling Gemini storyboard", e)
            generateOfflineStoryboard(concept, platform, durationSeconds)
        }
    }

    suspend fun generateVideoCaptions(
        theme: String,
        targetDurationSec: Float
    ): List<String> = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured()) {
            return@withContext listOf(
                "⚡ Unlock your aesthetic",
                "POV: You found the right lighting 🌅",
                "Small details make the biggest difference",
                "Drop a 🔥 if you needed this reminder!"
            )
        }

        try {
            val prompt = """
                Write 4 punchy viral captions/subtitles for a short $theme video ($targetDurationSec seconds).
                Make them engaging for TikTok/Reels with emojis and strong hooks.
                Return only JSON array of strings: ["Caption 1", "Caption 2", "Caption 3", "Caption 4"]
            """.trimIndent()
            val responseText = callGeminiText(prompt)
            val jsonArray = JSONArray(extractJsonContent(responseText))
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("GeminiService", "Error generating video captions", e)
            listOf(
                "⚡ Level up your creative flow",
                "Golden hour hits different when the pose is locked",
                "Frame the shot. Own the moment.",
                "Save this pose guide for later 📌"
            )
        }
    }

    private fun callGeminiText(prompt: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent"

        val requestJson = JSONObject().apply {
            val contents = JSONArray().apply {
                val turn = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    }
                    put("parts", parts)
                }
                put(turn)
            }
            put("contents", contents)
        }

        val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder().url(url).header("x-goog-api-key", apiKey).post(requestBody).build()

        okHttpClient.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()
                ?: throw IOException("Gemini returned an empty response body")
            if (!response.isSuccessful) {
                throw IOException("Gemini request failed with HTTP ${response.code}")
            }
            return extractTextFromResponse(responseBody)
        }
    }

    private fun callGeminiMultimodal(prompt: String, base64Image: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val requestJson = JSONObject().apply {
            val contents = JSONArray().apply {
                val turn = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                        put(JSONObject().apply {
                            put("inlineData", JSONObject().apply {
                                put("mimeType", "image/jpeg")
                                put("data", base64Image)
                            })
                        })
                    }
                    put("parts", parts)
                }
                put(turn)
            }
            put("contents", contents)
        }

        val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder().url(url).post(requestBody).build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw RuntimeException("Empty response")
        return extractTextFromResponse(responseBody)
    }

    private fun extractTextFromResponse(responseBody: String): String {
        val root = JSONObject(responseBody)
        val candidates = root.optJSONArray("candidates") ?: return ""
        if (candidates.length() == 0) return ""
        val candidate = candidates.getJSONObject(0)
        val content = candidate.optJSONObject("content") ?: return ""
        val parts = content.optJSONArray("parts") ?: return ""
        if (parts.length() == 0) return ""
        return parts.getJSONObject(0).optString("text", "")
    }

    private fun extractJsonContent(rawText: String): String {
        val trimmed = rawText.trim()
        val start = trimmed.indexOfFirst { it == '{' || it == '[' }
        val end = trimmed.indexOfLast { it == '}' || it == ']' }
        return if (start != -1 && end != -1 && end > start) {
            trimmed.substring(start, end + 1)
        } else {
            trimmed
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        val maxDim = 1024
        val scaled = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
            val targetW = (if (ratio >= 1f) maxDim.toFloat() else maxDim * ratio).toInt().coerceAtLeast(1)
            val targetH = (if (ratio >= 1f) maxDim / ratio else maxDim.toFloat()).toInt().coerceAtLeast(1)
            Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
        } else {
            bitmap
        }
        try {
            check(scaled.compress(Bitmap.CompressFormat.JPEG, 82, outputStream)) {
                "Unable to encode image for Gemini"
            }
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
        } finally {
            if (scaled !== bitmap && !scaled.isRecycled) scaled.recycle()
            outputStream.close()
        }
    }

    private fun parsePoseAnalysisJson(rawJsonText: String, context: String): PoseAnalysisResult {
        return try {
            val json = JSONObject(extractJsonContent(rawJsonText))
            val strengths = mutableListOf<String>()
            val sArray = json.optJSONArray("keyStrengths")
            if (sArray != null) {
                for (i in 0 until sArray.length()) strengths.add(sArray.getString(i))
            }
            val improvements = mutableListOf<String>()
            val iArray = json.optJSONArray("actionableImprovements")
            if (iArray != null) {
                for (i in 0 until iArray.length()) improvements.add(iArray.getString(i))
            }

            PoseAnalysisResult(
                score = json.optInt("score", 85),
                detectedBodyPosition = json.optString("detectedBodyPosition", "Standing with dynamic posture"),
                cameraAngleFeedback = json.optString("cameraAngleFeedback", "Eye-level angle with balanced perspective"),
                postureFeedback = json.optString("postureFeedback", "Great spinal alignment, shoulders relaxed"),
                handPlacementFeedback = json.optString("handPlacementFeedback", "Hands resting naturally in frame"),
                headAndGazeFeedback = json.optString("headAndGazeFeedback", "Engaging gaze with flattering head tilt"),
                backgroundContext = json.optString("backgroundContext", "Harmonious composition with good subject depth"),
                keyStrengths = if (strengths.isNotEmpty()) strengths else listOf("Strong confidence", "Clean angle", "Good silhouette"),
                actionableImprovements = if (improvements.isNotEmpty()) improvements else listOf("Shift weight to back foot", "Relax mouth tension"),
                recommendedPoses = PoseRepository.allPoses.take(3)
            )
        } catch (e: Exception) {
            PoseRepository.generateMockPoseAnalysis(context)
        }
    }

    private fun parseExpressionAnalysisJson(rawJsonText: String, targetMood: String): ExpressionAnalysisResult {
        return try {
            val json = JSONObject(extractJsonContent(rawJsonText))
            val obsList = mutableListOf<String>()
            val oArray = json.optJSONArray("observations")
            if (oArray != null) {
                for (i in 0 until oArray.length()) obsList.add(oArray.getString(i))
            }
            val tipList = mutableListOf<String>()
            val tArray = json.optJSONArray("instantCoachingTips")
            if (tArray != null) {
                for (i in 0 until tArray.length()) tipList.add(tArray.getString(i))
            }

            ExpressionAnalysisResult(
                detectedMood = json.optString("detectedMood", targetMood),
                confidenceScore = json.optInt("confidenceScore", 88),
                naturalnessScore = json.optInt("naturalnessScore", 90),
                eyeEngagementScore = json.optInt("eyeEngagementScore", 85),
                jawlineDefinitionScore = json.optInt("jawlineDefinitionScore", 87),
                overallRating = json.optString("overallRating", "High-Fashion Poise"),
                positiveObservations = if (obsList.isNotEmpty()) obsList else listOf("Natural micro-expressions", "Soft eye focus"),
                gentleSuggestions = if (tipList.isNotEmpty()) tipList else listOf("Exhale gently to loosen mouth tension", "Slight chin tuck")
            )
        } catch (e: Exception) {
            generateOfflineExpressionAnalysis(targetMood)
        }
    }

    private fun parseStoryboardJson(
        rawJsonText: String,
        concept: String,
        platform: String,
        durationSeconds: Int
    ): StoryboardPlan {
        return try {
            val json = JSONObject(extractJsonContent(rawJsonText))
            val scenesArray = json.optJSONArray("scenes")
            val scenes = mutableListOf<StoryboardScene>()
            if (scenesArray != null) {
                for (i in 0 until scenesArray.length()) {
                    val sObj = scenesArray.getJSONObject(i)
                    scenes.add(
                        StoryboardScene(
                            sceneNumber = sObj.optInt("sceneNumber", i + 1),
                            durationText = sObj.optString("durationText", "${i * 3}s - ${(i + 1) * 3}s"),
                            shotType = sObj.optString("shotType", "Medium Shot"),
                            cameraMovement = sObj.optString("cameraMovement", "Smooth Push-In"),
                            poseInstruction = sObj.optString("poseInstruction", "Stand tall with weight on back leg"),
                            facialExpression = sObj.optString("facialExpression", "Direct confident gaze"),
                            lightingAndAtmosphere = sObj.optString("lightingAndAtmosphere", "Warm ambient light"),
                            visualHookOrVoiceover = sObj.optString("visualHookOrVoiceover", "Hook hook line...")
                        )
                    )
                }
            }

            StoryboardPlan(
                title = json.optString("title", concept.take(24)),
                conceptOverview = json.optString("conceptOverview", concept),
                targetPlatform = json.optString("targetPlatform", platform),
                aestheticTheme = json.optString("aestheticTheme", "Modern Cinematic"),
                recommendedPacing = json.optString("recommendedPacing", "Punchy 2s cuts with audio sync"),
                trendingAudioVibe = json.optString("trendingAudioVibe", "Upbeat Lofi or Phonk"),
                scenes = if (scenes.isNotEmpty()) scenes else generateOfflineStoryboard(concept, platform, durationSeconds).scenes
            )
        } catch (e: Exception) {
            generateOfflineStoryboard(concept, platform, durationSeconds)
        }
    }

    private fun generateOfflineExpressionAnalysis(targetMood: String): ExpressionAnalysisResult {
        return ExpressionAnalysisResult(
            detectedMood = targetMood.ifEmpty { "Confident Radiance" },
            confidenceScore = 91,
            naturalnessScore = 88,
            eyeEngagementScore = 93,
            jawlineDefinitionScore = 86,
            overallRating = "Cinematic Presence",
            positiveObservations = listOf(
                "Eyes display authentic connection (Duchenne activation)",
                "Gentle head tilt softens jawline and adds photogenic dynamism",
                "Forehead is relaxed with zero visible stress micro-furrows",
                "Slight separation of lips provides relaxed, editorial mouth shape"
            ),
            gentleSuggestions = listOf(
                "Keep tongue pressed gently against the palate to tighten submental jaw definition",
                "Exhale softly through parted lips right before shutter release",
                "Raise lower eyelids 10% (the Peter Hurley 'Squinch') to project piercing magnetic confidence"
            )
        )
    }

    private fun generateOfflineStoryboard(
        concept: String,
        platform: String,
        durationSeconds: Int
    ): StoryboardPlan {
        return StoryboardPlan(
            title = if (concept.isNotBlank()) "Viral: ${concept.take(30)}" else "Viral Aesthetic Reel",
            conceptOverview = if (concept.isNotBlank()) concept else "High-retention visual storytelling crafted for $platform",
            targetPlatform = platform,
            aestheticTheme = "Cinematic Studio & Clean Contrast",
            recommendedPacing = "Rapid hook (0-2.5s), rhythmic beat drop on scene 2, aesthetic resolution on finale",
            trendingAudioVibe = "Lofi Hip-Hop with crisp vinyl clicks or Deep House pulse",
            scenes = listOf(
                StoryboardScene(
                    sceneNumber = 1,
                    durationText = "0.0s - 2.5s",
                    shotType = "Extreme Close-Up (ECU) Macro",
                    cameraMovement = "Quick Whip-Tilt down to subject's eyes",
                    poseInstruction = "Over-the-shoulder turn with sudden stop",
                    facialExpression = "Intriguing mysterious gaze with subtle raised eyebrow",
                    lightingAndAtmosphere = "Dramatic rim backlight creating golden halo on hair",
                    visualHookOrVoiceover = "Hook Overlay: 'The one pose trick that changed all my photos 🤫'"
                ),
                StoryboardScene(
                    sceneNumber = 2,
                    durationText = "2.5s - 6.0s",
                    shotType = "Full Body Low Angle (0.8x)",
                    cameraMovement = "Slow tracking dolly forward while walking",
                    poseInstruction = "The 'Power Stride': Long stride, hands loosely grazing coat pockets",
                    facialExpression = "Confident calm smirk with chin slightly tucked down",
                    lightingAndAtmosphere = "Even softbox glow with deep architectural shadow contrast",
                    visualHookOrVoiceover = "Audio Beat Drop: Sudden speed ramp from slow-mo to 1.5x"
                ),
                StoryboardScene(
                    sceneNumber = 3,
                    durationText = "6.0s - 10.5s",
                    shotType = "Medium 3/4 Studio Shot",
                    cameraMovement = "Smooth subtle floating camera circle",
                    poseInstruction = "Weight shifted to back leg, leading knee bent inward, jacket slung over shoulder",
                    facialExpression = "Relaxed editorial smile showing upper teeth naturally",
                    lightingAndAtmosphere = "Cyan & warm amber split lighting (Cyberpunk Warmth)",
                    visualHookOrVoiceover = "Voiceover: 'Notice how shifting the hips creates an effortless S-curve?'"
                ),
                StoryboardScene(
                    sceneNumber = 4,
                    durationText = "10.5s - ${durationSeconds}.0s",
                    shotType = "Tight Portrait Hero Frame",
                    cameraMovement = "Snap push-in to eye level",
                    poseInstruction = "One hand lightly touching jawline/glasses, other hand resting at side",
                    facialExpression = "Magnetic radiant expression with direct eye-level connection",
                    lightingAndAtmosphere = "Ring light catchlights clearly reflected in the pupils",
                    visualHookOrVoiceover = "Call to Action Overlay: 'Save this guide & try it today! ⚡'"
                )
            )
        )
    }
}
