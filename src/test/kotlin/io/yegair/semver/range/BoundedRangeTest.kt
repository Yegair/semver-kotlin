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
            "3.0.0,           true",
            "2.9.9,           false",
            "3.99.99,         true",
            "3.99.99-beta.99, false",
            "3.99.99+2,       true",
            "4.0.0,           false",
            "4.0.0+43,        false",
            "4.0.0-beta.1,    false"
        )
        fun partialNoMinorNoPatch(version: String, match: Boolean) {

            // [3.0.0 .. 4.0.0)
            val range = "3"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "20.2.0,                true",
            "20.1.42,               false",
            "20.2.42,               true",
            "20.2.42-beta.4,        false",
            "20.3.0,                false",
            "20.3.0+512,            false",
            "20.3.0-3+420,          false",
            "20.3.0-beta.2.nightly, false"
        )
        fun partialNoPatch(version: String, match: Boolean) {

            // [20.2.0 .. 20.3.0)
            val range = "20.2"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "2.0.0,         true",
            "1.999.99,      false",
            "2.17.15,       true",
            "2.17.15-16,    false",
            "2.17.15+42,    true",
            "3.0.0-alpha.4, false"
        )
        fun minorWildcard(version: String, match: Boolean) {

            // [2.0.0 .. 3.0.0)
            val range = "2.*"

            assertEquals(
                match,
                range.asRange().satisfiedBy(version.asVersion()),
                "'$version' ${if (match) "should" else "should not"} match range '$range'"
            )
        }

        @ParameterizedTest
        @CsvSource(
            "1.2.0+57,         true",
            "1.1.99,           false",
            "1.2.99,           true",
            "1.2.99-beta.16,   false",
            "1.2.99+12,        true",
            "1.3.0,            false",
            "1.3.0+3,          false",
            "1.3.0-17,         false",
            "1.3.0-alpha.4+25, false"
        )
        fun patchWildcard(version: String, match: Boolean) {

            // [1.2.0 .. 1.3.0)
            val range = "1.2.*"

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