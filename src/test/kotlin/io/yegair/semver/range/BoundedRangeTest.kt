package io.yegair.semver.range

import io.yegair.semver.version.SemanticVersion
import io.yegair.semver.version.Wildcard
import io.yegair.semver.version.WildcardVersion
import io.yegair.semver.version.asVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Unit test for [BoundedRange]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class BoundedRangeTest {

    @Nested
    inner class Parse {

        @Test
        fun exact() {
            assertEquals(
                BoundedRange(
                    lower = SemanticVersion(0, 0, 2),
                    upper = SemanticVersion(0, 8, 0)
                ),
                "0.0.2 - 0.8.0".asRange()
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                BoundedRange(
                    lower = SemanticVersion(0, 3, 2, prerelease = "M5.4"),
                    upper = SemanticVersion(0, 4, 2, prerelease = "M3.1")
                ),
                "0.3.2-M5.4 - 0.4.2-M3.1".asRange()
            )
        }

        @Test
        fun build() {
            assertEquals(
                BoundedRange(
                    lower = SemanticVersion(1, 1, 7, build = "13"),
                    upper = SemanticVersion(1, 1, 8, build = "2")
                ),
                "1.1.7+13 - 1.1.8+2".asRange()
            )
        }

        @Test
        fun full() {
            assertEquals(
                BoundedRange(
                    lower = SemanticVersion(4, 3, 1, prerelease = "beta.0.n", build = "42"),
                    upper = SemanticVersion(4, 6, 12, prerelease = "alpha.9.00", build = "64")
                ),
                "4.3.1-beta.0.n+42 - 4.6.12-alpha.9.00+64".asRange()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                BoundedRange(
                    lower = WildcardVersion(1, 2),
                    upper = WildcardVersion(1, 4)
                ),
                "1.2 - 1.4".asRange()
            )
        }

        @Test
        fun partialNoMinor() {
            assertEquals(
                BoundedRange(
                    lower = WildcardVersion(3),
                    upper = WildcardVersion(7)
                ),
                "3 - 7".asRange()
            )
        }

        @Test
        fun patchWildcard() {
            assertEquals(
                BoundedRange(
                    lower = WildcardVersion(3, 0, Wildcard),
                    upper = WildcardVersion(3, 7, Wildcard)
                ),
                "3.0.* - 3.7.*".asRange()
            )
        }

        @Test
        fun minorWildcard() {
            assertEquals(
                BoundedRange(
                    lower = WildcardVersion(8, Wildcard),
                    upper = WildcardVersion(10, Wildcard)
                ),
                "8.x - 10.X".asRange()
            )
        }
    }

    @Nested
    inner class SatisfiedBy {

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           false",
            "0.0.0+42,        false",
            "0.0.0-M9.2,      false",
            "0.0.0-M9.2+3,    false",

            "0.0.1,           false",
            "0.0.1+42,        false",
            "0.0.1-M9.2,      false",
            "0.0.1-M9.2+3,    false",

            "0.0.2,           true",
            "0.0.2+42,        true",
            "0.0.2-M9.2,      false",
            "0.0.2-M9.2+3,    false",

            "0.9.12,          true",
            "0.9.12+42,       true",
            "0.9.12-M9.2,     false",
            "0.9.12-M9.2+3,   false",

            "1.2.8,           true",
            "1.2.8+42,        true",
            "1.2.8-M9.2,      false",
            "1.2.8-M9.2+3,    false",

            "1.2.9,           true",
            "1.2.9+42,        true",
            "1.2.9-M9.2,      false",
            "1.2.9-M9.2+3,    false",

            "1.2.10,          false",
            "1.2.10+42,       false",
            "1.2.10-M9.2,     false",
            "1.2.10-M9.2+3,   false",

            "1.3.0,           false",
            "1.3.0+42,        false",
            "1.3.0-M9.2,      false",
            "1.3.0-M9.2+3,    false",

            "2.0.0,           false"
        )
        fun full(version: String, match: Boolean) {

            // [0.0.2 .. 1.2.10)
            val range = "0.0.2 - 1.2.9"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,             false",
            "0.0.0+42,          false",
            "0.0.0-beta.4,      false",
            "0.0.0-beta.4+3,    false",
            "0.0.0-beta.5,      false",
            "0.0.0-beta.5+3,    false",

            "1.0.1,             false",
            "1.0.1+42,          false",
            "1.0.1-beta.3,      false",
            "1.0.1-beta.3+3,    false",
            "1.0.1-beta.4,      false",
            "1.0.1-beta.4+3,    false",
            "1.0.1-beta.5,      false",
            "1.0.1-beta.5+3,    false",

            "0.0.1,           false",
            "0.0.1+42,        false",
            "0.0.1-M9.2,      false",
            "0.0.1-M9.2+3,    false",

            "0.0.2,           true",
            "0.0.2+42,        true",
            "0.0.2-M9.2,      false",
            "0.0.2-M9.2+3,    false",

            "0.9.12,          true",
            "0.9.12+42,       true",
            "0.9.12-M9.2,     false",
            "0.9.12-M9.2+3,   false",

            "1.2.8,           true",
            "1.2.8+42,        true",
            "1.2.8-M9.2,      false",
            "1.2.8-M9.2+3,    false",

            "1.2.9,           true",
            "1.2.9+42,        true",
            "1.2.9-M9.2,      false",
            "1.2.9-M9.2+3,    false",

            "1.2.10,          false",
            "1.2.10+42,       false",
            "1.2.10-M9.2,     false",
            "1.2.10-M9.2+3,   false",

            "1.3.0,           false",
            "1.3.0+42,        false",
            "1.3.0-M9.2,      false",
            "1.3.0-M9.2+3,    false",

            "2.0.0,           false"
        )
        fun prerelease(version: String, match: Boolean) {

            // [1.0.2-beta.4 .. 1.1.0-10.ALPHA)
            val range = "1.0.2-beta.4 - 1.1.0-9.ALPHA"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "3.0.0,           true",
            "3.0.0+42,        true",
            "3.0.0-M9.2,      false",
            "3.0.0-M9.2+3,    false",

            "3.0.3,           true",
            "3.0.3+42,        true",
            "3.0.3-M9.2,      false",
            "3.0.3-M9.2+3,    false",

            "3.7.3,           true",
            "3.7.3+42,        true",
            "3.7.3-M9.2,      false",
            "3.7.3-M9.2+3,    false",

            "4.2.0,           true",
            "4.2.0+42,        true",
            "4.2.0-M9.2,      false",
            "4.2.0-M9.2+3,    false",

            "4.9.99,          true",
            "4.9.99+42,       true",
            "4.9.99-M9.2,     false",
            "4.9.99-M9.2+3,   false",

            "5.0.0,           false",
            "5.0.0+42,        false",
            "5.0.0-M9.2,      false",
            "5.0.0-M9.2+3,    false",

            "5.0.1,           false",

            "5.1.0,           false",

            "5.1.1,           false",

            "6.0.0,           false"
        )
        fun partialMajor(version: String, match: Boolean) {

            // [3.0.0 .. 5.0.0)
            val range = "3 - 4"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "20.1.99,               false",
            "20.1.99+1,             false",
            "20.1.99-RC.9,          false",
            "20.1.99-RC.9+2,        false",

            "20.2.0,                true",
            "20.2.0+1,              true",
            "20.2.0-RC.9,           false",
            "20.2.0-RC.9+2,         false",

            "20.2.1,                true",
            "20.2.1+1,              true",
            "20.2.1-RC.9,           false",
            "20.2.1-RC.9+2,         false",

            "20.2.1,                true",
            "20.2.1+1,              true",
            "20.2.1-RC.9,           false",
            "20.2.1-RC.9+2,         false",

            "20.5.999,              true",
            "20.5.999+1,            true",
            "20.5.999-RC.9,         false",
            "20.5.999-RC.9+2,       false",

            "20.6.0,                false",
            "20.6.0+1,              false",
            "20.6.0-RC.9,           false",
            "20.6.0-RC.9+2,         false",

            "20.6.1,                false",
            "20.6.1+1,              false",
            "20.6.1-RC.9,           false",
            "20.6.1-RC.9+2,         false",

            "20.7.0,                false",
            "20.7.0+1,              false",
            "20.7.0-RC.9,           false",
            "20.7.0-RC.9+2,         false",

            "21.0.0,                false",
            "21.0.0+1,              false",
            "21.0.0-RC.9,           false",
            "21.0.0-RC.9+2,         false"
        )
        fun partialMinor(version: String, match: Boolean) {

            // [20.2.0 .. 20.6.0)
            val range = "20.2-20.5"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "1.99.999,        false",
            "1.99.999+42,     false",
            "1.99.999-M9.2,   false",
            "1.99.999-M9.2+3, false",

            "2.0.0,           true",
            "2.0.0+42,        true",
            "2.0.0-M9.2,      false",
            "2.0.0-M9.2+3,    false",

            "2.0.3,           true",
            "2.0.3+42,        true",
            "2.0.3-M9.2,      false",
            "2.0.3-M9.2+3,    false",

            "2.7.3,           true",
            "2.7.3+42,        true",
            "2.7.3-M9.2,      false",
            "2.7.3-M9.2+3,    false",

            "4.2.0,           true",
            "4.2.0+42,        true",
            "4.2.0-M9.2,      false",
            "4.2.0-M9.2+3,    false",

            "4.9.99,          true",
            "4.9.99+42,       true",
            "4.9.99-M9.2,     false",
            "4.9.99-M9.2+3,   false",

            "5.0.0,           false",
            "5.0.0+42,        false",
            "5.0.0-M9.2,      false",
            "5.0.0-M9.2+3,    false",

            "5.0.1,           false",

            "5.1.0,           false",

            "5.1.1,           false",

            "6.0.0,           false"
        )
        fun minorWildcard(version: String, match: Boolean) {

            // [2.0.0 .. 5.0.0)
            val range = "2.* -4.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,                false",
            "0.0.0+1,              false",
            "0.0.0-RC.9,           false",
            "0.0.0-RC.9+2,         false",

            "0.2.99,               false",
            "0.2.99+1,             false",
            "0.2.99-RC.9,          false",
            "0.2.99-RC.9+2,        false",

            "0.3.0,                true",
            "0.3.0+1,              true",
            "0.3.0-RC.9,           false",
            "0.3.0-RC.9+2,         false",

            "0.3.1,                true",
            "0.3.1+1,              true",
            "0.3.1-RC.9,           false",
            "0.3.1-RC.9+2,         false",

            "0.5.9,                true",
            "0.5.9+1,              true",
            "0.5.9-RC.9,           false",
            "0.5.9-RC.9+2,         false",

            "0.6.0,                false",
            "0.6.0+1,              false",
            "0.6.0-RC.9,           false",
            "0.6.0-RC.9+2,         false",

            "0.6.1,                false",
            "0.6.1+1,              false",
            "0.6.1-RC.9,           false",
            "0.6.1-RC.9+2,         false",

            "0.7.0,                false",
            "0.7.0+1,              false",
            "0.7.0-RC.9,           false",
            "0.7.0-RC.9+2,         false",

            "1.0.0,                false",
            "1.0.0+1,              false",
            "1.0.0-RC.9,           false",
            "1.0.0-RC.9+2,         false"
        )
        fun patchWildcard(version: String, match: Boolean) {

            // [0.3.0 .. 0.6.0)
            val range = "0.3.x- 0.5.X"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "0.0.0,           true",
            "0.0.0+42,        true",
            "0.0.0-GA.4,      false",
            "0.0.0-GA.4+11,   false",

            "0.0.1,           true",
            "0.0.1-RC.0,      false",
            "0.0.1+0a,        true",
            "0.0.1-RC.0+00f,  false",

            "0.12.0,          true",
            "0.12.0-beta,     false",
            "0.12.0+0a,       true",
            "0.12.0-beta+2,   false",

            "1.0.1,           true",
            "1.0.1-beta,      false",
            "1.0.1+0a,        true",
            "1.0.1-beta+2,    false",

            "4042.2.1,        true",
            "4042.2.1-beta,   false",
            "4042.2.1+0a,     true",
            "4042.2.1-beta+2, false"
        )
        fun anyVersion(version: String, match: Boolean) {

            // [0.0.0 .. *)
            val range = "*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }
    }
}