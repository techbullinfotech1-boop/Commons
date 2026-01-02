package com.dh.app.core.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.dh.app.core.R
import com.dh.app.core.compose.extensions.enableEdgeToEdgeSimple
import com.dh.app.core.compose.screens.AboutScreen
import com.dh.app.core.compose.screens.AboutSection
import com.dh.app.core.compose.screens.HelpUsSection
import com.dh.app.core.compose.screens.OtherSection
import com.dh.app.core.compose.theme.AppThemeSurface
import com.dh.app.core.extensions.baseConfig
import com.dh.app.core.extensions.getStoreUrl
import com.dh.app.core.extensions.launchAppRatingPage
import com.dh.app.core.extensions.launchMoreAppsFromUsIntent
import com.dh.app.core.extensions.launchViewIntent
import com.dh.app.core.extensions.toast
import com.dh.app.core.helpers.APP_FAQ
import com.dh.app.core.helpers.APP_ICON_IDS
import com.dh.app.core.helpers.APP_LAUNCHER_NAME
import com.dh.app.core.helpers.APP_NAME
import com.dh.app.core.helpers.APP_PACKAGE_NAME
import com.dh.app.core.helpers.APP_VERSION_NAME
import com.dh.app.core.models.FAQItem

class AboutActivity : BaseComposeActivity() {

    private val appName get() = intent.getStringExtra(APP_NAME) ?: ""

    private var firstVersionClickTS = 0L
    private var clicksSinceFirstClick = 0

    companion object {
        private const val EASTER_EGG_TIME_LIMIT = 3000L
        private const val EASTER_EGG_REQUIRED_CLICKS = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()

        setContent {
            AppThemeSurface {
                AboutScreen(
                    goBack = ::finish,

                    helpUsSection = {
                        HelpUsSection(
                            onRateThisAppClick = ::onRateThisAppClick,
                            onInviteClick = ::onInviteClick,
                            showRateUs = true,
                            showInvite = true
                        )
                    },

                    aboutSection = {
                        if (showFAQ()) {
                            AboutSection(
                                setupFAQ = true,
                                onFAQClick = ::launchFAQActivity
                            )
                        }
                    },

                    otherSection = {
                        val (versionName, packageName) = getPackageInfo()
                        OtherSection(
                            showMoreApps = true,
                            onMoreAppsClick = ::launchMoreAppsFromUsIntent,
                            onPrivacyPolicyClick = ::onPrivacyPolicyClick,
                            versionName = versionName,
                            packageName = packageName,
                            onVersionClick = ::onVersionClick
                        )
                    }
                )
            }
        }
    }

    // -------------------- COMPOSABLE HELPERS --------------------

    @Composable
    private fun showFAQ(): Boolean =
        remember {
            !(intent.getSerializableExtra(APP_FAQ) as? ArrayList<FAQItem>).isNullOrEmpty()
        }

    @Composable
    private fun getPackageInfo(): Pair<String, String> {
        var versionName = remember { intent.getStringExtra(APP_VERSION_NAME) ?: "" }
        val packageName = remember { intent.getStringExtra(APP_PACKAGE_NAME) ?: "" }

        if (baseConfig.appId.removeSuffix(".debug").endsWith(".pro")) {
            versionName += " ${getString(R.string.pro)}"
        }

        val fullVersion = stringResource(R.string.version_placeholder, versionName)
        return Pair(fullVersion, packageName)
    }

    // -------------------- ACTIONS --------------------

    private fun launchFAQActivity() {
        val faqItems = intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem>
        Intent(applicationContext, FAQActivity::class.java).apply {
            putExtra(
                APP_ICON_IDS,
                intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList<String>()
            )
            putExtra(APP_LAUNCHER_NAME, intent.getStringExtra(APP_LAUNCHER_NAME) ?: "")
            putExtra(APP_FAQ, faqItems)
            startActivity(this)
        }
    }

    private fun onRateThisAppClick() {
        launchAppRatingPage()
    }

    private fun onInviteClick() {
        val storeUrl = getStoreUrl()
        val text = String.format(getString(R.string.share_text), appName, storeUrl)

        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, appName)
            putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(this, getString(R.string.invite_via)))
        }
    }

    //Add privacy Policy Link
    private fun onPrivacyPolicyClick() {
        val appId = baseConfig.appId
            .removeSuffix(".debug")
            .removeSuffix(".pro")
            .removePrefix("com.dh.")
        launchViewIntent("https://www.fossify.org/policy/$appId")
    }

    private fun onVersionClick() {
        if (firstVersionClickTS == 0L) {
            firstVersionClickTS = System.currentTimeMillis()
            Handler(Looper.getMainLooper()).postDelayed({
                firstVersionClickTS = 0L
                clicksSinceFirstClick = 0
            }, EASTER_EGG_TIME_LIMIT)
        }

        clicksSinceFirstClick++
        if (clicksSinceFirstClick >= EASTER_EGG_REQUIRED_CLICKS) {
            toast(R.string.hello)
            firstVersionClickTS = 0L
            clicksSinceFirstClick = 0
        }
    }
}
