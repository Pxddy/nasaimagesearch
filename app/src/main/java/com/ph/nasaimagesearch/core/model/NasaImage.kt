package com.ph.nasaimagesearch.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NasaImage(
   val id: String,
   val title: String,
   val photographer: String?,
   val location: String?,
   val description: String?,
   val dateCreated: String,
   val imageUrl: String
) : Parcelable
