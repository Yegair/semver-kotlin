package io.yegair.semver.version

import io.yegair.semver.version.VersionNumber.Companion.One
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit test for [Prerelease]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class PrereleaseTest {

    @Nested
    inner class Parse {

        @Test
        fun full() {
            assertEquals(
                SemanticVersion(
                    major = One,
                    minor = One,
                    patch = One,
                    prerelease = Prerelease.of(
                        prefix = "beta.nightly",
                        version = 14,
                        suffix = "unstable.untested"
                    )
                ),
                Version.parse("1.1.1-beta.nightly.14.unstable.untested")
            )
        }

        @Test
        fun noSuffix() {
            assertEquals(
                SemanticVersion(
                    major = One,
                    minor = One,
                    patch = One,
                    prerelease = Prerelease.of(
                        prefix = "beta.M2",
                        version = 3
                    )
                ),
                Version.parse("1.1.1-beta.M2.3")
            )
        }

        @Test
        fun noPrefix() {
            assertEquals(
                SemanticVersion(
                    major = One,
                    minor = One,
                    patch = One,
                    prerelease = Prerelease.of(
                        version = 23,
                        suffix = "M13.unstable"
                    )
                ),
                Version.parse("1.1.1-23.M13.unstable")
            )
        }

        @Test
        fun versionOnly() {
            assertEquals(
                SemanticVersion(
                    major = One,
                    minor = One,
                    patch = One,
                    prerelease = Prerelease.of(
                        version = 42
                    )
                ),
                Version.parse("1.1.1-42")
            )
        }

        @Test
        fun multipleVersions() {
            assertEquals(
                SemanticVersion(
                    major = One,
                    minor = One,
                    patch = One,
                    prerelease = Prerelease.of(
                        prefix = "3",
                        version = 5,
                        suffix = "beta"
                    )
                ),
                Version.parse("1.1.1-3.5.beta")
            )
        }
    }

    @Nested
    inner class Compare {

        @Test
        fun equal() {

            val left = "M6.beta.13.nightly".asPrerelease()
            val right = "M6.beta.13.nightly".asPrerelease()

            assertTrue {
                (left.compareTo(right) == 0) && (left == right)
            }
        }

        @Test
        fun unequalVersion() {

            val left = "M6.beta.13.nightly".asPrerelease()
            val right = "M6.beta.9.nightly".asPrerelease()

            assertTrue {
                (left > right) && (left != right)
            }
        }

        @Test
        fun noPrefixUnequalVersion() {

            val left = "11.nightly".asPrerelease()
            val right = "9.nightly".asPrerelease()

            assertTrue {
                (left > right) && (left != right)
            }
        }

        @Test
        fun unequalPrefix() {

            val left = "M5.beta.13.nightly".asPrerelease()
            val right = "M6.beta.13.nightly".asPrerelease()

            assertTrue {
                (left < right) && (left != right)
            }
        }

        @Test
        fun unequalSuffix() {

            val left = "M4.beta.13.nightly".asPrerelease()
            val right = "M4.beta.13.stable".asPrerelease()

            assertTrue {
                (left < right) && (left != right)
            }
        }

        @Test
        fun noPrereleaseIsGreater() {

            val left = NoPrerelease
            val right = "M4.beta.13.stable".asPrerelease()

            assertTrue {
                (left > right) && (left != right)
            }
        }

        @Test
        fun noPrereleaseAreEqual() {

            val left = NoPrerelease
            val right = NoPrerelease

            assertTrue {
                (left.compareTo(right) == 0) && (left == right)
            }
        }

        @Test
        fun noPrereleaseEqualsEmptyPrerelease() {

            val left:Prerelease = NoPrerelease
            val right:Prerelease = PrereleaseIdentifier()

            assertTrue {
                (left.compareTo(right) == 0) && (left == right)
            }
        }
    }
}