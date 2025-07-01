package com.gleidsonlm.businesscard.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThreatEventData(
    val message: String?
) : Parcelable
