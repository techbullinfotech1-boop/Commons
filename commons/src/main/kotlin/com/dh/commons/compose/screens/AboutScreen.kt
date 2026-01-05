package com.dh.commons.compose.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dh.commons.R
import com.dh.commons.compose.extensions.MyDevices
import com.dh.commons.compose.lists.SimpleColumnScaffold
import com.dh.commons.compose.settings.SettingsGroup
import com.dh.commons.compose.settings.SettingsHorizontalDivider
import com.dh.commons.compose.settings.SettingsListItem
import com.dh.commons.compose.settings.SettingsTitleTextComponent
import com.dh.commons.compose.theme.AppThemeSurface
import com.dh.commons.compose.theme.SimpleTheme

private val titleStartPadding = Modifier.padding(start = 40.dp)

/* ---------------- SCREEN ---------------- */

@Composable
internal fun AboutScreen(
    goBack: () -> Unit,
    helpUsSection: @Composable () -> Unit,
    aboutSection: @Composable () -> Unit,
    otherSection: @Composable () -> Unit,
) {
    SimpleColumnScaffold(
        title = stringResource(id = R.string.about),
        goBack = goBack
    ) {
        aboutSection()
        helpUsSection()
        otherSection()
    }
}

/* ---------------- HELP US ---------------- */

@Composable
internal fun HelpUsSection(
    onRateThisAppClick: () -> Unit,
    onInviteClick: () -> Unit,
    showRateUs: Boolean,
    showInvite: Boolean,
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.help_us),
            modifier = titleStartPadding
        )
    }) {
        if (showRateUs) {
            TwoLinerTextItem(
                text = stringResource(id = R.string.rate_this_app),
                icon = R.drawable.ic_star_outline_vector,
                click = onRateThisAppClick
            )
        }

        if (showInvite) {
            TwoLinerTextItem(
                text = stringResource(id = R.string.invite_friends),
                icon = R.drawable.ic_share_outline_vector,
                click = onInviteClick
            )
        }

        SettingsHorizontalDivider()
    }
}

/* ---------------- SUPPORT ---------------- */

@Composable
internal fun AboutSection(
    setupFAQ: Boolean,
    onFAQClick: () -> Unit,
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.support),
            modifier = titleStartPadding
        )
    }) {
        if (setupFAQ) {
            TwoLinerTextItem(
                click = onFAQClick,
                text = stringResource(id = R.string.frequently_asked_questions),
                icon = R.drawable.ic_help_outline_vector
            )
        }

        SettingsHorizontalDivider()
    }
}

/* ---------------- OTHER ---------------- */

@Composable
internal fun OtherSection(
    showMoreApps: Boolean,
    onMoreAppsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    versionName: String,
    packageName: String,
    onVersionClick: () -> Unit,
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.other),
            modifier = titleStartPadding
        )
    }) {
        if (showMoreApps) {
            TwoLinerTextItem(
                click = onMoreAppsClick,
                text = stringResource(id = R.string.more_apps),
                icon = R.drawable.ic_apps_vector
            )
        }

        TwoLinerTextItem(
            click = onPrivacyPolicyClick,
            text = stringResource(id = R.string.privacy_policy),
            icon = R.drawable.ic_policy_outline_vector
        )

        SettingsListItem(
            tint = SimpleTheme.colorScheme.onSurface,
            click = onVersionClick,
            text = versionName,
            description = packageName,
            icon = R.drawable.ic_info_outline_vector,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        SettingsHorizontalDivider()
    }
}

/* ---------------- COMMON ITEM ---------------- */

@Composable
internal fun TwoLinerTextItem(
    text: String,
    icon: Int,
    click: () -> Unit
) {
    SettingsListItem(
        tint = SimpleTheme.colorScheme.onSurface,
        click = click,
        text = text,
        icon = icon,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

/* ---------------- PREVIEW ---------------- */

@MyDevices
@Composable
private fun AboutScreenPreview() {
    AppThemeSurface {
        AboutScreen(
            goBack = {},
            helpUsSection = {
                HelpUsSection(
                    onRateThisAppClick = {},
                    onInviteClick = {},
                    showRateUs = true,
                    showInvite = true
                )
            },
            aboutSection = {
                AboutSection(
                    setupFAQ = true,
                    onFAQClick = {}
                )
            }
        ) {
            OtherSection(
                showMoreApps = true,
                onMoreAppsClick = {},
                onPrivacyPolicyClick = {},
                versionName = "5.0.4",
                packageName = "com.dh.commons.samples",
                onVersionClick = {}
            )
        }
    }
}
