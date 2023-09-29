package com.android.genshinImpact

import android.content.Context
import androidx.lifecycle.LiveData
import com.android.genshinImpact.database.CharacterDatabase
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

class CharacterRepository private constructor(context: Context) {

    private val database: CharacterDatabase = CharacterDatabase.getInstance(context)
    private val characterDao = database.characterDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun upDateCharacter(character: Character){
        executor.execute {
            characterDao.updateCharacter(character)
        }
    }

    fun addCharacter(character: Character){
        executor.execute {
            characterDao.addCharacter(character)
        }
    }

    fun deleteCharacter(character: Character) {
        executor.execute {
            characterDao.deleteCharacter(character)
        }
    }

    fun getPhotoFile(character: Character): File = File(filesDir, character.photoFileName)

    fun getCharacters(): LiveData<List<Character>> = characterDao.getCharacters()

    fun getCharacter(id: UUID): LiveData<Character?> = characterDao.getCharacter(id)

    companion object {
        private var INSTANCE: CharacterRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CharacterRepository(context)
            }
        }

        fun get(): CharacterRepository {
            return INSTANCE ?: throw IllegalStateException("CharacterRepository must be initialized.")
        }
    }
}
