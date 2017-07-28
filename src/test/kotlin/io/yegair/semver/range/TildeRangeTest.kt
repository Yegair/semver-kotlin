package io.yegair.semver.range

import io.yegair.semver.version.asVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Unit test for [TildeRange].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class TildeRangeTest {

    @Nested
    inner class SatisfiedBy {

        @ParameterizedTest
        @CsvSource(
            "8.7.1,           true",
            "8.7.1+77,        true",
            "8.7.1-alpha.4,   false",

            "8.7.0,           false",
            "8.7.0-5.beta,    false",
            "8.7.0+foobar,    false",

            "8.7.3,           true",
            "8.7.3-beta.2,    false",
            "8.7.3+63,        true",

            "8.8.0,           false",
            "8.8.0-alpha.1,   false",
            "8.8.0+92,        false",
            "8.8.1,           false"
        )
        fun full(version: String, match: Boolean) {

            // [8.7.1 .. 8.8.0)
            val range = "~8.7.1"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "3.0.23,          true",
            "3.0.23+77,       true",
            "3.0.23-4.beta,   true",
            "3.0.23-12.beta,  true",
            "3.0.23-3.beta,   false",
            "3.0.23-alpha.2,  false",
            "3.0.23-3.alpha,  false",
            "3.0.23-4.alpha,  false",
            "3.0.23-5.alpha,  true",

            "3.0.22,          false",
            "3.0.22+78,       false",
            "3.0.22-2.beta,   false",
            "3.0.22-4.beta,   false",
            "3.0.22-5.beta,   false",

            "3.0.42,          true",
            "3.0.42+6,        true",
            "3.0.42-2.beta,   false",
            "3.0.42-4.beta,   false",
            "3.0.42-9.zeta,   false",
            "3.0.42-1.alpha,  false",

            "3.1.0,           false",
            "3.1.0,           false",
            "3.1.0-1.alpha,   false",
            "3.1.0-3.beta,    false",
            "3.1.0-4.beta,    false",
            "3.1.0-5.beta,    false",
            "3.1.0-4.beta+16, false",
            "3.1.0+32,        false"
        )
        fun prerelease(version: String, match: Boolean) {

            // [3.0.23-4.beta .. 3.1.0)
            val range = "~3.0.23-4.beta"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.6.1,           true",
            "0.6.1+9,         true",
            "0.6.1-alpha.7,   false",

            "0.6.0,           false",
            "0.6.0-5.nightly, false",
            "0.6.0+22,        false",
            "0.6.0-5.M6+13,   false",

            "0.6.8,           true",
            "0.6.8-beta.2,    false",
            "0.6.8+63,        true",
            "0.6.8-beta.5+92, false",

            "0.7.0,           false",
            "0.7.1,           false",
            "0.7.0-alpha.1,   false",
            "0.7.0+66,        false",
            "0.7.0-3+13,      false"
        )
        fun zeroMajor(version: String, match: Boolean) {

            // [0.6.1 .. 0.7.0)
            val range = "~0.6.1"

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
            "0.0.3-beta.1+22, false",

            "0.0.2,           false",
            "0.0.2-5.nightly, false",
            "0.0.2+23,        false",
            "0.0.2-RC.5+12,   false",

            "0.0.42,          true",
            "0.0.42-alpha.1,  false",
            "0.0.42+66,       true",

            "0.1.0,           false",
            "0.1.0+19,        false",
            "0.1.0-beta.3+19, false",
            "0.1.0-77+19,     false",
            "0.1.1,           false"
        )
        fun zeroMinor(version: String, match: Boolean) {

            // [0.0.3 .. 0.1.0)
            val range = "~0.0.3"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "1.0.0,           true",
            "1.0.0+77,        true",
            "1.0.0-RC.4,      false",
            "1.0.0-RC.4+23,   false",

            "0.9.9,           false",
            "0.9.9-RC.2,      false",
            "0.9.9-RC.2+60,   false",
            "0.9.9+42,        false",

            "1.8.2,           true",
            "1.8.2-beta.2,    false",
            "1.8.2+63,        true",
            "1.8.2-beta+2,    false",

            "2.0.0,           false",
            "2.0.1,           false",
            "2.1.0,           false",
            "2.0.0-alpha.1,   false",
            "2.0.0-alpha+13,  false",
            "2.0.0+66,        false"
        )
        fun minorWildcard(version: String, match: Boolean) {

            // [1.0.0 .. 2.0.0)
            val range = "~1.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "42.1.0,           true",
            "42.1.0+77,        true",
            "42.1.0-GA.4,      false",
            "42.1.0-GA.4+13,   false",

            "42.0.99,          false",
            "42.0.99-GA.3,     false",
            "42.0.99-GA.3,     false",

            "42.1.1,           true",
            "42.1.1-GA.2,      false",
            "42.1.1+63,        true",
            "42.1.1-GA.2+64,   false",

            "42.2.0,           false",
            "42.2.1,           false",
            "42.2.0-GA.1,      false",
            "42.2.0-GA.1+99,   false",
            "42.2.0+66,        false"
        )
        fun patchWildcard(version: String, match: Boolean) {

            // [42.1.0 .. 42.2.0)
            val range = "~42.1.X"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "6.0.0,           true",
            "6.0.0+77,        true",
            "6.0.0-alpha.4,   false",
            "6.0.0-alpha.4+0, false",

            "5.9.9,           false",
            "5.9.9-5.RC,      false",
            "5.9.9-5.RC+27,   false",
            "5.9.9+60,        false",

            "6.9.0,           true",
            "6.9.0-beta.2,    false",
            "6.9.0-beta.2+55, false",
            "6.9.0+63,        true",

            "7.0.0,           false",
            "7.0.1,           false",
            "7.1.0,           false",
            "7.0.0-1.RC,      false",
            "7.0.0-1.RC+65,   false",
            "7.0.0+66,        false"
        )
        fun minorWildcardPatchWildcard(version: String, match: Boolean) {

            // [6.0.0 .. 7.0.0)
            val range = "~6.*.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "18.0.0,           true",
            "18.0.0+77,        true",
            "18.0.0-alpha.4,   false",
            "18.0.0-alpha.4+0, false",

            "17.9.99,          false",
            "17.9.99-8.RC,     false",
            "17.9.99-8.RC+27,  false",
            "17.9.99+60,       false",

            "18.2.0,           true",
            "18.2.0-beta.2,    false",
            "18.2.0-beta.2+55, false",
            "18.2.0+63,        true",

            "19.0.0,           false",
            "19.0.1,           false",
            "19.1.0,           false",
            "19.0.0-0.RC,      false",
            "19.0.0-0.RC+65,   false",
            "19.0.0+66,        false"
        )
        fun partialMajor(version: String, match: Boolean) {

            // [18.0.0 .. 19.0.0)
            val range = "~18"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "4.2.0,           true",
            "4.2.0+77,        true",
            "4.2.0-alpha.4,   false",
            "4.2.0-alpha.4+0, false",

            "4.1.99,          false",
            "4.1.99-8.RC,     false",
            "4.1.99-8.RC+27,  false",
            "4.1.99+60,       false",

            "4.2.8,           true",
            "4.2.8-beta.2,    false",
            "4.2.8-beta.2+55, false",
            "4.2.8+63,        true",

            "4.3.0,           false",
            "4.3.1,           false",
            "5.0.0,           false",
            "4.3.0-0.RC,      false",
            "4.3.0-0.RC+65,   false",
            "4.3.0+66,        false"
        )
        fun partialMajorMinor(version: String, match: Boolean) {

            // [4.2.0 .. 4.3.0)
            val range = "~4.2"

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
            "0.0.0-4.b+13,    false",

            "0.3.5,           true",
            "0.3.5-RC.2.0,    false",
            "0.3.5+63,        true",
            "0.3.5-RC.2.0+00, false",

            "1.0.0,           false",
            "1.0.1,           false",
            "1.1.0,           false",
            "1.0.0-0.1,       false",
            "1.0.0-0.1+2,     false",
            "1.0.0+66,        false"
        )
        fun zeroMajorMinorWildcard(version: String, match: Boolean) {

            // [0.0.0 .. 1.0.0)
            val range = "~0.*"

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
            "0.0.0-9.M+1,     false",

            "0.0.4,           true",
            "0.0.4-beta.2,    false",
            "0.0.4+63,        true",

            "0.1.0,           false",
            "0.1.1,           false",
            "1.0.0,           false",
            "2.0.0,           false",
            "0.1.0-alpha.1,   false",
            "0.1.0-alpha.1+3, false",
            "0.1.0+66,        false"
        )
        fun zeroMajorZeroMinorPatchWildcard(version: String, match: Boolean) {

            // [0.0.0 .. 0.1.0)
            val range = "~0.0.X"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,            true",
            "0.0.0+12,         true",
            "0.0.0-foo.7,      false",
            "0.0.0-foo.7+24,   false",

            "0.9.9,            true",
            "0.9.9-BETA.2,     false",
            "0.9.9-BETA.2+0a,  false",
            "0.9.9+63,         true",

            "1.0.0,            false",
            "1.0.1,            false",
            "1.1.0,            false",
            "2.0.0,            false",
            "1.0.0-GA.20,      false",
            "1.0.0-GA.20+13,   false",
            "1.0.0+42,         false"
        )
        fun partialZeroMajor(version: String, match: Boolean) {

            // [0.0.0 .. 1.0.0)
            val range = "~0"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,            true",
            "0.0.0+0fe,        true",
            "0.0.0-M.4,        false",
            "0.0.0-10.M+1,     false",

            "0.0.7,            true",
            "0.0.7-beta.2,     false",
            "0.0.7-beta.2+121, false",
            "0.0.7+63,         true",

            "0.1.0,            false",
            "0.1.1,            false",
            "1.0.0,            false",
            "2.0.0,            false",
            "0.1.0-alpha.1,    false",
            "0.1.0-alpha.1+3,  false",
            "0.1.0+66,         false"
        )
        fun partialZeroMajorZeroMinor(version: String, match: Boolean) {

            // [0.0.0 .. 0.1.0)
            val range = "~0.0"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }
    }
}