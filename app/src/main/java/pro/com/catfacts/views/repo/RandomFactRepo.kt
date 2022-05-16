package pro.com.catfacts.views.repo

import kotlinx.coroutines.flow.Flow
import pro.com.catfacts.views.adapter.RandomCatFacts

interface RandomFactRepo {
    fun fetchRandomFact(): Flow<RandomCatFacts>
    fun delete(catId: String): Flow<String>
    suspend fun fetchDetails(): List<RandomCatFacts>
}