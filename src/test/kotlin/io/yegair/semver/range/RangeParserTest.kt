package io.yegair.semver.range

import io.yegair.semver.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit test for [ANTLRRangeParser]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class RangeParserTest {

    @Nested
    inner class Parse {

        @Test
        fun caret() {

            // given
            val expression = "^=1.2.4"

            // when
            val range = Range.parse(expression)

            // then
            assertEquals(
                OrRange(
                    CaretRange(Version(1, 2, 4))
                ),
                range
            )
        }
    }
}