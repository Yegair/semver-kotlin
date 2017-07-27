package io.yegair.semver.range

import io.yegair.semver.version.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
    }

    @Nested
    inner class SatisfiedBy {

        @Test
        fun min() {

            val range = "3".asRange()

            assertTrue {
                range.satisfiedBy("3.0.0".asVersion())
            }

            assertFalse {
                range.satisfiedBy("2.9.9".asVersion())
            }
        }

        @Test
        fun max() {

            val range = "1.2.*".asRange()

            assertTrue {
                range.satisfiedBy("1.2.99-beta.16".asVersion())
            }

            assertFalse {
                range.satisfiedBy("1.3.0".asVersion())
            }
        }
    }
}