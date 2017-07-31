package io.yegair.semver.range

import io.yegair.semver.range.PrimitiveRange.Operator.*
import io.yegair.semver.version.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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

        @Nested
        inner class SatisfiedBy {


            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+77,         true",
                "0.0.0-RC.4,       false",
                "0.0.0-RC.4+5,     false",

                "0.0.1,            true",
                "0.0.1-5.beta,     false",
                "0.0.1+foobar,     true",
                "0.0.1-5.beta+foo, false",

                "0.4.2,            true",
                "0.4.2-beta.2,     false",
                "0.4.2+63,         true",
                "0.4.2-beta.2+42,  false",

                "8.7.0,            true",
                "8.7.0-5.GA,       false",
                "8.7.0+63,         true",
                "8.7.0-5.GA+42,    false",

                "8.7.1,            false",
                "8.7.1-FOO.1,      false",
                "8.7.1+92,         false",
                "8.7.1-FOO.1+2,    false",
                "8.7.2,            false",
                "9.0.0,            false"
            )
            fun full(version: String, match: Boolean) {

                // [0.0.0 .. 8.7.1)
                val range = "< 8.7.1"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+1,          true",
                "0.0.0-RC.2,       false",
                "0.0.0-RC.1,       false",
                "0.0.0-RC.2+7,     false",
                "0.0.0-RC.1+7,     false",

                "0.0.2,            true",
                "0.0.2+4,          true",
                "0.0.2-RC.2,       false",
                "0.0.2-RC.1,       false",
                "0.0.2-RC.2+3,     false",
                "0.0.2-RC.1+1,     false",

                "0.8.42,           true",
                "0.8.42+63,        true",
                "0.8.42-RC.2,      false",
                "0.8.42-RC.0,      false",
                "0.8.42-RC.2+42,   false",
                "0.8.42-RC.0+1,    false",

                "0.9.56,           true",
                "0.9.56+22,        true",
                "0.9.56-RC.2,      false",
                "0.9.56-RC,        false",
                "0.9.56-RC.2+33,   false",
                "0.9.56-RC+0,      false",

                "0.9.57,           false",
                "0.9.57+13,        false",
                "0.9.57-RC.2,      false",
                "0.9.57-RC.1,      true",
                "0.9.57-RC.0,      true",
                "0.9.57-RC,        true",
                "0.9.57-RC.2+64,   false",
                "0.9.57-RC.1+33,   true",
                "0.9.57-RC.0+2,    true",
                "0.9.57-RC+42,     true",
                "0.9.58,           false",
                "0.9.58-RC.1,      false",
                "0.9.58-ALPHA.1,   false",
                "1.0.0,            false"
            )
            fun prerelease(version: String, match: Boolean) {

                // [0.0.0 .. 0.9.57-RC.2)
                val range = "< 0.9.57-RC.2"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+77,         true",
                "0.0.0-RC.4,       false",
                "0.0.0-RC.4+5,     false",

                "0.0.1,            true",
                "0.0.1-5.beta,     false",
                "0.0.1+foobar,     true",
                "0.0.1-5.beta+foo, false",

                "0.4.2,            true",
                "0.4.2-beta.2,     false",
                "0.4.2+63,         true",
                "0.4.2-beta.2+42,  false",

                "1.7.0,            true",
                "1.7.0-5.GA,       false",
                "1.7.0+63,         true",
                "1.7.0-5.GA+42,    false",

                "2.0.0,            false",
                "2.0.0-FOO.1,      false",
                "2.0.0+92,         false",
                "2.0.0-FOO.1+2,    false",
                "2.0.1,            false",
                "2.1.0,            false",
                "3.0.0,            false"
            )
            fun minorWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 2.0.0)
                val range = "< 2.*"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+23b,        true",
                "0.0.0-2.GA,       false",
                "0.0.0-2.GA+5,     false",

                "0.0.6,            true",
                "0.0.6-5.alpha,    false",
                "0.0.6+0fe,        true",
                "0.0.6-5.alpha+00, false",

                "0.3.12,           true",
                "0.3.12-beta.9,    false",
                "0.3.12+-1,        true",
                "0.3.12-beta.9+07, false",

                "6.2.4,            true",
                "6.2.4-5.GA,       false",
                "6.2.4+63,         true",
                "6.2.4-5.GA+42,    false",

                "7.4.0,            false",
                "7.4.0-FOO.1,      false",
                "7.4.0+92,         false",
                "7.4.0-FOO.1+2,    false",
                "7.4.1,            false",
                "7.5.0,            false",
                "8.0.0,            false"
            )
            fun patchWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 7.4.0)
                val range = "<7.4.X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             true",
                "0.0.0+f00bar,      true",
                "0.0.0-M6,          false",
                "0.0.0-M6+5,        false",

                "0.0.99,            true",
                "0.0.99-5.beta,     false",
                "0.0.99+foobar,     true",
                "0.0.99-5.beta+foo, false",

                "0.14.7,            true",
                "0.14.7-M5.2,       false",
                "0.14.7+63,         true",
                "0.14.7-M5.2+42,    false",

                "11.2.0,            true",
                "11.2.0-M9.0,       false",
                "11.2.0+63,         true",
                "11.2.0-M9.0+42,    false",

                "12.0.0,            false",
                "12.0.0-FOO.1,      false",
                "12.0.0+92,         false",
                "12.0.0-FOO.1+2,    false",
                "12.0.1,            false",
                "12.1.0,            false",
                "13.0.0,            false"
            )
            fun minorWildcardPatchWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 12.0.0)
                val range = "<  12.*.x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             true",
                "0.0.0+yolo,        true",
                "0.0.0-RC.4,        false",
                "0.0.0-RC.4+yolo,   false",

                "0.0.42,            true",
                "0.0.42-5.beta,     false",
                "0.0.42+foobar,     true",
                "0.0.42-5.beta+foo, false",

                "0.42.0,            true",
                "0.42.0-beta.2,     false",
                "0.42.0+63,         true",
                "0.42.0-beta.2+42,  false",

                "41.99.99,          true",
                "41.99.99-42,       false",
                "41.99.99+63,       true",
                "41.99.99-42+42,    false",

                "42.0.0,            false",
                "42.0.0-42,         false",
                "42.0.0+92,         false",
                "42.0.0-42+42,      false",
                "42.0.1,            false",
                "42.1.0,            false",
                "43.0.0,            false"
            )
            fun partialMajor(version: String, match: Boolean) {

                // [0.0.0 .. 42.0.0)
                val range = "<   42"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,               true",
                "0.0.0+23b,           true",
                "0.0.0-2.GA,          false",
                "0.0.0-2.GA+5,        false",

                "0.0.1337,            true",
                "0.0.1337-5.alpha,    false",
                "0.0.1337+0fe,        true",
                "0.0.1337-5.alpha+00, false",

                "0.133.7,             true",
                "0.133.7-R.9,         false",
                "0.133.7+-1,          true",
                "0.133.7-R.9+07,      false",

                "33.7.1,              true",
                "33.7.1-5.GA,         false",
                "33.7.1+63,           true",
                "33.7.1-5.GA+42,      false",

                "133.7.0,             false",
                "133.7.0-FOO.1,       false",
                "133.7.0+92,          false",
                "133.7.0-FOO.1+2,     false",
                "133.7.1,             false",
                "133.8.0,             false",
                "134.0.0,             false"
            )
            fun partialMajorMinor(version: String, match: Boolean) {

                // [0.0.0 .. 133.7.0)
                val range = "<133.7"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,           false",
                "0.0.0+42,        false",
                "0.0.0-GA.4,      false",
                "0.0.0-GA.4+11,   false",

                "0.0.1,           false",
                "0.0.1-RC.0,      false",
                "0.0.1+0a,        false",
                "0.0.1-RC.0+00f,  false",

                "0.12.0,          false",
                "0.12.0-beta,     false",
                "0.12.0+0a,       false",
                "0.12.0-beta+2,   false",

                "1.0.1,           false",
                "1.0.1-beta,      false",
                "1.0.1+0a,        false",
                "1.0.1-beta+2,    false",

                "4042.2.1,        false",
                "4042.2.1-beta,   false",
                "4042.2.1+0a,     false",
                "4042.2.1-beta+2, false"
            )
            fun anyVersion(version: String, match: Boolean) {

                // [0.0.0 .. *)
                val range = "< x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }
        }
    }

    @Nested
    inner class Lte {

        @Nested
        inner class SatisfiedBy {


            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+77,         true",
                "0.0.0-RC.4,       false",
                "0.0.0-RC.4+5,     false",

                "0.0.1,            true",
                "0.0.1-5.beta,     false",
                "0.0.1+foobar,     true",
                "0.0.1-5.beta+foo, false",

                "0.4.2,            true",
                "0.4.2-beta.2,     false",
                "0.4.2+63,         true",
                "0.4.2-beta.2+42,  false",

                "8.7.0,            true",
                "8.7.0-5.GA,       false",
                "8.7.0+63,         true",
                "8.7.0-5.GA+42,    false",

                "8.7.1,            true",
                "8.7.1-FOO.1,      false",
                "8.7.1+92,         true",
                "8.7.1-FOO.1+2,    false",

                "8.7.2,            false",
                "8.7.2-FOO.1,      false",
                "8.7.2+92,         false",
                "8.7.2-FOO.1+2,    false",

                "9.0.0,            false"
            )
            fun full(version: String, match: Boolean) {

                // [0.0.0 .. 8.7.1]
                val range = "<= 8.7.1"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,                  true",
                "0.0.0+1,                true",
                "0.0.0-M6.2.nightly,     false",
                "0.0.0-M6.1.nightly,     false",
                "0.0.0-M6.2.nightly+7,   false",
                "0.0.0-M6.1.nightly+7,   false",

                "0.0.2,                  true",
                "0.0.2+4,                true",
                "0.0.2-M6.2.nightly,     false",
                "0.0.2-M6.1.nightly,     false",
                "0.0.2-M6.2.nightly+3,   false",
                "0.0.2-M6.1.nightly+1,   false",

                "0.8.42,                 true",
                "0.8.42+63,              true",
                "0.8.42-M6.2.nightly,    false",
                "0.8.42-M6.0.nightly,    false",
                "0.8.42-M6.2.nightly+42, false",
                "0.8.42-M6.nightly+1,    false",

                "3.6.56,                 true",
                "3.6.56+22,              true",
                "3.6.56-M6.2.nightly,    false",
                "3.6.56-M6,              false",
                "3.6.56-M6.2.nightly+33, false",
                "3.6.56-M6.0+0,          false",

                "3.7.42,                 false",
                "3.7.42+13,              false",
                "3.7.42-M7,              false",
                "3.7.42-M7.2.nightly,    false",
                "3.7.42-M7.2.nightly+7,  false",
                "3.7.42-M7.1.nightly,    false",
                "3.7.42-M7.1.nightly+0,  false",
                "3.7.42-M6.3.nightly,    false",
                "3.7.42-M6.2.nightly,    true",
                "3.7.42-M6.2.nightly+22, true",
                "3.7.42-M6.1.nightly,    true",
                "3.7.42-M6.1.nightly+0,  true",
                "3.7.42-M6,              true",
                "3.7.42-M6+b.12,         true",
                "3.7.42-M5.1.nightly,    true",
                "3.7.42-M5.1.nightly+10, true",
                "3.7.42-M5,              true",
                "3.7.42-M5+117,          true",

                "3.7.43,                 false",
                "3.7.43+0,               false",
                "3.7.43-M6.2.nightly,    false",
                "3.7.43-M6.2.nightly+1,  false",
                "3.7.43-M6.1.nightly,    false",
                "3.7.43-M6.1.nightly+42, false",
                "3.7.43-M5,              false",

                "3.8.0,                  false",
                "3.8.0+f3,               false",
                "3.8.0-M6.2.nightly,     false",
                "3.8.0-M6.2.nightly+10,  false",
                "3.8.0-M5,               false",

                "4.0.0,                  false",
                "4.0.0+42,               false",
                "4.0.0-M7.1.nightly,     false",
                "4.0.0-M6.2.nightly+42,  false",
                "4.0.0-M5,               false"
            )
            fun prerelease(version: String, match: Boolean) {

                // [0.0.0 .. 3.7.42-M6.2.nightly]
                val range = "<=3.7.42-M6.2.nightly"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+77,         true",
                "0.0.0-RC.4,       false",
                "0.0.0-RC,         false",
                "0.0.0-RC.4+5,     false",
                "0.0.0-RC+3,       false",

                "0.0.6,            true",
                "0.0.6-GA.7,       false",
                "0.0.6-GA,         false",
                "0.0.6+foobar,     true",
                "0.0.6-GA.7+c3,    false",
                "0.0.6-GA+c3,      false",

                "0.4.2,            true",
                "0.4.2-beta.2,     false",
                "0.4.2-beta,       false",
                "0.4.2+63,         true",
                "0.4.2-beta.2+42,  false",
                "0.4.2-beta+42,    false",

                "1.3.3,            true",
                "1.3.3-5.GA,       false",
                "1.3.3+63,         true",
                "1.3.3-5.GA+42,    false",

                "2.0.0,            true",
                "2.0.0-FOO.1,      false",
                "2.0.0+92,         true",
                "2.0.0-FOO.1+2,    false",
                "2.0.1,            false",
                "2.0.1-RC.3,       false",
                "2.0.1-RC,         false",
                "2.0.1+22,         false",
                "2.1.0,            false",
                "3.0.0,            false"
            )
            fun minorWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 2.0.0]
                val range = "<=  2.*"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            true",
                "0.0.0+23b,        true",
                "0.0.0-2.GA,       false",
                "0.0.0-2.GA+5,     false",

                "0.0.6,            true",
                "0.0.6-5.alpha,    false",
                "0.0.6+0fe,        true",
                "0.0.6-5.alpha+00, false",

                "0.3.12,           true",
                "0.3.12-beta.9,    false",
                "0.3.12+-1,        true",
                "0.3.12-beta.9+07, false",

                "6.2.4,            true",
                "6.2.4-5.GA,       false",
                "6.2.4+63,         true",
                "6.2.4-5.GA+42,    false",

                "7.4.0,            true",
                "7.4.0-FOO.1,      false",
                "7.4.0+92,         true",
                "7.4.0-FOO.1+2,    false",
                "7.4.1,            false",
                "7.5.0,            false",
                "8.0.0,            false"
            )
            fun patchWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 7.4.0]
                val range = "<= 7.4.X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             true",
                "0.0.0+f00bar,      true",
                "0.0.0-M6,          false",
                "0.0.0-M6+5,        false",

                "0.0.99,            true",
                "0.0.99-5.beta,     false",
                "0.0.99+foobar,     true",
                "0.0.99-5.beta+foo, false",

                "0.14.7,            true",
                "0.14.7-M5.2,       false",
                "0.14.7+63,         true",
                "0.14.7-M5.2+42,    false",

                "11.2.0,            true",
                "11.2.0-M9.0,       false",
                "11.2.0+63,         true",
                "11.2.0-M9.0+42,    false",

                "12.0.0,            true",
                "12.0.0-FOO.1,      false",
                "12.0.0+92,         true",
                "12.0.0-FOO.1+2,    false",
                "12.0.1,            false",
                "12.1.0,            false",
                "13.0.0,            false"
            )
            fun minorWildcardPatchWildcard(version: String, match: Boolean) {

                // [0.0.0 .. 12.0.0]
                val range = "<=  12.*.x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             true",
                "0.0.0+yolo,        true",
                "0.0.0-RC.4,        false",
                "0.0.0-RC.4+yolo,   false",

                "0.0.42,            true",
                "0.0.42-5.beta,     false",
                "0.0.42+foobar,     true",
                "0.0.42-5.beta+foo, false",

                "0.42.0,            true",
                "0.42.0-beta.2,     false",
                "0.42.0+63,         true",
                "0.42.0-beta.2+42,  false",

                "41.99.99,          true",
                "41.99.99-42,       false",
                "41.99.99+63,       true",
                "41.99.99-42+42,    false",

                "42.0.0,            true",
                "42.0.0-42,         false",
                "42.0.0+92,         true",
                "42.0.0-42+42,      false",
                "42.0.1,            false",
                "42.1.0,            false",
                "43.0.0,            false"
            )
            fun partialMajor(version: String, match: Boolean) {

                // [0.0.0 .. 42.0.0]
                val range = "<=42"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,               true",
                "0.0.0+23b,           true",
                "0.0.0-2.GA,          false",
                "0.0.0-2.GA+5,        false",

                "0.0.1337,            true",
                "0.0.1337-5.alpha,    false",
                "0.0.1337+0fe,        true",
                "0.0.1337-5.alpha+00, false",

                "0.133.7,             true",
                "0.133.7-R.9,         false",
                "0.133.7+-1,          true",
                "0.133.7-R.9+07,      false",

                "33.7.1,              true",
                "33.7.1-5.GA,         false",
                "33.7.1+63,           true",
                "33.7.1-5.GA+42,      false",

                "133.7.0,             true",
                "133.7.0-FOO.1,       false",
                "133.7.0+92,          true",
                "133.7.0-FOO.1+2,     false",
                "133.7.1,             false",
                "133.8.0,             false",
                "134.0.0,             false"
            )
            fun partialMajorMinor(version: String, match: Boolean) {

                // [0.0.0 .. 133.7.0]
                val range = "<=133.7"

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
                val range = "<= X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }
        }
    }

    @Nested
    inner class Gt {

        @Nested
        inner class SatisfiedBy {


            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+77,         false",
                "0.0.0-RC.4,       false",
                "0.0.0-RC.4+5,     false",

                "0.0.1,            false",
                "0.0.1-5.beta,     false",
                "0.0.1+foobar,     false",
                "0.0.1-5.beta+foo, false",

                "0.4.2,            false",
                "0.4.2-beta.2,     false",
                "0.4.2+63,         false",
                "0.4.2-beta.2+42,  false",

                "2.3.1,            false",
                "2.3.1-5.GA,       false",
                "2.3.1+63,         false",
                "2.3.1-5.GA+42,    false",

                "2.9.8,            false",
                "2.9.8-GA.5,       false",
                "2.9.8-GA,         false",
                "2.9.8+63,         false",
                "2.9.8-GA.5+64,    false",
                "2.9.8-GA+75,      false",

                "2.9.9,            true",
                "2.9.9-GA.5,       false",
                "2.9.9-GA,         false",
                "2.9.9+63,         true",
                "2.9.9-GA.5+64,    false",
                "2.9.9-GA+75,      false",

                "4.7.5,            true",
                "4.7.5-GA.5,       false",
                "4.7.5-GA,         false",
                "4.7.5+63,         true",
                "4.7.5-GA.5+64,    false",
                "4.7.5-GA+75,      false"
            )
            fun full(version: String, match: Boolean) {

                // (2.9.8 .. *)
                val range = "> 2.9.8"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,                  false",
                "0.0.0+1,                false",
                "0.0.0-M6.2.nightly,     false",
                "0.0.0-M6.1.nightly,     false",
                "0.0.0-M6.2.nightly+7,   false",
                "0.0.0-M6.1.nightly+7,   false",

                "0.0.2,                  false",
                "0.0.2+4,                false",
                "0.0.2-M6.2.nightly,     false",
                "0.0.2-M6.1.nightly,     false",
                "0.0.2-M6.2.nightly+3,   false",
                "0.0.2-M6.1.nightly+1,   false",
                "0.0.2-M6.2,             false",
                "0.0.2-M6.1,             false",
                "0.0.2-M6,               false",

                "0.8.42,                 false",
                "0.8.42+63,              false",
                "0.8.42-M6.2.nightly,    false",
                "0.8.42-M6.0.nightly,    false",
                "0.8.42-M6.2,            false",
                "0.8.42-M6.1,            false",
                "0.8.42-M6,              false",
                "0.8.42-M6.2.nightly+42, false",
                "0.8.42-M6.nightly+1,    false",

                "3.6.56,                 false",
                "3.6.56+22,              false",
                "3.6.56-M6.2.nightly,    false",
                "3.6.56-M6,              false",
                "3.6.56-M6.2.nightly+33, false",
                "3.6.56-M6.0+0,          false",

                "3.7.41,                 false",
                "3.7.41+0,               false",
                "3.7.41-M6.2.nightly,    false",
                "3.7.41-M6.2.nightly+1,  false",
                "3.7.41-M6.1.nightly,    false",
                "3.7.41-M6.1.nightly+42, false",
                "3.7.41-M6.2,            false",
                "3.7.41-M6.0,            false",
                "3.7.41-M6,              false",
                "3.7.41-M5,              false",

                "3.7.42,                 true",
                "3.7.42+13,              true",
                "3.7.42-M7,              true",
                "3.7.42-M7.2.nightly,    true",
                "3.7.42-M7.2.nightly+7,  true",
                "3.7.42-M7.1.nightly,    true",
                "3.7.42-M7.1.nightly+0,  true",
                "3.7.42-M7.0,            true",
                "3.7.42-M7,              true",
                "3.7.42-M6.3.nightly,    true",
                "3.7.42-M6.32,           true",
                "3.7.42-M6.3,            true",
                "3.7.42-M6.2.nightly,    false", // <-
                "3.7.42-M6.2.nightly+22, false",
                "3.7.42-M6.2,            false",
                "3.7.42-M6.1.nightly,    false",
                "3.7.42-M6.1.nightly+0,  false",
                "3.7.42-M6.1,            false",
                "3.7.42-M6,              false",
                "3.7.42-M6+b.12,         false",
                "3.7.42-M5.3.nightly,    false",
                "3.7.42-M5.2.nightly,    false",
                "3.7.42-M5.1.nightly,    false",
                "3.7.42-M5.1.nightly+10, false",
                "3.7.42-M5,              false",
                "3.7.42-M5+117,          false",

                "3.7.43,                 true",
                "3.7.43+0,               true",
                "3.7.43-M6.2.nightly,    false",
                "3.7.43-M6.2.nightly+1,  false",
                "3.7.43-M6.1.nightly,    false",
                "3.7.43-M6.1.nightly+42, false",
                "3.7.43-M5,              false",

                "3.8.0,                  true",
                "3.8.0+f3,               true",
                "3.8.0-M6.2.nightly,     false",
                "3.8.0-M6.2.nightly+10,  false",
                "3.8.0-M5,               false",

                "4.0.0,                  true",
                "4.0.0+42,               true",
                "4.0.0-M7.1.nightly,     false",
                "4.0.0-M6.2.nightly+42,  false",
                "4.0.0-M5,               false"
            )
            fun prerelease(version: String, match: Boolean) {

                // (3.7.42-M6.2.nightly .. *)
                val range = ">3.7.42-M6.2.nightly"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+77,         false",
                "0.0.0-RC.4,       false",
                "0.0.0-RC,         false",
                "0.0.0-RC.4+5,     false",
                "0.0.0-RC+3,       false",

                "0.0.6,            false",
                "0.0.6-GA.7,       false",
                "0.0.6-GA,         false",
                "0.0.6+foobar,     false",
                "0.0.6-GA.7+c3,    false",
                "0.0.6-GA+c3,      false",

                "0.4.2,            false",
                "0.4.2-beta.2,     false",
                "0.4.2-beta,       false",
                "0.4.2+63,         false",
                "0.4.2-beta.2+42,  false",
                "0.4.2-beta+42,    false",

                "1.3.3,            false",
                "1.3.3-5.GA,       false",
                "1.3.3+63,         false",
                "1.3.3-5.GA+42,    false",

                "2.0.0,            false",
                "2.0.0-FOO.1,      false",
                "2.0.0+92,         false",
                "2.0.0-FOO.1+2,    false",

                "2.0.1,            false",
                "2.0.1-RC.3,       false",
                "2.0.1-RC,         false",
                "2.0.1+22,         false",

                "2.1.0,            false",
                "2.1.0-GA.7,       false",
                "2.1.0-GA.7+7,     false",
                "2.1.0+21,         false",

                "3.0.0,            true",
                "3.0.0-GA.7,       false",
                "3.0.0-GA.7+7,     false",
                "3.0.0+21,         true",

                "45.3.9,           true",
                "45.3.9-M9.77,     false",
                "45.3.9-M9.77+10,  false",
                "45.3.9+5,         true"
            )
            fun minorWildcard(version: String, match: Boolean) {

                // [3.0.0 .. *)
                val range = ">  2.*"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+23b,        false",
                "0.0.0-2.GA,       false",
                "0.0.0-2.GA+5,     false",

                "0.0.6,            false",
                "0.0.6-5.alpha,    false",
                "0.0.6+0fe,        false",
                "0.0.6-5.alpha+00, false",

                "0.3.12,           false",
                "0.3.12-beta.9,    false",
                "0.3.12+-1,        false",
                "0.3.12-beta.9+07, false",

                "6.2.4,            false",
                "6.2.4-5.GA,       false",
                "6.2.4+63,         false",
                "6.2.4-5.GA+42,    false",

                "7.3.99,           false",
                "7.3.99-5.GA,      false",
                "7.3.99+63,        false",
                "7.3.99-5.GA+42,   false",

                "7.4.0,            false",
                "7.4.0-FOO.1,      false",
                "7.4.0+92,         false",
                "7.4.0-FOO.1+2,    false",

                "7.4.7,            false",
                "7.4.7-FOO.1,      false",
                "7.4.7+92,         false",
                "7.4.7-FOO.1+2,    false",

                "7.5.0,            true",
                "7.5.0-FOO.1,      false",
                "7.5.0+92,         true",
                "7.5.0-FOO.1+2,    false",

                "7.5.1,            true",
                "7.6.0,            true",
                "8.0.0,            true"
            )
            fun patchWildcard(version: String, match: Boolean) {

                // [7.5.0 .. *)
                val range = "> 7.4.X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             false",
                "0.0.0+f00bar,      false",
                "0.0.0-M6,          false",
                "0.0.0-M6+5,        false",

                "0.0.99,            false",
                "0.0.99-5.beta,     false",
                "0.0.99+foobar,     false",
                "0.0.99-5.beta+foo, false",

                "0.14.7,            false",
                "0.14.7-M5.2,       false",
                "0.14.7+63,         false",
                "0.14.7-M5.2+42,    false",

                "11.2.0,            false",
                "11.2.0-M9.0,       false",
                "11.2.0+63,         false",
                "11.2.0-M9.0+42,    false",

                "12.0.0,            false",
                "12.0.0-FOO.1,      false",
                "12.0.0+92,         false",
                "12.0.0-FOO.1+2,    false",

                "12.0.2,            false",
                "12.0.2-FOO.1,      false",
                "12.0.2+92,         false",
                "12.0.2-FOO.1+2,    false",

                "12.2.0,            false",
                "12.2.0-FOO.1,      false",
                "12.2.0+92,         false",
                "12.2.0-FOO.1+2,    false",

                "13.0.0,            true",
                "13.0.0-FOO.1,      false",
                "13.0.0+92,         true",
                "13.0.0-FOO.1+2,    false",

                "17.3.4,            true",
                "17.3.4-FOO.1,      false",
                "17.3.4+92,         true",
                "17.3.4-FOO.1+2,    false"
            )
            fun minorWildcardPatchWildcard(version: String, match: Boolean) {

                // [13.0.0 .. *)
                val range = "> 12.*.x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             false",
                "0.0.0+yolo,        false",
                "0.0.0-RC.4,        false",
                "0.0.0-RC.4+yolo,   false",

                "0.0.42,            false",
                "0.0.42-5.beta,     false",
                "0.0.42+foobar,     false",
                "0.0.42-5.beta+foo, false",

                "0.42.0,            false",
                "0.42.0-beta.2,     false",
                "0.42.0+63,         false",
                "0.42.0-beta.2+42,  false",

                "41.99.99,          false",
                "41.99.99-42,       false",
                "41.99.99+63,       false",
                "41.99.99-42+42,    false",

                "42.0.0,            false",
                "42.0.0-42,         false",
                "42.0.0+92,         false",
                "42.0.0-42+42,      false",

                "42.0.3,            false",
                "42.0.3-42,         false",
                "42.0.3+92,         false",
                "42.0.3-42+42,      false",

                "42.4.0,            false",
                "42.4.0-42,         false",
                "42.4.0+92,         false",
                "42.4.0-42+42,      false",

                "43.0.0,            true",
                "43.0.0-42,         false",
                "43.0.0+92,         true",
                "43.0.0-42+42,      false",

                "122.2.7,           true",
                "122.2.7-42,        false",
                "122.2.7+92,        true",
                "122.2.7-42+42,     false"
            )
            fun partialMajor(version: String, match: Boolean) {

                // [43.0.0 .. *)
                val range = "> 42"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,               false",
                "0.0.0+23b,           false",
                "0.0.0-2.GA,          false",
                "0.0.0-2.GA+5,        false",

                "0.0.1337,            false",
                "0.0.1337-5.alpha,    false",
                "0.0.1337+0fe,        false",
                "0.0.1337-5.alpha+00, false",

                "0.133.7,             false",
                "0.133.7-R.9,         false",
                "0.133.7+-1,          false",
                "0.133.7-R.9+07,      false",

                "33.7.1,              false",
                "33.7.1-5.GA,         false",
                "33.7.1+63,           false",
                "33.7.1-5.GA+42,      false",

                "133.7.0,             false",
                "133.7.0-FOO.1,       false",
                "133.7.0+92,          false",
                "133.7.0-FOO.1+2,     false",

                "133.7.13,            false",
                "133.7.13-FOO.1,      false",
                "133.7.13+92,         false",
                "133.7.13-FOO.1+2,    false",

                "133.8.0,             true",
                "133.8.0-FOO.1,       false",
                "133.8.0+92,          true",
                "133.8.0-FOO.1+2,     false",

                "1001.0.1,            true",
                "1001.0.1-FOO.1,      false",
                "1001.0.1+92,         true",
                "1001.0.1-FOO.1+2,    false"
            )
            fun partialMajorMinor(version: String, match: Boolean) {

                // [133.8.0 .. *)
                val range = ">133.7"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,           false",
                "0.0.0+42,        false",
                "0.0.0-GA.4,      false",
                "0.0.0-GA.4+11,   false",

                "0.0.1,           false",
                "0.0.1-RC.0,      false",
                "0.0.1+0a,        false",
                "0.0.1-RC.0+00f,  false",

                "0.12.0,          false",
                "0.12.0-beta,     false",
                "0.12.0+0a,       false",
                "0.12.0-beta+2,   false",

                "1.0.1,           false",
                "1.0.1-beta,      false",
                "1.0.1+0a,        false",
                "1.0.1-beta+2,    false",

                "4042.2.1,        false",
                "4042.2.1-beta,   false",
                "4042.2.1+0a,     false",
                "4042.2.1-beta+2, false"
            )
            fun anyVersion(version: String, match: Boolean) {

                // [0.0.0 .. *)
                val range = "> X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }
        }
    }

    @Nested
    inner class Gte {

        @Nested
        inner class SatisfiedBy {


            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+77,         false",
                "0.0.0-RC.4,       false",
                "0.0.0-RC.4+5,     false",

                "0.0.1,            false",
                "0.0.1-5.beta,     false",
                "0.0.1+foobar,     false",
                "0.0.1-5.beta+foo, false",

                "0.4.2,            false",
                "0.4.2-beta.2,     false",
                "0.4.2+63,         false",
                "0.4.2-beta.2+42,  false",

                "2.3.1,            false",
                "2.3.1-5.GA,       false",
                "2.3.1+63,         false",
                "2.3.1-5.GA+42,    false",

                "2.9.8,            true",
                "2.9.8-GA.5,       false",
                "2.9.8-GA,         false",
                "2.9.8+63,         true",
                "2.9.8-GA.5+64,    false",
                "2.9.8-GA+75,      false",

                "2.9.9,            true",
                "2.9.9-GA.5,       false",
                "2.9.9-GA,         false",
                "2.9.9+63,         true",
                "2.9.9-GA.5+64,    false",
                "2.9.9-GA+75,      false",

                "4.7.5,            true",
                "4.7.5-GA.5,       false",
                "4.7.5-GA,         false",
                "4.7.5+63,         true",
                "4.7.5-GA.5+64,    false",
                "4.7.5-GA+75,      false"
            )
            fun full(version: String, match: Boolean) {

                // [2.9.8 .. *)
                val range = ">= 2.9.8"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,                  false",
                "0.0.0+1,                false",
                "0.0.0-M6.2.nightly,     false",
                "0.0.0-M6.1.nightly,     false",
                "0.0.0-M6.2.nightly+7,   false",
                "0.0.0-M6.1.nightly+7,   false",

                "0.0.2,                  false",
                "0.0.2+4,                false",
                "0.0.2-M6.2.nightly,     false",
                "0.0.2-M6.1.nightly,     false",
                "0.0.2-M6.2.nightly+3,   false",
                "0.0.2-M6.1.nightly+1,   false",
                "0.0.2-M6.2,             false",
                "0.0.2-M6.1,             false",
                "0.0.2-M6,               false",

                "0.8.42,                 false",
                "0.8.42+63,              false",
                "0.8.42-M6.2.nightly,    false",
                "0.8.42-M6.0.nightly,    false",
                "0.8.42-M6.2,            false",
                "0.8.42-M6.1,            false",
                "0.8.42-M6,              false",
                "0.8.42-M6.2.nightly+42, false",
                "0.8.42-M6.nightly+1,    false",

                "3.6.56,                 false",
                "3.6.56+22,              false",
                "3.6.56-M6.2.nightly,    false",
                "3.6.56-M6,              false",
                "3.6.56-M6.2.nightly+33, false",
                "3.6.56-M6.0+0,          false",

                "3.7.41,                 false",
                "3.7.41+0,               false",
                "3.7.41-M6.2.nightly,    false",
                "3.7.41-M6.2.nightly+1,  false",
                "3.7.41-M6.1.nightly,    false",
                "3.7.41-M6.1.nightly+42, false",
                "3.7.41-M6.2,            false",
                "3.7.41-M6.0,            false",
                "3.7.41-M6,              false",
                "3.7.41-M5,              false",

                "3.7.42,                 true",
                "3.7.42+13,              true",
                "3.7.42-M7,              true",
                "3.7.42-M7.2.nightly,    true",
                "3.7.42-M7.2.nightly+7,  true",
                "3.7.42-M7.1.nightly,    true",
                "3.7.42-M7.1.nightly+0,  true",
                "3.7.42-M7.0,            true",
                "3.7.42-M7,              true",
                "3.7.42-M6.3.nightly,    true",
                "3.7.42-M6.32,           true",
                "3.7.42-M6.3,            true",
                "3.7.42-M6.2.nightly,    true", // <-
                "3.7.42-M6.2.nightly+22, true",
                "3.7.42-M6.2,            false",
                "3.7.42-M6.1.nightly,    false",
                "3.7.42-M6.1.nightly+0,  false",
                "3.7.42-M6.1,            false",
                "3.7.42-M6,              false",
                "3.7.42-M6+b.12,         false",
                "3.7.42-M5.3.nightly,    false",
                "3.7.42-M5.2.nightly,    false",
                "3.7.42-M5.1.nightly,    false",
                "3.7.42-M5.1.nightly+10, false",
                "3.7.42-M5,              false",
                "3.7.42-M5+117,          false",

                "3.7.43,                 true",
                "3.7.43+0,               true",
                "3.7.43-M6.2.nightly,    false",
                "3.7.43-M6.2.nightly+1,  false",
                "3.7.43-M6.1.nightly,    false",
                "3.7.43-M6.1.nightly+42, false",
                "3.7.43-M5,              false",

                "3.8.0,                  true",
                "3.8.0+f3,               true",
                "3.8.0-M6.2.nightly,     false",
                "3.8.0-M6.2.nightly+10,  false",
                "3.8.0-M5,               false",

                "4.0.0,                  true",
                "4.0.0+42,               true",
                "4.0.0-M7.1.nightly,     false",
                "4.0.0-M6.2.nightly+42,  false",
                "4.0.0-M5,               false"
            )
            fun prerelease(version: String, match: Boolean) {

                // [3.7.42-M6.2.nightly .. *)
                val range = ">=3.7.42-M6.2.nightly"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+77,         false",
                "0.0.0-RC.4,       false",
                "0.0.0-RC,         false",
                "0.0.0-RC.4+5,     false",
                "0.0.0-RC+3,       false",

                "0.0.6,            false",
                "0.0.6-GA.7,       false",
                "0.0.6-GA,         false",
                "0.0.6+foobar,     false",
                "0.0.6-GA.7+c3,    false",
                "0.0.6-GA+c3,      false",

                "0.4.2,            false",
                "0.4.2-beta.2,     false",
                "0.4.2-beta,       false",
                "0.4.2+63,         false",
                "0.4.2-beta.2+42,  false",
                "0.4.2-beta+42,    false",

                "1.3.3,            false",
                "1.3.3-5.GA,       false",
                "1.3.3+63,         false",
                "1.3.3-5.GA+42,    false",

                "2.0.0,            true",
                "2.0.0-FOO.1,      false",
                "2.0.0+92,         true",
                "2.0.0-FOO.1+2,    false",

                "2.0.1,            true",
                "2.0.1-RC.3,       false",
                "2.0.1-RC,         false",
                "2.0.1+22,         true",

                "2.1.0,            true",
                "2.1.0-GA.7,       false",
                "2.1.0-GA.7+7,     false",
                "2.1.0+21,         true",

                "3.0.0,            true",
                "3.0.0-GA.7,       false",
                "3.0.0-GA.7+7,     false",
                "3.0.0+21,         true",

                "45.3.9,           true",
                "45.3.9-M9.77,     false",
                "45.3.9-M9.77+10,  false",
                "45.3.9+5,         true"
            )
            fun minorWildcard(version: String, match: Boolean) {

                // [2.0.0 .. *)
                val range = ">=  2.*"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,            false",
                "0.0.0+23b,        false",
                "0.0.0-2.GA,       false",
                "0.0.0-2.GA+5,     false",

                "0.0.6,            false",
                "0.0.6-5.alpha,    false",
                "0.0.6+0fe,        false",
                "0.0.6-5.alpha+00, false",

                "0.3.12,           false",
                "0.3.12-beta.9,    false",
                "0.3.12+-1,        false",
                "0.3.12-beta.9+07, false",

                "6.2.4,            false",
                "6.2.4-5.GA,       false",
                "6.2.4+63,         false",
                "6.2.4-5.GA+42,    false",

                "7.3.99,           false",
                "7.3.99-5.GA,      false",
                "7.3.99+63,        false",
                "7.3.99-5.GA+42,   false",

                "7.4.0,            true",
                "7.4.0-FOO.1,      false",
                "7.4.0+92,         true",
                "7.4.0-FOO.1+2,    false",

                "7.4.7,            true",
                "7.4.7-FOO.1,      false",
                "7.4.7+92,         true",
                "7.4.7-FOO.1+2,    false",

                "7.5.0,            true",
                "7.5.0-FOO.1,      false",
                "7.5.0+92,         true",
                "7.5.0-FOO.1+2,    false",

                "7.5.1,            true",
                "7.6.0,            true",
                "8.0.0,            true"
            )
            fun patchWildcard(version: String, match: Boolean) {

                // [7.4.0 .. *)
                val range = ">= 7.4.X"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             false",
                "0.0.0+f00bar,      false",
                "0.0.0-M6,          false",
                "0.0.0-M6+5,        false",

                "0.0.99,            false",
                "0.0.99-5.beta,     false",
                "0.0.99+foobar,     false",
                "0.0.99-5.beta+foo, false",

                "0.14.7,            false",
                "0.14.7-M5.2,       false",
                "0.14.7+63,         false",
                "0.14.7-M5.2+42,    false",

                "11.2.0,            false",
                "11.2.0-M9.0,       false",
                "11.2.0+63,         false",
                "11.2.0-M9.0+42,    false",

                "12.0.0,            true",
                "12.0.0-FOO.1,      false",
                "12.0.0+92,         true",
                "12.0.0-FOO.1+2,    false",

                "12.0.2,            true",
                "12.0.2-FOO.1,      false",
                "12.0.2+92,         true",
                "12.0.2-FOO.1+2,    false",

                "12.2.0,            true",
                "12.2.0-FOO.1,      false",
                "12.2.0+92,         true",
                "12.2.0-FOO.1+2,    false",

                "13.0.0,            true",
                "13.0.0-FOO.1,      false",
                "13.0.0+92,         true",
                "13.0.0-FOO.1+2,    false",

                "17.3.4,            true",
                "17.3.4-FOO.1,      false",
                "17.3.4+92,         true",
                "17.3.4-FOO.1+2,    false"
            )
            fun minorWildcardPatchWildcard(version: String, match: Boolean) {

                // [12.0.0 .. *)
                val range = ">= 12.*.x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,             false",
                "0.0.0+yolo,        false",
                "0.0.0-RC.4,        false",
                "0.0.0-RC.4+yolo,   false",

                "0.0.42,            false",
                "0.0.42-5.beta,     false",
                "0.0.42+foobar,     false",
                "0.0.42-5.beta+foo, false",

                "0.42.0,            false",
                "0.42.0-beta.2,     false",
                "0.42.0+63,         false",
                "0.42.0-beta.2+42,  false",

                "41.99.99,          false",
                "41.99.99-42,       false",
                "41.99.99+63,       false",
                "41.99.99-42+42,    false",

                "42.0.0,            true",
                "42.0.0-42,         false",
                "42.0.0+92,         true",
                "42.0.0-42+42,      false",

                "42.0.3,            true",
                "42.0.3-42,         false",
                "42.0.3+92,         true",
                "42.0.3-42+42,      false",

                "42.4.0,            true",
                "42.4.0-42,         false",
                "42.4.0+92,         true",
                "42.4.0-42+42,      false",

                "43.0.0,            true",
                "43.0.0-42,         false",
                "43.0.0+92,         true",
                "43.0.0-42+42,      false",

                "122.2.7,           true",
                "122.2.7-42,        false",
                "122.2.7+92,        true",
                "122.2.7-42+42,     false"
            )
            fun partialMajor(version: String, match: Boolean) {

                // [42.0.0 .. *)
                val range = ">= 42"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }

            @ParameterizedTest
            @CsvSource(
                "0.0.0,               false",
                "0.0.0+23b,           false",
                "0.0.0-2.GA,          false",
                "0.0.0-2.GA+5,        false",

                "0.0.1337,            false",
                "0.0.1337-5.alpha,    false",
                "0.0.1337+0fe,        false",
                "0.0.1337-5.alpha+00, false",

                "0.133.7,             false",
                "0.133.7-R.9,         false",
                "0.133.7+-1,          false",
                "0.133.7-R.9+07,      false",

                "33.7.1,              false",
                "33.7.1-5.GA,         false",
                "33.7.1+63,           false",
                "33.7.1-5.GA+42,      false",

                "133.7.0,             true",
                "133.7.0-FOO.1,       false",
                "133.7.0+92,          true",
                "133.7.0-FOO.1+2,     false",

                "133.7.13,            true",
                "133.7.13-FOO.1,      false",
                "133.7.13+92,         true",
                "133.7.13-FOO.1+2,    false",

                "133.8.0,             true",
                "133.8.0-FOO.1,       false",
                "133.8.0+92,          true",
                "133.8.0-FOO.1+2,     false",

                "1001.0.1,            true",
                "1001.0.1-FOO.1,      false",
                "1001.0.1+92,         true",
                "1001.0.1-FOO.1+2,    false"
            )
            fun partialMajorMinor(version: String, match: Boolean) {

                // [133.7.0 .. *)
                val range = ">=133.7"

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
                val range = ">= *"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }
        }
    }
}