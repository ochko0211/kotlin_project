package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class OfflineWordsRepository(private val wordDao: WordDao) : WordsRepository {
    override fun getAllWordsStream(): Flow<List<Word>> = wordDao.getAllWords()
    override fun getWordStream(id: Int): Flow<Word?> = wordDao.getWordById(id)
    override suspend fun insertWord(word: Word): Long = wordDao.insert(word)
    override suspend fun deleteWord(word: Word) = wordDao.delete(word)
    override suspend fun updateWord(word: Word) = wordDao.update(word)
}