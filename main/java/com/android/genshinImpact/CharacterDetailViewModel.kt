package com.android.genshinImpact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class CharacterDetailViewModel: ViewModel() {

    private val characterRepository =  CharacterRepository.get()
    private val characterIdLiveData = MutableLiveData<UUID>()



    var characterLiveData: LiveData<Character?> =
        Transformations.switchMap(characterIdLiveData){characterId->
            characterRepository.getCharacter(characterId)
        }

    fun loadCharacter(characterId: UUID){
        characterIdLiveData.value = characterId
    }

    fun saveCharacter(character: Character){
        characterRepository.upDateCharacter(character)
    }

    fun getPhotoFile(character: Character): File {
        return characterRepository.getPhotoFile(character)
    }

    fun deleteCharacter(character: Character) {
        characterRepository.deleteCharacter(character)
    }
}