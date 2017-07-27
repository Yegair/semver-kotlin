package io.yegair.semver.version

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit test for [WildcardVersion]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class WildcardVersionTest {

    @Nested
    inner class Floor {

        @Test
        fun partialNoMinorNoPatch() {
            assertEquals(
                SemanticVersion(3, 0, 0),
                WildcardVersion(3).floor()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                SemanticVersion(29, 4, 0),
                WildcardVersion(29, 4).floor()
            )
        }

        @Test
        fun minorWildcard() {
            assertEquals(
                SemanticVersion(1, 0, 0),
                WildcardVersion(1, Wildcard).floor()
            )
        }

        @Test
        fun patchWildcard() {
            assertEquals(
                SemanticVersion(2, 3, 0),
                WildcardVersion(2, 3, Wildcard).floor()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(0, 0, 0),
                WildcardVersion(0).floor()
            )
        }

        @Test
        fun zeroWildcard() {
            assertEquals(
                SemanticVersion(0, 0, 0),
                WildcardVersion(0, Wildcard).floor()
            )
        }

        @Test
        fun zeroZero() {
            assertEquals(
                SemanticVersion(0, 0, 0),
                WildcardVersion(0, 0).floor()
            )
        }

        @Test
        fun zeroZeroWildcard() {
            assertEquals(
                SemanticVersion(0, 0, 0),
                WildcardVersion(0, 0, Wildcard).floor()
            )
        }
    }

    @Nested
    inner class Ceil {

        @Test
        fun partialNoMinorNoPatch() {
            assertEquals(
                SemanticVersion(4, 0, 0),
                WildcardVersion(3).ceil()
            )
        }

        @Test
        fun partialNoPatch() {
            assertEquals(
                SemanticVersion(29, 5, 0),
                WildcardVersion(29, 4).ceil()
            )
        }

        @Test
        fun minorWildcard() {
            assertEquals(
                SemanticVersion(2, 0, 0),
                WildcardVersion(1, Wildcard).ceil()
            )
        }

        @Test
        fun patchWildcard() {
            assertEquals(
                SemanticVersion(2, 4, 0),
                WildcardVersion(2, 3, Wildcard).ceil()
            )
        }

        @Test
        fun zero() {
            assertEquals(
                SemanticVersion(1, 0, 0),
                WildcardVersion(0).ceil()
            )
        }

        @Test
        fun zeroWildcard() {
            assertEquals(
                SemanticVersion(1, 0, 0),
                WildcardVersion(0, Wildcard).ceil()
            )
        }

        @Test
        fun zeroZero() {
            assertEquals(
                SemanticVersion(0, 1, 0),
                WildcardVersion(0, 0).ceil()
            )
        }

        @Test
        fun zeroZeroWildcard() {
            assertEquals(
                SemanticVersion(0, 1, 0),
                WildcardVersion(0, 0, Wildcard).ceil()
            )
        }
    }
}