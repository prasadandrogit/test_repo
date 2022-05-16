package pro.com.catfacts.model

import pro.com.catfacts.db.CatDetailDao
import pro.com.catfacts.db.CatDetails
import pro.com.catfacts.util.DateUtil.parseDate
import pro.com.catfacts.views.adapter.RandomCatFacts

class DataMapper(private val dao: CatDetailDao) {
    fun toMap(data: CatRandomFact): RandomCatFacts {
        val displayDate = parseDate(data.createdAt)
        val catDetails = CatDetails(catId = data.id, fact = data.text, createdDate = displayDate)
        dao.insertCatDetails(catDetails)
        return RandomCatFacts(data.id, data.text, displayDate)
    }

    suspend fun fetchCatDetails(): List<RandomCatFacts> {
        val randomCatFacts = dao.fetchCatDetails()
        return randomCatFacts.map {
            RandomCatFacts(it.catId, it.fact, it.createdDate)
        }
    }

    fun delete(catId: String) {
        dao.deleteCatDetails(catId)
    }
}