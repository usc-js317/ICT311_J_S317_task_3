package com.org.workout.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(var id: Long, var title: String, var date: String, var place: String,
                var startTime: String, var endTime : String, var group: Int) : ViewModel(), Parcelable