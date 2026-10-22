package com.example.data.repository

import com.example.data.model.ExpressionGuide
import com.example.data.model.ExpressionMood
import com.example.data.model.PracticeStep

object ExpressionRepository {

    val guides = mapOf(
        ExpressionMood.CONFIDENT to ExpressionGuide(
            mood = ExpressionMood.CONFIDENT,
            eyeDirection = "Direct laser focus into the camera lens. Engage lower eyelids 15% (The Squinch) to convey sharp focus without looking alarmed.",
            eyebrowPosition = "Neutral, completely relaxed brow with zero vertical tension or furrowing.",
            lipsGuidance = "Part lips subtly by 2mm. Inhale gently through the mouth to eliminate masseter muscle clenching.",
            smileIntensity = 2,
            headTilt = "Bring forehead forward 1 inch and down by 5° to emphasize cheekbones and jawline contour.",
            postureGuidance = "Roll shoulders back and down 2 inches. Elongate spine and neck like a dancer.",
            breathingTip = "Exhale slowly through your nose right before clicking to relax trapezius tension.",
            encouragementNote = "You have an undeniable presence. Let that quiet internal confidence shine naturally."
        ),
        ExpressionMood.NATURAL_SMILE to ExpressionGuide(
            mood = ExpressionMood.NATURAL_SMILE,
            eyeDirection = "Engage the orbicularis oculi muscles to produce soft authentic Duchenne eye crinkles.",
            eyebrowPosition = "Gently arched upwards in welcoming warmth.",
            lipsGuidance = "Open natural smile revealing upper row of teeth. Keep lower teeth mostly hidden.",
            smileIntensity = 7,
            headTilt = "Slight 10° head tilt towards your dominant photogenic side.",
            postureGuidance = "Slight forward torso lean towards camera to convey approachability.",
            breathingTip = "Say 'Yoga' or 'Money' softly on exhale—never say 'Cheese' which creates stiff mouth corners.",
            encouragementNote = "Your authentic smile is contagious. Think of someone who makes you laugh effortlessly."
        ),
        ExpressionMood.SERIOUS_MODEL to ExpressionGuide(
            mood = ExpressionMood.SERIOUS_MODEL,
            eyeDirection = "Unblinking, intense laser gaze directly down the barrel of the camera lens.",
            eyebrowPosition = "Weighted, level brow projecting architectural composure.",
            lipsGuidance = "Completely relaxed resting lips; soft separation between upper and lower lips.",
            smileIntensity = 0,
            headTilt = "Turn head 30° into 3/4 profile with chin elevated 5° to catch directional key light.",
            postureGuidance = "Drop shoulders completely, push chest out softly to create sculpted collarbone highlights.",
            breathingTip = "Breathe in deeply through the nose and hold softly for 2 seconds during shutter release.",
            encouragementNote = "High-fashion geometry is all about quiet poise. You look striking and sculptural."
        ),
        ExpressionMood.CUTE_PLAYFUL to ExpressionGuide(
            mood = ExpressionMood.CUTE_PLAYFUL,
            eyeDirection = "Bright, wide, sparkling eyes looking with curiosity and spontaneity.",
            eyebrowPosition = "Raised with playful surprise and delight.",
            lipsGuidance = "Playful micro-pout, side smirk, or soft open smile.",
            smileIntensity = 6,
            headTilt = "Playful 20° head tilt with one shoulder slightly raised towards the ear.",
            postureGuidance = "Dynamic kinetic posture; keep shifting angles between camera clicks.",
            breathingTip = "Keep movements bouncy and quick with energetic rhythmic breathing.",
            encouragementNote = "Charming and full of life! Don't worry about being perfect—authenticity wins every time."
        ),
        ExpressionMood.ROMANTIC to ExpressionGuide(
            mood = ExpressionMood.ROMANTIC,
            eyeDirection = "Soft, tender, slightly lowered eyelids glancing warmly towards the lens.",
            eyebrowPosition = "Completely soft and relaxed.",
            lipsGuidance = "Gentle, hydrated lips with soft corners.",
            smileIntensity = 4,
            headTilt = "Head tilted softly towards one shoulder as if resting near a loved one.",
            postureGuidance = "Relaxed, curved spine with soft shoulders and open chest.",
            breathingTip = "Slow gentle sigh of contentment.",
            encouragementNote = "A dreamy, cinematic aura. Let your warmth radiate through the frame."
        ),
        ExpressionMood.MYSTERIOUS to ExpressionGuide(
            mood = ExpressionMood.MYSTERIOUS,
            eyeDirection = "Indirect gaze looking just 3 inches past the camera lens into negative space.",
            eyebrowPosition = "Softly contoured brow hinting at deep contemplation.",
            lipsGuidance = "Closed neutral lips with effortless composure (Mona Lisa micro-expression).",
            smileIntensity = 1,
            headTilt = "Turn face into split lighting so shadows sculpt half of your facial profile.",
            postureGuidance = "Still, poised, meditative posture.",
            breathingTip = "Deep slow diaphragmatic breathing.",
            encouragementNote = "Intriguing and magnetic. The audience will wonder what story lies behind your eyes."
        ),
        ExpressionMood.HAPPY_ENERGETIC to ExpressionGuide(
            mood = ExpressionMood.HAPPY_ENERGETIC,
            eyeDirection = "Wide open, vibrant eyes with joyful crinkles radiating infectious optimism.",
            eyebrowPosition = "Lifted with energetic excitement.",
            lipsGuidance = "Wide beaming smile; full laughter activation.",
            smileIntensity = 10,
            headTilt = "Chin lifted 10° with proud, enthusiastic head orientation.",
            postureGuidance = "Upright, expansive stance taking up confident space in the frame.",
            breathingTip = "Laugh out loud ('ha-ha') right before the countdown reaches zero.",
            encouragementNote = "Pure positive vibration! Your energy lights up the entire screen."
        ),
        ExpressionMood.CALM_RELAXED to ExpressionGuide(
            mood = ExpressionMood.CALM_RELAXED,
            eyeDirection = "Soft, serene focus; eyelids gently resting without strain.",
            eyebrowPosition = "Zero muscle tension; smooth, peaceful forehead.",
            lipsGuidance = "Resting naturally with teeth slightly separated inside mouth.",
            smileIntensity = 3,
            headTilt = "Centered head alignment balanced gracefully on the cervical spine.",
            postureGuidance = "Ground your feet flat; release all tension from neck and trapezoids.",
            breathingTip = "4-second inhale through nose, 6-second slow exhale through lips.",
            encouragementNote = "Grounded and peaceful. Your serenity creates a soothing, timeless portrait."
        ),
        ExpressionMood.PROFESSIONAL to ExpressionGuide(
            mood = ExpressionMood.PROFESSIONAL,
            eyeDirection = "Direct, engaging eye contact projecting competence, honesty, and authority.",
            eyebrowPosition = "Naturally positioned with approachable brow.",
            lipsGuidance = "Polite, closed or gentle tooth smile displaying executive poise.",
            smileIntensity = 5,
            headTilt = "Level chin aligned squarely with shoulders—neither tilted up nor down.",
            postureGuidance = "Straight executive spine with shoulders rolled back and hands open.",
            breathingTip = "Rhythmic, steady professional cadence.",
            encouragementNote = "Authoritative yet approachable. Perfect for LinkedIn, press kits, and keynote bios."
        ),
        ExpressionMood.CINEMATIC_DRAMATIC to ExpressionGuide(
            mood = ExpressionMood.CINEMATIC_DRAMATIC,
            eyeDirection = "Sultry heavy eyelids (Squinch) glancing through eyelashes into the key light.",
            eyebrowPosition = "One brow subtly arched 2mm higher than the other.",
            lipsGuidance = "Parted lips with slight asymmetrical micro-smirk.",
            smileIntensity = 3,
            headTilt = "Chin tucked down into chest by 10°, eyes looking up through the brow line.",
            postureGuidance = "Angular 3/4 turn towards directional rim light.",
            breathingTip = "Slow, whisper-soft exhale through mouth right as shutter activates.",
            encouragementNote = "Hollywood blockbuster energy. You look ready for the silver screen."
        )
    )

    val defaultPracticeSequence = listOf(
        PracticeStep(1, ExpressionMood.CALM_RELAXED, 4, "Breathe in... find your calm center 🌿"),
        PracticeStep(2, ExpressionMood.CONFIDENT, 4, "Engage the Squinch eye trick... strong jaw ✨"),
        PracticeStep(3, ExpressionMood.CINEMATIC_DRAMATIC, 4, "Tuck chin slightly... cinematic smolder 🎬"),
        PracticeStep(4, ExpressionMood.NATURAL_SMILE, 4, "Break into an authentic warm smile! 😊")
    )

    fun getGuide(mood: ExpressionMood): ExpressionGuide {
        return guides[mood] ?: guides.values.first()
    }
}
