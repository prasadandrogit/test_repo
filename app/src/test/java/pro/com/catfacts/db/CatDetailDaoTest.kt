package pro.com.catfacts.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatDetailDaoTest {

    private lateinit var catDetailsDao: CatDetailDao

    @Before
    fun setUp() {
        val database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        catDetailsDao = database.catDetailsDao()
    }

    @Test
    fun insertArticle() = runBlocking {
        val catDetails = CatDetails(
            id = 1,
            catId = "76523765",
            fact = "Cat is a pet animal",
            createdDate = "2022-02-20"
        )
        catDetailsDao.insertCatDetails(catDetails)
        val catDetailList = catDetailsDao.fetchCatDetails()
        assertEquals(catDetailList.size, 1)
        assertEquals(catDetailList[0], catDetails)
    }

    @Test
    fun deleteCatDetails() = runBlocking {
        val catId = "76523765"
        val catDetails = CatDetails(
            id = 1,
            catId = catId,
            fact = "Cat is a pet animal",
            createdDate = "2022-02-20"
        )
        catDetailsDao.insertCatDetails(catDetails)

        catDetailsDao.deleteCatDetails(catId)
        val catDetailList = catDetailsDao.fetchCatDetails()
        assertEquals(catDetailList.size, 0)
    }
}