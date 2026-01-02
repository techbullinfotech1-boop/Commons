package com.dh.app.core.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import com.dh.app.core.compose.extensions.enableEdgeToEdgeSimple
import com.dh.app.core.compose.screens.FAQScreen
import com.dh.app.core.compose.theme.AppThemeSurface
import com.dh.app.core.helpers.APP_FAQ
import com.dh.app.core.models.FAQItem

class FAQActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val faqItems = remember { intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem> }
                FAQScreen(
                    goBack = ::finish,
                    faqItems = faqItems.toImmutableList()
                )
            }
        }
    }
}
