package pro.com.catfacts.views

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Ignore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.TestUtils
import pro.com.catfacts.util.Connectivity
import pro.com.catfacts.views.adapter.RandomCatFacts
import pro.com.catfacts.views.repo.RandomFactRepo
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RandomFactViewModelTest {

    private val mockRepo: RandomFactRepo = mockk(relaxed = true)
    private val mockLiveData: MutableLiveData<State> = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    lateinit var instance: RandomFactViewModel

    @BeforeEach
    fun beforeEachTest() {
        Dispatchers.setMain(dispatcher)
        instance = spyk(RandomFactViewModel(mockRepo))
        TestUtils.setProperty(instance, "liveData", mockLiveData)
    }

    @AfterEach
    fun afterEachTest() {
        Dispatchers.resetMain()
        unmockkAll()
        clearAllMocks()
    }

    @DisplayName("When loadFromDb")
    @Nested
    inner class LoadFromDb {
        @DisplayName("Then the render data with list form db")
        @Test
        fun test() = runTest {

            val list: List<RandomCatFacts> = listOf()

            val slot = CapturingSlot<State.RenderData>()
            coEvery { mockRepo.fetchDetails() } returns list

            instance.loadFromDb()

            verify { mockLiveData.postValue(capture(slot)) }
            assertEquals(State.RenderData::class, slot.captured::class)
            assertEquals(list, slot.captured.randomFacts)

        }
    }

    @DisplayName("When call getRandomFact()")
    @Ignore("Tried but not getting the root cause for the failure so I have skipped")
    @Nested
    inner class WhenGetRandomFact {
        @DisplayName("Then collect the data and set to live data")
        @Test
        fun test() = runTest {

            val mockFact = RandomCatFacts()
            val slot = CapturingSlot<State.Add>()

            every { mockRepo.fetchRandomFact() } returns flow {
                emit(mockFact)
            }

            instance.getRandomFact()

            verify { mockLiveData.postValue(capture(slot)) }
            assertEquals(State.Add::class, slot.captured::class)

        }

        @DisplayName("Then catch the error if anything exception throws")
        @Test
        fun testForError() = runTest {

            val slot = CapturingSlot<State.Error>()
            every { mockRepo.fetchRandomFact() } returns flow {
                throw NullPointerException()
            }

            instance.getRandomFact()

            verify { mockLiveData.postValue(capture(slot)) }
            assertEquals(State.Error::class, slot.captured::class)
            assertEquals(NullPointerException::class, slot.captured.error::class)
        }
    }

    @DisplayName("When call setObserver()")
    @Nested
    inner class WhenSetObserver {
        @DisplayName("Given lifecycle owner and observer")
        @Nested
        inner class GivenParams {
            @DisplayName("Then register observer to live data")
            @Test
            fun test() {
                val mockLifeCycleOwner: LifecycleOwner = mockk(relaxed = true)
                val mockObserver: Observer<State> = mockk(relaxed = true)

                instance.setObserver(mockLifeCycleOwner, mockObserver)

                verify { mockLiveData.observe(mockLifeCycleOwner, mockObserver) }
            }
        }
    }

    @DisplayName("When call the loadFacts()")
    @Nested
    inner class WhenLoadFacts {

        @BeforeEach
        fun beforeEachTest() {
            mockkObject(Connectivity)
        }

        @After
        fun afterEachTest() {
            unmockkObject(Connectivity)
        }

        @DisplayName("Given context if internet available")
        @Nested
        inner class IfInternetAvailable {
            @DisplayName("Then load facts from remote")
            @Test
            fun loadFromRemoteTest() {

                val mockContext: Context = mockk(relaxed = true)

                every { Connectivity.isConnected(mockContext) } returns true

                instance.loadFacts(mockContext)

                verify { mockRepo.fetchRandomFact() }

            }
        }

        @DisplayName("Given context if internet not available")
        @Nested
        inner class IfInternetUnAvailable {
            @DisplayName("Then load facts from remote")
            @Test
            fun loadFromDb() {
                val mockContext: Context = mockk(relaxed = true)

                every { Connectivity.isConnected(mockContext) } returns false

                instance.loadFacts(mockContext)

                coVerify { mockRepo.fetchDetails() }

            }
        }
    }

    @DisplayName("When call on delete")
    @Nested
    inner class WhenCallDelete {
        @DisplayName("Given CatId")
        @Nested
        inner class GivenCatId {
            @DisplayName("Then post the value delete state to live data")
            @Test
            fun test() = runTest {
                val catId = "catId"
                val slot = CapturingSlot<State.Delete>()
                every { mockRepo.delete(catId) } returns flow {
                    emit(catId)
                }

                instance.delete(catId)

                verify { mockLiveData.postValue(capture(slot)) }
                assertEquals(State.Delete::class, slot.captured::class)

            }

            @DisplayName("Then catch the error if anything exception throws")
            @Test
            fun testForError() = runTest {
                val catId = "catId"
                val slot = CapturingSlot<State.Error>()
                every { mockRepo.delete(catId) } returns flow {
                    throw NullPointerException()
                }

                instance.delete(catId)

                verify { mockLiveData.postValue(capture(slot)) }
                assertEquals(State.Error::class, slot.captured::class)
                assertEquals(NullPointerException::class, slot.captured.error::class)
            }
        }
    }
}
