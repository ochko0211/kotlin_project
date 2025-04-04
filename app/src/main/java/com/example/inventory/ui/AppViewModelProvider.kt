//appviewmodelprovider
package com.example.inventory.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.WordApplication
import com.example.inventory.ui.edit.WordEditViewModel
import com.example.inventory.ui.home.WordListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApplication)
            val savedStateHandle = this.createSavedStateHandle()
            WordEditViewModel(
                savedStateHandle,
                application.container.wordsRepository
            )
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApplication)
            WordListViewModel(
                application.container.wordsRepository,
                application.container.choiceRepository
            )
        }
    }
}