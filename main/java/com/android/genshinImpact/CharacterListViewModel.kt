package com.android.genshinImpact

import androidx.lifecycle.ViewModel

class CharacterListViewModel: ViewModel() {

    private val characterRepository = CharacterRepository.get()

    val characterListLiveData = characterRepository.getCharacters()

    fun addCharacter(character: Character){
        characterRepository.addCharacter(character)
    }


}