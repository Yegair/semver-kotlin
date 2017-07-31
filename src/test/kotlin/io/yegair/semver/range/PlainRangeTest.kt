package io.yegair.semver.range

import io.yegair.semver.version.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Unit test for [PlainRange]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class PlainRangeTest {

    @Nested
    inner class Parse {

        @Test
        fun partialNoPatch() {
            assertEquals(
                PlainRange(WildcardVersion(
                    major = VersionNumber.of(1),
                    minor = VersionNumber.of(2)
                )),
                "1.2".asRange()
            )
        }

        @Test
        fun partialNoMinor() {
            assertEquals(
                PlainRange(WildcardVersion(
                    major = VersionNumber.of(3)
                )),
                "3".asRange()
            )
        }

        @Test
        fun patchWildcard() {
            assertEquals(
                PlainRange(WildcardVersion(
                    major = VersionNumber.of(4),
                    minor = VersionNumber.of(3),
                    patch = Wildcard
                )),
                "4.3.X".asRange()
            )
        }

        @Test
        fun minorWildcard() {
            assertEquals(
                PlainRange(WildcardVersion(
                    major = VersionNumber.of(2),
                    minor = Wildcard
                )),
                "2.*".asRange()
            )
        }

        @Test
        fun anyVersion() {
            assertEquals(
                PlainRange(AnyVersion),
                "*".asRange()
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