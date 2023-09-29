package com.android.genshinImpact.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.genshinImpact.Character
import java.util.*
import androidx.room.*

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getCharacters(): LiveData<List<Character>>

    @Query("SELECT * FROM character WHERE id = (:id)")
    fun getCharacter(id: UUID): LiveData<Character?>

    @Update
    fun updateCharacter(character: Character)

    @Insert
    fun addCharacter(character: Character)

    @Delete
    fun deleteCharacter(character: Character)
}
