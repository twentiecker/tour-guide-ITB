package com.twentiecker.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BikeModel(
    val name: String,
    val description: String,
    val photo: String,
    val energy: String,
    val rating: String,
    val speed: String
) : Parcelable

