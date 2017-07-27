package io.yegair.semver.version

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit test for [SemanticVersion]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class SemanticVersionTest {

    @Nested
    inner class Parse {

        @Test
        fun plain() {
            assertEquals(
                SemanticVersion(34, 7, 19),
                "34.7.19".asVersion()
            )
        }

        @Test
        fun missingPatch() {

            // given
            val expression = "5.7"

            // when
            val version = expression.asVersion()

            // then
            assertEquals(SemanticVersion(5, 7), "5.7".asVersion())
        }

        @Test
        fun simplePrerelease() {
            assertEquals(
                SemanticVersion(
                    major = VersionNumber.of(3),
                    minor = VersionNumber.of(2),
                    patch = VersionNumber.of(9),
                    prerelease = Prerelease.of("beta", 13)
                ),
                "3.2.9-beta.13".asVersion()
            )
        }
    }

    @Nested
    inner class NextMajor {

        @Test
        fun noMinorNoPatch() {
            assertEquals(
                SemanticVersion(4),
                SemanticVersion(3).nextMajor()
            )
        }

        @Test
        fun noPatch() {
            assertEquals(
                SemanticVersion(3, 0),
                SemanticVersion(2, 2).nextMajor()
            )
        }

        @Test
        fun basic() {
            assertEquals(
                SemanticVersion(2, 0, 0),
                SemanticVersion(1, 2, 7).nextMajor()
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                SemanticVersion(1, 0, 0),
                SemanticVersion(0, 27, 1, prerelease = "beta.7").nextMajor()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(3, 0, 0),
                SemanticVersion(2, 13, 3, build = "24").nextMajor()
            )
        }

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(20, 0, 0),
                SemanticVersion(19, 79, 24, prerelease = "alpha.23", build = "372").nextMajor()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(1),
                SemanticVersion(0).nextMajor()
            )
        }
    }

    @Nested
    inner class NextMinor {

        @Test
        fun noMinorNoPatch() {
            assertEquals(
                SemanticVersion(3, 1),
                SemanticVersion(3).nextMinor()
            )
        }

        @Test
        fun noPatch() {
            assertEquals(
                SemanticVersion(2, 3),
                SemanticVersion(2, 2).nextMinor()
            )
        }

        @Test
        fun basic() {
            assertEquals(
                SemanticVersion(1, 3, 0),
                SemanticVersion(1, 2, 7).nextMinor()
            )
        }

        @Test
        fun prerelease() {

            // TODO

            assertEquals(
                SemanticVersion(0, 28, 0),
                SemanticVersion(0, 27, 1, prerelease = "beta.7").nextMinor()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(2, 14, 0),
                SemanticVersion(2, 13, 3, build = "24").nextMinor()
            )
        }

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(19, 80, 0),
                SemanticVersion(19, 79, 24, prerelease = "alpha.23", build = "372").nextMinor()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(0, 1),
                SemanticVersion(0).nextMinor()
            )
        }
    }

    @Nested
    inner class NextPatch {

        @Test
        fun noMinorNoPatch() {
            assertEquals(
                SemanticVersion(3, 0, 1),
                SemanticVersion(3).nextPatch()
            )
        }

        @Test
        fun noPatch() {
            assertEquals(
                SemanticVersion(2, 2, 1),
                SemanticVersion(2, 2).nextPatch()
            )
        }

        @Test
        fun basic() {
            assertEquals(
                SemanticVersion(1, 2, 8),
                SemanticVersion(1, 2, 7).nextPatch()
            )
        }

        @Test
        fun prerelease() {

            // TODO

            assertEquals(
                SemanticVersion(0, 27, 1),
                SemanticVersion(0, 27, 1, prerelease = "beta.7").nextPatch()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(2, 13, 4),
                SemanticVersion(2, 13, 3, build = "24").nextPatch()
            )
        }

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(19, 79, 24),
                SemanticVersion(19, 79, 24, prerelease = "alpha.23", build = "372").nextPatch()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(0, 0, 1),
                SemanticVersion(0).nextPatch()
            )
        }
    }

    @Nested
    inner class NextBreakingChange {

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(4, 0, 0),
                SemanticVersion(3, 9, 7, "alpha.3", "47").nextBreakingChange()
            )
        }

        @Test
        fun partialNoMinorNoPatch() {
            assertEquals(
                SemanticVersion(4, 0, 0),
                SemanticVersion(3).nextBreakingChange()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                SemanticVersion(30, 0, 0),
                SemanticVersion(29, 4).nextBreakingChange()
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                SemanticVersion(0, 28, 0),
                SemanticVersion(0, 27, 1, "beta.7").nextBreakingChange()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(0, 28, 0),
                SemanticVersion(0, 27, 2, build = "33").nextBreakingChange()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(0, 0, 1),
                SemanticVersion(0).nextBreakingChange()
            )
        }

        @Test
        fun zeroZero() {
            assertEquals(
                SemanticVersion(0, 0, 1),
                SemanticVersion(0, 0).nextBreakingChange()
            )
        }

        @Test
        fun zeroZeroZero() {
            assertEquals(
                SemanticVersion(0, 0, 1),
                SemanticVersion(0, 0, 0).nextBreakingChange()
            )
        }

        @Test
        fun zeroMajor() {
            assertEquals(
                SemanticVersion(0, 3, 0),
                SemanticVersion(0, 2, 0).nextBreakingChange()
            )
        }

        @Test
        fun zeroMajorNoPatch() {
            assertEquals(
                SemanticVersion(0, 3, 0),
                SemanticVersion(0, 2).nextBreakingChange()
            )
        }

        @Test
        fun zeroMinor() {
            assertEquals(
                SemanticVersion(0, 0, 12),
                SemanticVersion(0, 0, 11).nextBreakingChange()
            )
        }
    }

    @Nested
    inner class Floor {

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(3, 9, 7, "alpha.3"),
                SemanticVersion(3, 9, 7, "alpha.3", "47").floor()
            )
        }

        @Test
        fun partialNoMinorNoPatch() {
            assertEquals(
                SemanticVersion(3, 0, 0),
                SemanticVersion(3).floor()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                SemanticVersion(29, 4, 0),
                SemanticVersion(29, 4).floor()
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                SemanticVersion(0, 27, 1, "beta.7"),
                SemanticVersion(0, 27, 1, "beta.7").floor()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(0, 27, 2),
                SemanticVersion(0, 27, 2, build = "33").floor()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(0, 0, 0),
                SemanticVersion(0).floor()
            )
        }
    }

    @Nested
    inner class Ceil {

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(3, 10, 0),
                SemanticVersion(3, 9, 7, "alpha.3", "47").ceil()
            )
        }

        @Test
        fun partialNoMinorNoPatch() {
            assertEquals(
                SemanticVersion(4, 0, 0),
                SemanticVersion(3).ceil()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                SemanticVersion(29, 5, 0),
                SemanticVersion(29, 4).ceil()
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                SemanticVersion(0, 28, 0),
                SemanticVersion(0, 27, 1, "beta.7").ceil()
            )
        }

        @Test
        fun build() {
            assertEquals(
                SemanticVersion(0, 28, 0),
                SemanticVersion(0, 27, 2, build = "33").ceil()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(1, 0, 0),
                SemanticVersion(0).ceil()
            )
        }
    }
}