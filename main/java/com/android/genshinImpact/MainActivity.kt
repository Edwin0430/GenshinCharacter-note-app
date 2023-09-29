package com.android.genshinImpact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity(),
    CharacterListFragment.Callbacks, TimePickerFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CharacterListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCharacterSelected(characterId: UUID) {
        val fragment = CharacterFragment.newInstance(characterId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onTimeSelected(hourOfDay: Int, minute: Int) {
        // Handle the selected time here
        // You can use this function to communicate the selected time back to the CharacterFragment
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is CharacterFragment) {
            fragment.onTimeSelected(hourOfDay, minute)
        }
    }
}
