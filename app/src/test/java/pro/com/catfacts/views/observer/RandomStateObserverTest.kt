package pro.com.catfacts.views.observer

import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.views.RandomFactView
import pro.com.catfacts.views.State
import pro.com.catfacts.views.adapter.RandomCatFacts
import kotlin.test.assertEquals

class RandomStateObserverTest {

    lateinit var instance: RandomStateObserver
    private val mockRandomFactView: RandomFactView = mockk(relaxed = true)

    @BeforeEach
    fun beforeEachTest() {
        instance = RandomStateObserver(mockRandomFactView)
    }

    @AfterEach
    fun afterEachTest() {
        unmockkAll()
        clearAllMocks()
    }

    @DisplayName("Given state")
    @Nested
    inner class GivenState {
        @DisplayName("When state is RenderData")
        @Nested
        inner class WhenStateIsRenderData {
            @DisplayName("Then change state to RandomFactView.State.RenderDate")
            @Test
            fun test() {
                val list = arrayListOf<RandomCatFacts>()
                val slot = slot<RandomFactView.State.RenderData>()

                instance.onChanged(State.RenderData(list))

                verify { mockRandomFactView.changeState(capture(slot)) }

                assertEquals(list, slot.captured.randomFacts)
                assertEquals(RandomFactView.State.RenderData::class, slot.captured::class)
            }
        }

        @DisplayName("When state is Error")
        @Nested
        inner class WhenStateIsError {
            @DisplayName("Then change state to RandomFactView.State.Error")
            @Test
            fun test() {

                val mockThrowable: Throwable = mockk(relaxed = true)
                val slot = slot<RandomFactView.State.Error>()

                instance.onChanged(State.Error(mockThrowable))

                verify { mockRandomFactView.changeState(capture(slot)) }

                assertEquals(mockThrowable, slot.captured.error)
                assertEquals(RandomFactView.State.Error::class, slot.captured::class)
            }
        }
    }
}