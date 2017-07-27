package io.yegair.semver.range

import io.yegair.semver.range.antlr.ANTLRRangeParser
import io.yegair.semver.version.Version

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
 * Represents a semantic version range expression.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
interface Range {

    /**
     * Determines whether the given version satisfies this version range.
     */
    fun satisfiedBy(version: Version): Boolean

    /**
     * Return `true` if version is greater than all the versions possible in the range.
     */
    fun gtr(version: Version): Boolean

    /**
     * Return `true` if version is less than all the versions possible in the range.
     */
    fun ltr(version: Version): Boolean

    companion object {

        /**
         * Parses the given expression into a [Range]
         */
        fun parse(expression: String): Range {
            return ANTLRRangeParser().parse(expression)
        }
    }
}

fun String.asRange(): Range = Range.parse(this)