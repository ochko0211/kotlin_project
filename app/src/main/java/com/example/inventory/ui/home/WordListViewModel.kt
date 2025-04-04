package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Word
import com.example.inventory.data.WordsRepository
import com.example.inventory.data.ChoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WordListUiState(
    val words: List<Word> = emptyList(),
    val currentWord: Word? = null,
    val currentIndex: Int = -1,
    val showEnglish: Boolean = true,
    val showMongolian: Boolean = true,
    val showBoth: Boolean = true,
    val showDeleteDialog: Boolean = false
)

class WordListViewModel(
    private val wordsRepository: WordsRepository,
    private val choiceRepository: ChoiceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordListUiState())
    val uiState: StateFlow<WordListUiState> = _uiState.asStateFlow()

    init {
        loadWords()
        loadPreferences()
    }



    private fun loadWords() {
        viewModelScope.launch {
            wordsRepository.getAllWordsStream().collect { words ->
                _uiState.update { currentState ->
                    currentState.copy(
                        words = words,
                        currentWord = words.firstOrNull(),
                        currentIndex = if (words.isNotEmpty()) 0 else -1
                    )
                }
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            choiceRepository.showEnglish.collect { show ->
                _uiState.update { currentState ->
                    currentState.copy(showEnglish = show)
                }
            }
        }

        viewModelScope.launch {
            choiceRepository.showMongolian.collect { show ->
                _uiState.update { currentState ->
                    currentState.copy(showMongolian = show)
                }
            }
        }

        viewModelScope.launch {
            choiceRepository.showBoth.collect { show ->
                _uiState.update { currentState ->
                    currentState.copy(showBoth = show)
                }
            }
        }
    }

    fun nextWord() {
        _uiState.update { currentState ->
            val nextIndex = (currentState.currentIndex + 1) % currentState.words.size
            currentState.copy(
                currentIndex = nextIndex,
                currentWord = currentState.words.getOrNull(nextIndex)
            )
        }
    }

    fun previousWord() {
        _uiState.update { currentState ->
            val prevIndex = if (currentState.currentIndex - 1 < 0) {
                currentState.words.size - 1
            } else {
                currentState.currentIndex - 1
            }
            currentState.copy(
                currentIndex = prevIndex,
                currentWord = currentState.words.getOrNull(prevIndex)
            )
        }
    }

    fun deleteCurrentWord() {
        viewModelScope.launch {
            _uiState.value.currentWord?.let { wordsRepository.deleteWord(it) }
        }
    }

    fun toggleShowEnglish() {
        _uiState.update { currentState ->
            currentState.copy(
                showEnglish = true,
                showMongolian = false,
                showBoth = false
            )
        }
    }

    fun toggleShowMongolian() {
        _uiState.update { currentState ->
            currentState.copy(
                showEnglish = false,
                showMongolian = true,
                showBoth = false
            )
        }
    }

    fun toggleShowBoth() {
        _uiState.update { currentState ->
            currentState.copy(
                showEnglish = false,
                showMongolian = false,
                showBoth = true
            )
        }
    }
    fun savePreferences() {
        viewModelScope.launch {
            val state = uiState.value
            choiceRepository.updateAllChoice(
                showEnglish = state.showEnglish,
                showMongolian = state.showMongolian,
                showBoth = state.showBoth && !state.showEnglish && !state.showMongolian
            )
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showDeleteDialog = show)
        }
    }
}