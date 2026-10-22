package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.model.WireframeType
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonViolet

@Composable
fun PoseWireframeCanvas(
    wireframeType: WireframeType,
    modifier: Modifier = Modifier,
    alpha: Float = 0.85f,
    showGrid: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wireframe_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Optional Rule of Thirds camera grid lines
        if (showGrid) {
            val gridColor = Color.White.copy(alpha = 0.12f * alpha)
            val strokeDashed = Stroke(
                width = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
            )
            // Vertical thirds
            drawLine(gridColor, Offset(w / 3f, 0f), Offset(w / 3f, h), strokeWidth = 1.5f)
            drawLine(gridColor, Offset(w * 2f / 3f, 0f), Offset(w * 2f / 3f, h), strokeWidth = 1.5f)
            // Horizontal thirds
            drawLine(gridColor, Offset(0f, h / 3f), Offset(w, h / 3f), strokeWidth = 1.5f)
            drawLine(gridColor, Offset(0f, h * 2f / 3f), Offset(w, h * 2f / 3f), strokeWidth = 1.5f)
        }

        // Generate normalized skeleton nodes based on wireframe type
        val nodes = getSkeletonNodes(wireframeType, w, h)

        val neonGradient = Brush.linearGradient(
            colors = listOf(
                ElectricCyan.copy(alpha = alpha),
                NeonViolet.copy(alpha = alpha)
            ),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        )

        // Draw connections
        val strokeWidth = 4f
        for ((p1, p2) in nodes.connections) {
            drawLine(
                brush = neonGradient,
                start = p1,
                end = p2,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        // Draw head circle
        drawCircle(
            brush = neonGradient,
            radius = nodes.headRadius * pulse,
            center = nodes.headCenter,
            style = Stroke(width = 3.5f)
        )

        // Draw glowing joint dots
        for (joint in nodes.joints) {
            // Outer glow
            drawCircle(
                color = ElectricCyan.copy(alpha = 0.35f * alpha),
                radius = 8f * pulse,
                center = joint
            )
            // Inner core
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = 4f,
                center = joint
            )
        }
    }
}

private data class SkeletonData(
    val headCenter: Offset,
    val headRadius: Float,
    val joints: List<Offset>,
    val connections: List<Pair<Offset, Offset>>
)

