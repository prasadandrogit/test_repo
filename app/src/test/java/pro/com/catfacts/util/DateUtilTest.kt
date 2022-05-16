package pro.com.catfacts.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DateUtilTest {

    @DisplayName("Given date")
    @Nested
    inner class GivenDate {
        @DisplayName("When input format is yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        @Nested
        inner class WhenInputFormat {
            @DisplayName("Then return required format(yyyy-MM-dd HH:mm:ss)")
            @Test
            fun test() {
                val inputDate = "2021-10-09T19:17:09.036Z"

                val result = DateUtil.parseDate(inputDate)

                assertEquals("2021-10-09 19:17:09", result)

            }
        }
    }
}