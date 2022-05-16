package pro.com.catfacts.views.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.com.catfacts.model.DataMapper
import pro.com.catfacts.network.ApiService
import pro.com.catfacts.views.adapter.RandomCatFacts

class RandomFactRepoImpl(
    private val apiService: ApiService,
    private val dataMapper: DataMapper
) : RandomFactRepo {

    override fun fetchRandomFact(): Flow<RandomCatFacts> {
        return flow {
            val data = apiService.getRandomFact()
            emit(dataMapper.toMap(data))
        }
    }

    override fun delete(catId: String): Flow<String> {
        return flow {
            dataMapper.delete(catId)
            emit(catId)
        }
    }

    override suspend fun fetchDetails(): List<RandomCatFacts> {
        return dataMapper.fetchCatDetails()
    }
}