private fun getSkeletonNodes(type: WireframeType, w: Float, h: Float): SkeletonData {
    return when (type) {
        WireframeType.WALKING_STRIDE, WireframeType.STANDING_CASUAL -> {
            val head = Offset(w * 0.5f, h * 0.18f)
            val neck = Offset(w * 0.5f, h * 0.25f)
            val leftShoulder = Offset(w * 0.40f, h * 0.28f)
            val rightShoulder = Offset(w * 0.60f, h * 0.28f)
            val leftElbow = Offset(w * 0.35f, h * 0.40f)
            val rightElbow = Offset(w * 0.63f, h * 0.41f)
            val leftWrist = Offset(w * 0.38f, h * 0.52f)
            val rightWrist = Offset(w * 0.60f, h * 0.53f)

            val spine = Offset(w * 0.51f, h * 0.42f)
            val leftHip = Offset(w * 0.44f, h * 0.53f)
            val rightHip = Offset(w * 0.56f, h * 0.52f)

            val leftKnee = Offset(w * 0.42f, h * 0.70f)
            val rightKnee = Offset(w * 0.60f, h * 0.72f)
            val leftAnkle = Offset(w * 0.40f, h * 0.88f)
            val rightAnkle = Offset(w * 0.64f, h * 0.90f)

            val joints = listOf(
                neck, leftShoulder, rightShoulder, leftElbow, rightElbow, leftWrist, rightWrist,
                spine, leftHip, rightHip, leftKnee, rightKnee, leftAnkle, rightAnkle
            )

            val connections = listOf(
                head to neck,
                neck to leftShoulder, neck to rightShoulder,
                leftShoulder to leftElbow, leftElbow to leftWrist,
                rightShoulder to rightElbow, rightElbow to rightWrist,
                neck to spine,
                spine to leftHip, spine to rightHip,
                leftHip to leftKnee, leftKnee to leftAnkle,
                rightHip to rightKnee, rightKnee to rightAnkle
            )

            SkeletonData(head, h * 0.055f, joints, connections)
        }

        WireframeType.HEAD_TILT_SELFIE -> {
            val head = Offset(w * 0.50f, h * 0.38f)
            val neck = Offset(w * 0.50f, h * 0.56f)
            val leftShoulder = Offset(w * 0.30f, h * 0.65f)
            val rightShoulder = Offset(w * 0.70f, h * 0.63f)
            val handWrist = Offset(w * 0.65f, h * 0.48f)
            val handElbow = Offset(w * 0.78f, h * 0.72f)

            val joints = listOf(neck, leftShoulder, rightShoulder, handWrist, handElbow)
            val connections = listOf(
                head to neck,
                neck to leftShoulder,
                neck to rightShoulder,
                rightShoulder to handElbow,
                handElbow to handWrist
            )
            SkeletonData(head, h * 0.14f, joints, connections)
        }

        WireframeType.SITTING_LEAN, WireframeType.CROSS_LEGGED -> {
            val head = Offset(w * 0.48f, h * 0.25f)
            val neck = Offset(w * 0.49f, h * 0.33f)
            val leftShoulder = Offset(w * 0.38f, h * 0.37f)
            val rightShoulder = Offset(w * 0.62f, h * 0.36f)
            val leftElbow = Offset(w * 0.32f, h * 0.52f)
            val rightElbow = Offset(w * 0.68f, h * 0.50f)
            val leftWrist = Offset(w * 0.40f, h * 0.58f)
            val rightWrist = Offset(w * 0.60f, h * 0.58f)

            val spine = Offset(w * 0.50f, h * 0.48f)
            val leftHip = Offset(w * 0.42f, h * 0.62f)
            val rightHip = Offset(w * 0.58f, h * 0.62f)
            val leftKnee = Offset(w * 0.28f, h * 0.75f)
            val rightKnee = Offset(w * 0.72f, h * 0.75f)
            val leftAnkle = Offset(w * 0.48f, h * 0.82f)
            val rightAnkle = Offset(w * 0.54f, h * 0.82f)

            val joints = listOf(
                neck, leftShoulder, rightShoulder, leftElbow, rightElbow, leftWrist, rightWrist,
                spine, leftHip, rightHip, leftKnee, rightKnee, leftAnkle, rightAnkle
            )
            val connections = listOf(
                head to neck,
                neck to leftShoulder, neck to rightShoulder,
                leftShoulder to leftElbow, leftElbow to leftWrist,
                rightShoulder to rightElbow, rightElbow to rightWrist,
                neck to spine, spine to leftHip, spine to rightHip,
                leftHip to leftKnee, leftKnee to leftAnkle,
                rightHip to rightKnee, rightKnee to rightAnkle
            )
            SkeletonData(head, h * 0.065f, joints, connections)
        }

        else -> {
            // Default standing editorial posture
            val head = Offset(w * 0.5f, h * 0.16f)
            val neck = Offset(w * 0.5f, h * 0.23f)
            val leftShoulder = Offset(w * 0.38f, h * 0.26f)
            val rightShoulder = Offset(w * 0.62f, h * 0.25f)
            val leftElbow = Offset(w * 0.32f, h * 0.38f)
            val rightElbow = Offset(w * 0.68f, h * 0.36f)
            val leftWrist = Offset(w * 0.38f, h * 0.48f)
            val rightWrist = Offset(w * 0.62f, h * 0.46f)
            val spine = Offset(w * 0.52f, h * 0.39f)
            val leftHip = Offset(w * 0.43f, h * 0.50f)
            val rightHip = Offset(w * 0.57f, h * 0.49f)
            val leftKnee = Offset(w * 0.44f, h * 0.68f)
            val rightKnee = Offset(w * 0.59f, h * 0.69f)
            val leftAnkle = Offset(w * 0.42f, h * 0.88f)
            val rightAnkle = Offset(w * 0.61f, h * 0.89f)

            val joints = listOf(
                neck, leftShoulder, rightShoulder, leftElbow, rightElbow, leftWrist, rightWrist,
                spine, leftHip, rightHip, leftKnee, rightKnee, leftAnkle, rightAnkle
            )
            val connections = listOf(
                head to neck,
                neck to leftShoulder, neck to rightShoulder,
                leftShoulder to leftElbow, leftElbow to leftWrist,
                rightShoulder to rightElbow, rightElbow to rightWrist,
                neck to spine, spine to leftHip, spine to rightHip,
                leftHip to leftKnee, leftKnee to leftAnkle,
                rightHip to rightKnee, rightKnee to rightAnkle
            )
            SkeletonData(head, h * 0.055f, joints, connections)
        }
    }
}
