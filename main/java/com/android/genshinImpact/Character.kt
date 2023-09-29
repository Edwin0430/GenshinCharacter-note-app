package com.android.genshinImpact

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Character(@PrimaryKey val id: UUID = UUID.randomUUID(),
                     var title: String = "",
                     var date: Date = Date(),
                     var time: Date = Date(),
                     var isPyro:Boolean = false,
                     var isHydro:Boolean = false,
                     var isElectro:Boolean = false,
                     var isGeo:Boolean = false,
                     var isAnemo:Boolean = false,
                     var isCryo:Boolean = false,
                     var isDendro:Boolean = false,
                     var owner: String ="") {

    val photoFileName
        get() = "IMG_$id.jpg"

}