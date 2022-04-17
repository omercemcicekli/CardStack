package com.omercemcicekli.cardstack

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal const val rotationValue = 45f

@ExperimentalMaterialApi
@Composable
fun CardStack(
    cardContents: List<@Composable (Int) -> Unit>,
    cardElevation: Dp = 1.dp,
    paddingBetweenCards: Dp = 8.dp,
    animationDuration: Int = 300,
    cardShape: Shape = MaterialTheme.shapes.medium,
    cardBorder: BorderStroke? = null,
    onCardClick: ((Int) -> Unit)? = null,
    orientation: Orientation = Orientation.Vertical()
) {
    checkPadding(paddingBetweenCards)

    val cardCount = cardContents.size

    val runAnimations = animationDuration > 0
    val coroutineScope = rememberCoroutineScope()

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val contentAlignment = getContentAlignment(orientation)

    val rotationValue = getRotation(orientation)

    Box(contentAlignment = contentAlignment) {
        cardContents.forEachIndexed { index, _ ->
            ShowCard(
                coroutineScope,
                runAnimations,
                selectedIndex,
                index,
                cardCount,
                paddingBetweenCards,
                animationDuration,
                rotationValue,
                orientation,
                cardElevation,
                cardShape,
                cardBorder,
                onCardClick,
                { cardContents[index](index) },
                { selectedIndex = it })
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CardStack(
    cardContent: @Composable (Int) -> Unit,
    cardCount: Int,
    cardElevation: Dp = 1.dp,
    paddingBetweenCards: Dp = 8.dp,
    animationDuration: Int = 300,
    cardShape: Shape = MaterialTheme.shapes.medium,
    cardBorder: BorderStroke? = null,
    onCardClick: ((Int) -> Unit)? = null,
    orientation: Orientation = Orientation.Vertical()
) {
    checkCardCount(cardCount)
    checkPadding(paddingBetweenCards)

    val runAnimations = animationDuration > 0
    val coroutineScope = rememberCoroutineScope()

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val contentAlignment = getContentAlignment(orientation)

    val rotationValue = getRotation(orientation)

    Box(contentAlignment = contentAlignment) {
        (0 until cardCount).forEachIndexed { index, _ ->
            ShowCard(
                coroutineScope,
                runAnimations,
                selectedIndex,
                index,
                cardCount,
                paddingBetweenCards,
                animationDuration,
                rotationValue,
                orientation,
                cardElevation,
                cardShape,
                cardBorder,
                onCardClick,
                { cardContent(index) },
                { selectedIndex = it })
        }
    }
}

fun checkCardCount(cardCount: Int) {
    if (cardCount < 1)
        throw IllegalArgumentException("Can't use 0 or negative card count.")
}

private fun checkPadding(paddingBetweenCards: Dp) {
    if (paddingBetweenCards == 0.dp)
        throw IllegalArgumentException("Can't use 0 dp padding for items.")
}

private fun getContentAlignment(orientation: Orientation): Alignment {
    return when(orientation) {
        is Orientation.Vertical -> {
            if(orientation.alignment == VerticalAlignment.TopToBottom)
                Alignment.TopCenter
            else
                Alignment.BottomCenter
        }

        is Orientation.Horizontal -> {
            if(orientation.alignment == HorizontalAlignment.StartToEnd)
                Alignment.CenterStart
            else
                Alignment.CenterEnd
        }
    }
}

private fun getRotation(orientation: Orientation): Float {
    return when(orientation) {
        is Orientation.Vertical -> if(orientation.animationStyle == VerticalAnimationStyle.ToRight)
            rotationValue
        else
            -rotationValue
        is Orientation.Horizontal -> if(orientation.animationStyle == HorizontalAnimationStyle.FromTop)
            -rotationValue
        else
            rotationValue
    }
}

@ExperimentalMaterialApi
@Composable
private fun ShowCard(
    coroutineScope: CoroutineScope,
    runAnimations: Boolean,
    selectedIndex: Int,
    index: Int,
    cardCount: Int,
    paddingBetweenCards: Dp,
    animationDuration: Int,
    rotationValue: Float,
    orientation: Orientation,
    cardElevation: Dp,
    cardShape: Shape,
    cardBorder: BorderStroke?,
    onCardClick: ((Int) -> Unit)? = null,
    composable: @Composable (Int) -> Unit,
    newIndexBlock: (Int) -> Unit
) {
    var pxValue = 0

    val padding = when {
        selectedIndex == index -> 0.dp
        selectedIndex < index -> ((index - selectedIndex) * paddingBetweenCards.value).dp
        selectedIndex > index -> ((cardCount - selectedIndex + index) * paddingBetweenCards.value).dp
        else -> throw IllegalStateException()
    }

    val paddingAnimation by animateDpAsState(padding, tween(animationDuration, easing = FastOutSlowInEasing))
    val offsetAnimation = remember { Animatable(0f) }
    val rotateAnimation = remember { Animatable(0f) }

    val offsetValues = when (orientation) {
        is Orientation.Vertical -> {
            IntOffset(
                if (orientation.animationStyle == VerticalAnimationStyle.ToRight)
                    offsetAnimation.value.toInt()
                else
                    -offsetAnimation.value.toInt(), 0
            )
        }
        is Orientation.Horizontal -> {
            IntOffset(
                0, if (orientation.animationStyle == HorizontalAnimationStyle.FromTop)
                    -offsetAnimation.value.toInt()
                else
                    offsetAnimation.value.toInt()
            )
        }
    }

    val paddingModifier = when {
        orientation is Orientation.Vertical && orientation.alignment == VerticalAlignment.TopToBottom -> PaddingValues(top = paddingAnimation)
        orientation is Orientation.Vertical && orientation.alignment == VerticalAlignment.BottomToTop -> PaddingValues(bottom = paddingAnimation)
        orientation is Orientation.Horizontal && orientation.alignment == HorizontalAlignment.StartToEnd -> PaddingValues(start = paddingAnimation)
        else -> PaddingValues(end = paddingAnimation)
    }

    Card(elevation = cardElevation,
        shape = cardShape,
        modifier = Modifier
            .padding(paddingModifier)
            .zIndex(-padding.value)
            .offset { offsetValues }
            .onSizeChanged {
                pxValue = if (orientation is Orientation.Vertical) {
                    if (pxValue > it.width)
                        pxValue
                    else
                        it.width
                } else {
                    if (pxValue > it.height)
                        pxValue
                    else
                        it.height
                }
            }
            .rotate(rotateAnimation.value),
        border = cardBorder,
        onClick =  {
            if(cardCount > 1 && selectedIndex == index) {
                onCardClick?.invoke(index)
                animateOnClick(coroutineScope, pxValue, runAnimations, animationDuration, rotationValue, index, cardCount, offsetAnimation, rotateAnimation, newIndexBlock)
            }
        }
    ) {
        composable.invoke(index)
    }
}

private fun animateOnClick(
    coroutineScope: CoroutineScope,
    pxValue: Int,
    runAnimations: Boolean,
    animationDuration: Int,
    rotationValue: Float,
    index: Int,
    cardCount: Int,
    offsetAnimation: Animatable<Float, AnimationVector1D>,
    rotateAnimation: Animatable<Float, AnimationVector1D>,
    newIndexBlock: (Int) -> Unit
) {
    val spec: TweenSpec<Float> = tween(animationDuration, easing = FastOutLinearInEasing)

    coroutineScope.launch {

        if (runAnimations)
            offsetAnimation.animateTo(pxValue.toFloat(), spec)

        val newIndex = if (cardCount > index + 1)
            index + 1
        else
            0

        newIndexBlock.invoke(newIndex)

        if (runAnimations) {
            rotateAnimation.animateTo(rotationValue, spec)
            launch { rotateAnimation.animateTo(0f, spec) }
            launch { offsetAnimation.animateTo(0f, spec) }
        }
    }
}



