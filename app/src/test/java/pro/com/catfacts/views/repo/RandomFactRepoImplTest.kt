package pro.com.catfacts.views.repo

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.model.CatRandomFact
import pro.com.catfacts.model.DataMapper
import pro.com.catfacts.network.ApiService
import pro.com.catfacts.views.adapter.RandomCatFacts
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RandomFactRepoImplTest {

    private val mockApiService: ApiService = mockk(relaxed = true)
    private val mockDataMapper: DataMapper = mockk(relaxed = true)

    private lateinit var instance: RandomFactRepoImpl

    @BeforeEach
    fun beforeEachTest() {
        instance = RandomFactRepoImpl(mockApiService, mockDataMapper)
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("When call the fetchRandomFact()")
    @Nested
    inner class WhenFetchRandomFact {

        @DisplayName("Then emit RandomCatFacts data")
        @Test
        fun test() {

            val mockData: CatRandomFact = mockk(relaxed = true)
            val fact = RandomCatFacts()

            coEvery { mockApiService.getRandomFact() } returns mockData
            every { mockDataMapper.toMap(mockData) } returns fact

            val result = instance.fetchRandomFact()

            runBlocking {
                result.collect {
                    assertEquals(fact, it)
                }
            }
        }
    }

    @DisplayName("When call the WhenFetchDetails()")
    @Nested
    inner class WhenFetchDetails {

        @DisplayName("Then return list of RandomCatFacts data")
        @Test
        fun test() = runBlocking {
            val list: List<RandomCatFacts> = listOf(RandomCatFacts(), RandomCatFacts())

            coEvery { mockDataMapper.fetchCatDetails() } returns list

            instance.fetchDetails()

            assertEquals(2, list.size)

        }
    }

    @DisplayName("When call the WhenFetchDetails()")
    @Nested
    inner class WhenDeleteByCatId {

        @DisplayName("Then return the catId")
        @Test
        fun test() = runTest {

            val catId = "catId'"

            every { mockDataMapper.delete(any()) } just runs

            val result = instance.delete(catId)

            result.collect {
                assertEquals(catId, it)
            }
        }
    }
}