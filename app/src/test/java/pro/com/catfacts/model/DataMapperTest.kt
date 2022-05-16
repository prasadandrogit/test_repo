package pro.com.catfacts.model

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.db.CatDetailDao
import pro.com.catfacts.db.CatDetails
import pro.com.catfacts.util.DateUtil
import pro.com.catfacts.views.adapter.RandomCatFacts
import kotlin.test.assertEquals

class DataMapperTest {
    lateinit var instance: DataMapper
    private val mockDao: CatDetailDao = mockk(relaxed = true)

    @BeforeEach
    fun beforeEachTest() {
        instance = DataMapper(mockDao)
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("When toMap()")
    @Nested
    inner class WhenInsertIntoDbAndFetch {
        @DisplayName("Given data")
        @Nested
        inner class WhenInsertIntoDbAndFetch {
            @DisplayName("Then insert and fetch the details from db")
            @Test
            fun test() {
                val id = "123"
                val fact = "some random fact"
                val mockData: CatRandomFact = mockk(relaxed = true)
                val date = "2022-01-22"

                mockkObject(DateUtil)
                every { mockDao.insertCatDetails(any()) } just runs
                every { DateUtil.parseDate(any()) } returns date
                every { mockData.id } returns id
                every { mockData.text } returns fact

                val result = instance.toMap(mockData)

                verify { mockDao.insertCatDetails(any()) }
                assertEquals(date, result.dateAdded)

                unmockkObject(DateUtil)
            }
        }
    }

    @DisplayName("When fetchDetails()")
    @Nested
    inner class WhenFetchDetailsFromDb {
        @DisplayName("Then fetch the details from db")
        @Test
        fun test() = runBlocking {
            val list = arrayListOf(
                CatDetails(
                    id = 1, fact = "fact1",
                    createdDate = "2022-01-23"
                )
            )

            coEvery { mockDao.fetchCatDetails() } returns list

            val result = instance.fetchCatDetails()

            assertEquals(1, result.size)

            result.forEach {
                assertEquals(RandomCatFacts::class, it::class)
            }

        }
    }
}