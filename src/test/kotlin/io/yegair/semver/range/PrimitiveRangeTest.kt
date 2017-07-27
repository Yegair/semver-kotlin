package io.yegair.semver.range

import io.yegair.semver.range.PrimitiveRange.Operator.*
import io.yegair.semver.version.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit test for [PrimitiveRange]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class PrimitiveRangeTest {

    @Nested
    inner class Parse {

        @Test
        fun wildcard() {
            assertEquals(
                PrimitiveRange(LT, WildcardVersion(
                    major = VersionNumber.of(3),
                    minor = Wildcard
                )),
                Range.parse("< 3.x")
            )
        }

        @Test
        fun partial() {
            assertEquals(
                PrimitiveRange(GT, WildcardVersion(
                    major = VersionNumber.of(3),
                    minor = VersionNumber.of(7)
                )),
                Range.parse(">3.7")
            )
        }

        @Test
        fun full() {
            assertEquals(
                PrimitiveRange(GTE, SemanticVersion(
                    major = VersionNumber.of(3),
                    minor = VersionNumber.of(0),
                    patch = VersionNumber.of(14)
                )),
                Range.parse(">= 3.0.14")
            )
        }

        @Test
        fun prerelease() {
            assertEquals(
                PrimitiveRange(LTE, SemanticVersion(
                    major = VersionNumber.of(125),
                    minor = VersionNumber.of(12),
                    patch = VersionNumber.of(19),
                    prerelease = Prerelease.of(
                        prefix = "beta",
                        version = 3,
                        suffix = "nightly"
                    )
                )),
                Range.parse("<=125.12.19-beta.3.nightly")
            )
        }

        @Test
        fun build() {
            assertEquals(
                PrimitiveRange(EQ, SemanticVersion(
                    major = VersionNumber.of(125),
                    minor = VersionNumber.of(12),
                    patch = VersionNumber.of(19),
                    build = BuildIdentifier("build-637")
                )),
                Range.parse("= 125.12.19+build-637")
            )
        }
    }

    @Nested
    inner class Lt {

        @Test
        fun fullVersion() {
            val range = "< 1.0.1".asRange()

            assertTrue {
                range.satisfiedBy("1.0.0".asVersion())
            }
        }

        @Test
        fun minorWildcard() {
            val range = "<2.*".asRange()

            assertTrue {
                range.satisfiedBy("1.9.99".asVersion())
            }

            assertFalse {
                range.satisfiedBy("2.0.0".asVersion())
            }
        }

        @Test
        fun patchWildcard() {
            val range = "< 19.3.X".asRange()

            assertTrue {
                range.satisfiedBy("19.2.42".asVersion())
            }

            assertFalse {
                range.satisfiedBy("19.3.0".asVersion())
            }
        }

        @Test
        fun prerelease() {
            val range = "< 19.3.77-5.alpha".asRange()

            assertTrue {
                range.satisfiedBy("19.3.77-4.alpha".asVersion())
            }

            assertFalse {
                range.satisfiedBy("19.3.77-5.alpha".asVersion())
            }
        }
    }
}