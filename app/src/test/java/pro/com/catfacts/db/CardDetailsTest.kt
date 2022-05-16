package pro.com.catfacts.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CardDetailsTest {
    @DisplayName("Given CardDetails")
    @Nested
    inner class GivenArticleDetails {
        @DisplayName("then check default values")
        @Test
        fun test() {

            val mockCardDetails = CatDetails()

            assertTrue(mockCardDetails.catId.isEmpty())
            assertTrue(mockCardDetails.fact.isEmpty())
            assertTrue(mockCardDetails.createdDate.isEmpty())
            assertEquals(mockCardDetails.id, 0)
        }

        @DisplayName("When you pass the values through constructor, " +
                " then should be set the values")
        @Test
        fun whenYouPassTheValuesThroughConstructorThenShouldBeSetTheValues() {

            val mockCardDetails = CatDetails(id = 1, catId = "CatId", "fact",
                "2021-11-25")
            assertEquals(mockCardDetails.id, 1)
            assertEquals(mockCardDetails.catId, "CatId")
            assertEquals(mockCardDetails.createdDate, "2021-11-25")
            assertEquals(mockCardDetails.fact, "fact")

        }
    }
}