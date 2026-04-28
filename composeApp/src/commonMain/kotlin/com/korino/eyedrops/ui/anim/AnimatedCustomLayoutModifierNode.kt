package com.korino.eyedrops.ui.anim

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.ApproachMeasureScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round

@OptIn(ExperimentalAnimatableApi::class)
class AnimatedCustomLayoutModifierNode(
    var lookaheadScope: LookaheadScope,
) : ApproachLayoutModifierNode, Modifier.Node() {

    private val sizeAnimation: DeferredTargetAnimation<IntSize, AnimationVector2D> =
        DeferredTargetAnimation(IntSize.VectorConverter)

    private val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> =
        DeferredTargetAnimation(IntOffset.VectorConverter)

    // LookaheadScope가 계산한 최종 size를 목표로 등록하고,
    // 아직 그 size에 도달하지 않았으면 true → Compose가 approachMeasure를 계속 호출
    override fun isMeasurementApproachInProgress(lookaheadSize: IntSize): Boolean {
        sizeAnimation.updateTarget(lookaheadSize, coroutineScope)
        return !sizeAnimation.isIdle
    }

    // LookaheadScope가 계산한 최종 position을 목표로 등록하고,
    // 아직 그 위치에 도달하지 않았으면 true → Compose가 approachMeasure의 place 블록을 계속 호출
    override fun Placeable.PlacementScope.isPlacementApproachInProgress(
        lookaheadCoordinates: LayoutCoordinates
    ): Boolean {
//        val target = with(lookaheadScope) {
//            lookaheadScopeCoordinates
//                .localLookaheadPositionOf(lookaheadCoordinates)
//                .round()
//        }
//        offsetAnimation.updateTarget(target, coroutineScope)
//        return !offsetAnimation.isIdle
        return false
    }

    // 매 프레임마다 호출: 현재 애니메이션 중인 size로 measure하고, 애니메이션 중인 offset에 place
    override fun ApproachMeasureScope.approachMeasure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val animatedSize = sizeAnimation.updateTarget(lookaheadSize, coroutineScope)
        val placeable = measurable.measure(
            Constraints.fixed(animatedSize.width, animatedSize.height)
        )
        return layout(placeable.width, placeable.height) {
            val coordinates = coordinates
            if (coordinates != null) {
                val target = with(lookaheadScope) {
                    lookaheadScopeCoordinates
                        .localLookaheadPositionOf(coordinates)
                        .round()
                }
                val animatedOffset = offsetAnimation.updateTarget(target, coroutineScope)
                val placementOffset = with(lookaheadScope) {
                    lookaheadScopeCoordinates
                        .localPositionOf(coordinates, Offset.Zero)
                        .round()
                }
                val (x, y) = animatedOffset - placementOffset
                placeable.place(x, y)
            } else {
                placeable.place(0, 0)
            }
        }
    }
}

@OptIn(ExperimentalAnimatableApi::class)
data class AnimatePlacementNodeElement(
    val lookaheadScope: LookaheadScope
) : ModifierNodeElement<AnimatedCustomLayoutModifierNode>() {

    override fun create(): AnimatedCustomLayoutModifierNode {
        return AnimatedCustomLayoutModifierNode(lookaheadScope)
    }

    override fun update(node: AnimatedCustomLayoutModifierNode) {
        node.lookaheadScope = lookaheadScope
    }
}