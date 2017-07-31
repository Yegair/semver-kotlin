package io.yegair.semver.range

import io.yegair.semver.range.PrimitiveRange.Operator.*
import io.yegair.semver.version.*
import org.junit.jupiter.api.Assertions.*
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
                val range = "< x"

                assertEquals(
                    match,
                    range.asRange().satisfiedBy(version.asVersion()),
                    "'$version' ${if (match) "should" else "should not"} match range '$range'"
                )
            }
        }
    }
}