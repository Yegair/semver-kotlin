package io.yegair.semver.range

import io.yegair.semver.version.SemanticVersion
import io.yegair.semver.range.PrimitiveRange.Operator.LT
import io.yegair.semver.version.VersionNumber
import io.yegair.semver.version.Wildcard
import io.yegair.semver.version.WildcardVersion
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
                CaretRange(SemanticVersion(1, 2, 4)),
                range
            )
        }

        @Test
        fun primitiveLt() {

            // given
            val expression = "< 3.3.x"

            // when
            val range = Range.parse(expression)

            // then
            assertEquals(
                PrimitiveRange(LT, WildcardVersion(
                    VersionNumber.of(3),
                    VersionNumber.of(3),
                    Wildcard
                )),
                range
            )
        }
    }
}