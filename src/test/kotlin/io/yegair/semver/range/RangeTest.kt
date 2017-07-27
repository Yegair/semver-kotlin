package io.yegair.semver.range

import io.yegair.semver.version.asVersion
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/*
 * MIT License
 *
 * Copyright (c) 2017 Hauke Jaeger http://yegair.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Unit test for [Range].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class RangeTest {

    @Nested
    inner class Gtr {

//        @ParameterizedTest
//        @CsvSource(
//            "~1.2.2, 1.3.0, false",
//            "~0.6.1-1, 0.7.1-1, false",
//            "1.0.0 - 2.0.0, 2.0.1, false",
//            "1.0.0, 1.0.1-beta1, false",
//            "1.0.0, 2.0.0, false",
//            "<=2.0.0, 2.1.1, false",
//            "<=2.0.0, 3.2.9, false",
//            "<2.0.0, 2.0.0, false",
//            "0.1.20 || 1.2.4, 1.2.5, false",
//            "2.x.x, 3.0.0, false",
//            "1.2.x, 1.3.0, false",
//            "1.2.x || 2.x, 3.0.0, false",
//            "2.*.*, 5.0.1, false",
//            "1.2.*, 1.3.3, false",
//            "1.2.* || 2.*, 4.0.0, false",
//            "2, 3.0.0, false",
//            "2.3, 2.4.2, false",
//            "~2.4, 2.5.0, false", // >=2.4.0 <2.5.0
//            "~2.4, 2.5.5, false",
//            "~>3.2.1, 3.3.0, false", // >=3.2.1 <3.3.0
//            "~1, 2.2.3, false", // >=1.0.0 <2.0.0
//            "~>1, 2.2.4, false",
//            "~> 1, 3.2.3, false",
//            "~1.0, 1.1.2, false", // >=1.0.0 <1.1.0
//            "~ 1.0, 1.1.0, false",
//            "<1.2, 1.2.0, false",
//            "< 1.2, 1.2.1, false",
//            "1, 2.0.0beta, true",
//            "~v0.5.4-pre, 0.6.0, false",
//            "~v0.5.4-pre, 0.6.1-pre, false",
//            "=0.7.x, 0.8.0, false",
//            "=0.7.x, 0.8.0-asdf, false",
//            "<0.7.x, 0.7.0, false",
//            "~1.2.2, 1.3.0, false",
//            "1.0.0 - 2.0.0, 2.2.3, false",
//            "1.0.0, 1.0.1, false",
//            "<=2.0.0, 3.0.0, false",
//            "<=2.0.0, 2.9999.9999, false",
//            "<=2.0.0, 2.2.9, false",
//            "<2.0.0, 2.9999.9999, false",
//            "<2.0.0, 2.2.9, false",
//            "2.x.x, 3.1.3, false",
//            "1.2.x, 1.3.3, false",
//            "1.2.x || 2.x, 3.1.3, false",
//            "2.*.*, 3.1.3, false",
//            "1.2.*, 1.3.3, false",
//            "1.2.* || 2.*, 3.1.3, false",
//            "2, 3.1.2, false",
//            "2.3, 2.4.1, false",
//            "~2.4, 2.5.0, false", // >=2.4.0 <2.5.0
//            "~>3.2.1, 3.3.2, false", // >=3.2.1 <3.3.0
//            "~1, 2.2.3, false", // >=1.0.0 <2.0.0
//            "~>1, 2.2.3, false",
//            "~1.0, 1.1.0, false", // >=1.0.0 <1.1.0
//            "<1, 1.0.0, false",
//            "1, 2.0.0beta, true",
//            "<1, 1.0.0beta, true",
//            "< 1, 1.0.0beta, true",
//            "=0.7.x, 0.8.2, false",
//            "<0.7.x, 0.7.2, false"
//        )
//        fun positive(rangeExpression: String, version: String, loose: Boolean) {
//
//            // given
//            val range = Range.parse(rangeExpression)
//
//            // when
//            val gtr = range.gtr(version.asVersion())
//
//            // then
//            assertTrue(gtr)
//        }

//        @ParameterizedTest
//        @CsvSource(
//            "~0.6.1-1, 0.6.1-1, false",
//            "1.0.0 - 2.0.0, 1.2.3, false",
//            "1.0.0 - 2.0.0, 0.9.9, false",
//            "1.0.0, 1.0.0, false",
//            ">=*, 0.2.4, false",
//            ", 1.0.0, true",
//            "*, 1.2.3, false",
//            "*, v1.2.3-foo, false",
//            ">=1.0.0, 1.0.0, false",
//            ">=1.0.0, 1.0.1, false",
//            ">=1.0.0, 1.1.0, false",
//            ">1.0.0, 1.0.1, false",
//            ">1.0.0, 1.1.0, false",
//            "<=2.0.0, 2.0.0, false",
//            "<=2.0.0, 1.9999.9999, false",
//            "<=2.0.0, 0.2.9, false",
//            "<2.0.0, 1.9999.9999, false",
//            "<2.0.0, 0.2.9, false",
//            ">= 1.0.0, 1.0.0, false",
//            ">=  1.0.0, 1.0.1, false",
//            ">=   1.0.0, 1.1.0, false",
//            "> 1.0.0, 1.0.1, false",
//            ">  1.0.0, 1.1.0, false",
//            "<=   2.0.0, 2.0.0, false",
//            "<= 2.0.0, 1.9999.9999, false",
//            "<=  2.0.0, 0.2.9, false",
//            "<    2.0.0, 1.9999.9999, false",
//            "<\t2.0.0, 0.2.9, false",
//            ">=0.1.97, v0.1.97, false",
//            ">=0.1.97, 0.1.97, false",
//            "0.1.20 || 1.2.4, 1.2.4, false",
//            "0.1.20 || >1.2.4, 1.2.4, false",
//            "0.1.20 || 1.2.4, 1.2.3, false",
//            "0.1.20 || 1.2.4, 0.1.20, false",
//            ">=0.2.3 || <0.0.1, 0.0.0, false",
//            ">=0.2.3 || <0.0.1, 0.2.3, false",
//            ">=0.2.3 || <0.0.1, 0.2.4, false",
//            "||, 1.3.4, false",
//            "2.x.x, 2.1.3, false",
//            "1.2.x, 1.2.3, false",
//            "1.2.x || 2.x, 2.1.3, false",
//            "1.2.x || 2.x, 1.2.3, false",
//            "x, 1.2.3, false",
//            "2.*.*, 2.1.3, false",
//            "1.2.*, 1.2.3, false",
//            "1.2.* || 2.*, 2.1.3, false",
//            "1.2.* || 2.*, 1.2.3, false",
//            "1.2.* || 2.*, 1.2.3, false",
//            "*, 1.2.3, false",
//            "2, 2.1.2, false",
//            "2.3, 2.3.1, false",
//            "~2.4, 2.4.0, false", // >=2.4.0 <2.5.0
//            "~2.4, 2.4.5, false",
//            "~>3.2.1, 3.2.2, false", // >=3.2.1 <3.3.0
//            "~1, 1.2.3, false", // >=1.0.0 <2.0.0
//            "~>1, 1.2.3, false",
//            "~> 1, 1.2.3, false",
//            "~1.0, 1.0.2, false", // >=1.0.0 <1.1.0
//            "~ 1.0, 1.0.2, false",
//            ">=1, 1.0.0, false",
//            ">= 1, 1.0.0, false",
//            "<1.2, 1.1.1, false",
//            "< 1.2, 1.1.1, false",
//            "1, 1.0.0beta, true",
//            "~v0.5.4-pre, 0.5.5, false",
//            "~v0.5.4-pre, 0.5.4, false",
//            "=0.7.x, 0.7.2, false",
//            ">=0.7.x, 0.7.2, false",
//            "=0.7.x, 0.7.0-asdf, false",
//            ">=0.7.x, 0.7.0-asdf, false",
//            "<=0.7.x, 0.6.2, false",
//            ">0.2.3 >0.2.4 <=0.2.5, 0.2.5, false",
//            ">=0.2.3 <=0.2.4, 0.2.4, false",
//            "1.0.0 - 2.0.0, 2.0.0, false",
//            "^1, 0.0.0-0, false",
//            "^3.0.0, 2.0.0, false",
//            "^1.0.0 || ~2.0.1, 2.0.0, false",
//            "^0.1.0 || ~3.0.1 || 5.0.0, 3.2.0, false",
//            "^0.1.0 || ~3.0.1 || 5.0.0, 1.0.0beta, true",
//            "^0.1.0 || ~3.0.1 || 5.0.0, 5.0.0-0, true",
//            "^0.1.0 || ~3.0.1 || >4 <=5.0.0, 3.5.0, false"
//        )
//        fun negative(rangeExpression: String, version: String, loose: Boolean) {
//
//            // given
//            val range = Range.parse(rangeExpression)
//
//            // when
//            val gtr = range.gtr(version.asVersion())
//
//            // then
//            assertFalse(gtr)
//        }
    }
}