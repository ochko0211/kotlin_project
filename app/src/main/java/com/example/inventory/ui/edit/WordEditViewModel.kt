package com.example.inventory.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.R
import com.example.inventory.data.Word
import com.example.inventory.data.WordsRepository
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


object WordEditDestination : NavigationDestination {
    override val route = "word_edit"
    override val titleRes = R.string.app_name
    const val wordIdArg = "wordId"
    val routeWithArgs = "$route/{$wordIdArg}"
    val routeWithoutArgs = route
}
data class WordEditUiState(
    val engWord: String = "",
    val monWord: String = "",
    val isEntryValid: Boolean = false
)

class WordEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val wordsRepository: WordsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordEditUiState())
    val uiState: StateFlow<WordEditUiState> = _uiState.asStateFlow()

    private val wordId: Int? = savedStateHandle[WordEditDestination.wordIdArg]

    init {
        if (wordId != null && wordId > 0) {
            loadWord(wordId)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    engWord = "",
                    monWord = "",
                    isEntryValid = false
                )
            }
        }
    }

    private fun loadWord(id: Int) {
        viewModelScope.launch {
            wordsRepository.getWordStream(id).collect { word ->
                word?.let {
                    _uiState.value = WordEditUiState(
                        engWord = it.engWord,
                        monWord = it.monWord,
                        isEntryValid = true
                    )
                }
            }
        }
    }

    fun updateEngWord(engWord: String) {
        _uiState.update { currentState ->
            currentState.copy(
                engWord = engWord,
                isEntryValid = engWord.isNotBlank() && currentState.monWord.isNotBlank()
            )
        }
    }

    fun updateMonWord(monWord: String) {
        _uiState.update { currentState ->
            currentState.copy(
                monWord = monWord,
                isEntryValid = monWord.isNotBlank() && currentState.engWord.isNotBlank()
            )
        }
    }

    suspend fun saveWord() {
        if (uiState.value.isEntryValid) {
            val word = Word(
                id = wordId ?: 0,
                engWord = uiState.value.engWord,
                monWord = uiState.value.monWord
            )
            if (wordId != null) {
                wordsRepository.updateWord(word)
            } else {
                wordsRepository.insertWord(word)
            }
        }
    }
}