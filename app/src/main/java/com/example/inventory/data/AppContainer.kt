    package com.example.inventory.data

    import android.content.Context

    interface AppContainer {
        val wordsRepository: WordsRepository
        val choiceRepository: ChoiceRepository
        val workManagerRepository: WorkManagerRepository
    }

    class AppDataContainer(private val context: Context) : AppContainer {
        override val wordsRepository: WordsRepository by lazy {
            OfflineWordsRepository(WordDatabase.getDatabase(context).wordDao())
        }

        override val choiceRepository: ChoiceRepository by lazy {
            ChoiceRepository(context)
        }

        override val workManagerRepository: WorkManagerRepository by lazy {
            WorkManagerRepository(context)
        }
    }