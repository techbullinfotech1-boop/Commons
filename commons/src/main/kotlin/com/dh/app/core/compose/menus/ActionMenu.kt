package com.dh.app.core.compose.menus

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.dh.app.core.R
import com.dh.app.core.compose.alert_dialog.dialogBorder
import com.dh.app.core.compose.alert_dialog.dialogContainerColor
import com.dh.app.core.compose.components.SimpleDropDownMenuItem
import com.dh.app.core.compose.extensions.MyDevices
import com.dh.app.core.compose.extensions.rememberMutableInteractionSource
import com.dh.app.core.compose.theme.AppThemeSurface
import com.dh.app.core.compose.theme.Shapes
import com.dh.app.core.compose.theme.SimpleTheme


@Immutable
data class ActionItem(
    @StringRes
    val nameRes: Int,
    val icon: ImageVector? = null,
    val overflowMode: OverflowMode = OverflowMode.IF_NECESSARY,
    val doAction: () -> Unit,
    val iconColor: Color? = null
) {
    operator fun invoke() = doAction()
}


@Immutable
enum class OverflowMode {
    NEVER_OVERFLOW, IF_NECESSARY, ALWAYS_OVERFLOW, NOT_SHOWN
}


@Composable
fun ActionMenu(
    items: ImmutableList<ActionItem>,
    numIcons: Int = 2,
    isMenuVisible: Boolean,
    iconsColor: Color? = null,
    onMenuToggle: (isVisible: Boolean) -> Unit
) {
    if (items.isEmpty()) {
        return
    }
    val (appbarActions, overflowActions) = remember(items, numIcons) {
        separateIntoIconAndOverflow(items, numIcons)
    }
    for (item in appbarActions) {
        key(item.hashCode()) {
            val name = stringResource(item.nameRes)
            if (item.icon != null) {
                val iconButtonColor = when {
                    iconsColor != null -> iconsColor
                    item.iconColor != null -> item.iconColor
                    else -> LocalContentColor.current
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(spacingBetweenTooltipAndAnchor = 18.dp),
                    tooltip = {
                        PlainTooltip(shape = Shapes.extraLarge) {
                            Text(
                                text = name,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(SimpleTheme.dimens.padding.medium),
                            )
                        }
                    },
                    state = rememberTooltipState(),
                ) {
                    ActionIconButton(
                        onClick = item.doAction,
                        contentColor = iconButtonColor,
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = name
                        )
                    }
                }
            } else {
                SimpleDropDownMenuItem(onClick = item.doAction, text = name)
            }
        }
    }

    if (overflowActions.isNotEmpty()) {
        TooltipBox(
            tooltip = {
                PlainTooltip(shape = Shapes.extraLarge) {
                    Text(
                        text = stringResource(id = R.string.more_options),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(SimpleTheme.dimens.padding.medium),
                    )
                }
            },
            state = rememberTooltipState(),
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(spacingBetweenTooltipAndAnchor = 18.dp),
        ) {
            ActionIconButton(
                onClick = { onMenuToggle(true) },
                contentColor = iconsColor ?: LocalContentColor.current,
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.more_options))
            }
        }
        DropdownMenu(
            modifier = Modifier
                .background(dialogContainerColor)
                .dialogBorder(),
            expanded = isMenuVisible,
            onDismissRequest = { onMenuToggle(false) },
        ) {
            for (item in overflowActions) {
                key(item.hashCode()) {
                    SimpleDropDownMenuItem(text = item.nameRes, onClick = {
                        onMenuToggle(false)
                        item.doAction()
                    })
                }
            }
        }
    }
}

@Composable
internal fun ActionIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .combinedClickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    radius = 40.dp / 2
                ),
                onLongClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

private fun separateIntoIconAndOverflow(
    items: List<ActionItem>,
    numIcons: Int
): Pair<List<ActionItem>, List<ActionItem>> {
    var (iconCount, overflowCount, preferIconCount) = Triple(0, 0, 0)
    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> iconCount++
            OverflowMode.IF_NECESSARY -> preferIconCount++
            OverflowMode.ALWAYS_OVERFLOW -> overflowCount++
            OverflowMode.NOT_SHOWN -> {}
        }
    }

    val needsOverflow = ((iconCount + preferIconCount) > numIcons) || (overflowCount > 0)
    val actionIconSpace = numIcons - (if (needsOverflow) 1 else 0)

    val iconActions = mutableListOf<ActionItem>()
    val overflowActions = mutableListOf<ActionItem>()

    var iconsAvailableBeforeOverflow = actionIconSpace - iconCount
    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> {
                iconActions.add(item)
            }

            OverflowMode.ALWAYS_OVERFLOW -> {
                overflowActions.add(item)
            }

            OverflowMode.IF_NECESSARY -> {
                if (iconsAvailableBeforeOverflow > 0) {
                    iconActions.add(item)
                    iconsAvailableBeforeOverflow--
                } else {
                    overflowActions.add(item)
                }
            }

            OverflowMode.NOT_SHOWN -> {
                // skip
            }
        }
    }
    return Pair(iconActions, overflowActions)
}

@MyDevices
@Composable
private fun ActionMenuPreview() {
    AppThemeSurface {
        val actionMenus = remember {
            listOf(
                ActionItem(R.string.add_a_blocked_number, icon = Icons.Filled.Add, doAction = { }),
                ActionItem(R.string.import_blocked_numbers, doAction = {}, overflowMode = OverflowMode.ALWAYS_OVERFLOW),
                ActionItem(R.string.export_blocked_numbers, doAction = { }, overflowMode = OverflowMode.ALWAYS_OVERFLOW),
            ).toImmutableList()
        }
        ActionMenu(items = actionMenus, numIcons = 2, isMenuVisible = true, onMenuToggle = { }, iconsColor = Color.Black)
    }
}
