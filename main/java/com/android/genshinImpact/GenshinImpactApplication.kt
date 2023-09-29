package com.android.genshinImpact

import android.app.Application


class GenshinImpactApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CharacterRepository.initialize(this)
    }
}