package io.yegair.semver.range

import io.yegair.semver.version.asVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Unit test for [CaretRange].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class CaretRangeTest {

    @Nested
    inner class SatisfiedBy {

        @ParameterizedTest
        @CsvSource(
            "3.2.5,           true",
            "3.2.5+77,        true",
            "3.2.5-alpha.4,   false",
            "3.2.4,           false",
            "3.2.4-5.nightly, false",
            "3.9.15,          true",
            "3.9.15-beta.2,   false",
            "3.9.15+63,       true",
            "4.0.0,           false",
            "4.2.5,           false",
            "4.0.0-alpha.1,   false"
        )
        fun full(version: String, match: Boolean) {

            // [3.2.5 .. 4.0.0)
            val range = "^3.2.5"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "1.9.17,          true",
            "1.9.17+77,       true",
            "1.9.17-beta.4,   true",
            "1.9.17-beta.1,   false",
            "1.9.17-alpha.2,  false",

            "1.9.16,          false",
            "1.9.16+78,       false",
            "1.9.16-beta.2,   false",
            "1.9.16-beta.4,   false",

            "1.9.21,          true",
            "1.9.21+6,        true",
            "1.9.21-beta.2,   false",
            "1.9.21-beta.4,   false",
            "1.9.21-zeta.9,   false",
            "1.9.21-alpha.1,  false",

            "2.0.0,           false",
            "2.0.1,           false",
            "2.0.0-alpha.1,   false",
            "2.0.0-beta.1+16, false",
            "2.0.0+32,        false"
        )
        fun prerelease(version: String, match: Boolean) {

            // [1.9.17-beta.2 .. 2.0.0)
            val range = "^1.9.17-beta.2"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.2.3,           true",
            "0.2.3+77,        true",
            "0.2.3-alpha.4,   false",

            "0.2.2,           false",
            "0.2.2-5.nightly, false",

            "0.2.7,           true",
            "0.2.7-beta.2,    false",
            "0.2.7+63,        true",

            "0.3.0,           false",
            "0.3.1,           false",
            "0.3.0-alpha.1,   false",
            "0.3.0+66,        false"
        )
        fun zeroMajor(version: String, match: Boolean) {

            // [0.2.3 .. 0.3.0)
            val range = "^0.2.3"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.3,           true",
            "0.0.3+77,        true",
            "0.0.3-beta.1,    false",

            "0.0.2,           false",
            "0.0.2-5.nightly, false",
            "0.0.2+23,        false",

            "0.0.4,           false",
            "0.3.0-alpha.1,   false",
            "0.3.0+66,        false",

            "0.0.5,           false",

            "0.1.0,           false"
        )
        fun zeroMinor(version: String, match: Boolean) {

            // [0.0.3 .. 0.0.4)
            val range = "^0.0.3"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "4.0.0,           true",
            "4.0.0+77,        true",
            "4.0.0-alpha.4,   false",

            "3.9.9,           false",
            "3.9.9-5.nightly, false",

            "4.6.3,           true",
            "4.6.3-beta.2,    false",
            "4.6.3+63,        true",

            "5.0.0,           false",
            "5.0.1,           false",
            "5.1.0,           false",
            "5.0.0-alpha.1,   false",
            "5.0.0+66,        false"
        )
        fun minorWildcard(version: String, match: Boolean) {

            // [4.0.0 .. 5.0.0)
            val range = "^4.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "20.4.0,           true",
            "20.4.0+77,        true",
            "20.4.0-alpha.4,   false",

            "20.3.9,           false",
            "20.3.9-5.nightly, false",

            "20.8.1,           true",
            "20.8.1-beta.2,    false",
            "20.8.1+63,        true",

            "21.0.0,           false",
            "21.0.1,           false",
            "21.1.0,           false",
            "21.0.0-alpha.1,   false",
            "21.0.0+66,        false"
        )
        fun patchWildcard(version: String, match: Boolean) {

            // [20.4.0 .. 21.0.0)
            val range = "^20.4.x"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "9.0.0,           true",
            "9.0.0+77,        true",
            "9.0.0-alpha.4,   false",

            "8.9.9,           false",
            "8.9.9-5.nightly, false",

            "9.0.3,           true",
            "9.0.3-beta.2,    false",
            "9.0.3+63,        true",

            "10.0.0,          false",
            "10.0.1,          false",
            "10.1.0,          false",
            "10.0.0-alpha.1,  false",
            "10.0.0+66,       false"
        )
        fun minorWildcardPatchWildcard(version: String, match: Boolean) {

            // [9.0.0 .. 10.0.0)
            val range = "^9.*.X"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "11.0.0,           true",
            "11.0.0+77,        true",
            "11.0.0-alpha.4,   false",

            "10.9.9,           false",
            "10.9.9-5.nightly, false",

            "11.9.2,           true",
            "11.9.2-beta.2,    false",
            "11.9.2+63,        true",

            "12.0.0,           false",
            "12.0.1,           false",
            "12.1.0,           false",
            "12.0.0-alpha.1,   false",
            "12.0.0+66,        false"
        )
        fun partialMajor(version: String, match: Boolean) {

            // [11.0.0 .. 12.0.0)
            val range = "^11"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "2.1.0,           true",
            "2.1.0+3,         true",
            "2.1.0-3.beta,    false",

            "2.0.9,           false",
            "2.0.9-5.nightly, false",

            "2.2.1,           true",
            "2.2.1-beta.2,    false",
            "2.2.1+63,        true",

            "3.0.0,           false",
            "3.0.1,           false",
            "3.1.0,           false",
            "3.0.0-alpha.1,   false",
            "3.0.0+66,        false"
        )
        fun partialMajorMinor(version: String, match: Boolean) {

            // [2.1.0 .. 3.0.0)
            val range = "^2.1"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           true",
            "0.0.0+77,        true",
            "0.0.0-alpha.4,   false",

            "0.7.5,           true",
            "0.7.5-beta.2,    false",
            "0.7.5+63,        true",

            "1.0.0,           false",
            "1.0.1,           false",
            "1.1.0,           false",
            "1.0.0-alpha.1,   false",
            "1.0.0+66,        false"
        )
        fun zeroMajorMinorWildcard(version: String, match: Boolean) {

            // [0.0.0 .. 1.0.0)
            val range = "^0.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           true",
            "0.0.0+77,        true",
            "0.0.0-alpha.4,   false",

            "0.0.9,           true",
            "0.0.9-beta.2,    false",
            "0.0.9+63,        true",

            "0.1.0,           false",
            "0.1.1,           false",
            "1.0.0,           false",
            "0.1.0-alpha.1,   false",
            "0.1.0+66,        false"
        )
        fun zeroMajorZeroMinorPatchWildcard(version: String, match: Boolean) {

            // [0.0.0 .. 0.1.0)
            val range = "^0.0.X"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           true",
            "0.0.0+12,        true",
            "0.0.0-foo.7,     false",

            "0.9.9,           true",
            "0.9.9-beta.2,    false",
            "0.9.9+63,        true",

            "1.0.0,           false",
            "1.0.1,           false",
            "1.1.0,           false",
            "1.0.0-beta.20,   false",
            "1.0.0+42,        false"
        )
        fun partialZeroMajor(version: String, match: Boolean) {

            // [0.0.0 .. 1.0.0)
            val range = "^0"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           true",
            "0.0.0+77,        true",
            "0.0.0-alpha.4,   false",

            "0.0.4,           true",
            "0.0.4-beta,      false",
            "0.0.4+latest,    true",

            "0.1.0,           false",
            "0.1.1,           false",
            "1.0.0,           false",
            "0.1.0-beta.1,    false",
            "0.1.0+nightly,   false"
        )
        fun partialZeroMajorZeroMinor(version: String, match: Boolean) {

            // [0.0.0 .. 0.1.0)
            val range = "^0.0"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }
    }
}