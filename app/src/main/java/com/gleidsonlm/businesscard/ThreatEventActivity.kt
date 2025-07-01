package com.gleidsonlm.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gleidsonlm.businesscard.model.ThreatEventData
import com.gleidsonlm.businesscard.ui.ThreatEventScreenContent
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreatEventActivity : ComponentActivity() {

    companion object {
        const val EXTRA_THREAT_EVENT_DATA = "com.gleidsonlm.businesscard.EXTRA_THREAT_EVENT_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val threatEventData: ThreatEventData? = intent.getParcelableExtra(EXTRA_THREAT_EVENT_DATA)

        setContent {
            BusinessCardTheme {
                ThreatEventScreenContent(threatEventData = threatEventData)
            }
        }
    }
}
