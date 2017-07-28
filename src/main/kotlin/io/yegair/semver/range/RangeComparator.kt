package io.yegair.semver.range

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
 * Compares a [Version] to the bounds of a [Range].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal interface RangeComparator {

    enum class Result {

        /**
         * Result of [RangeComparator.compare] when the given [Version] is lower than the range.
         */
        Lower,

        /**
         * Result of [RangeComparator.compare] when the given [Version] is greater than the range.
         */
        Greater,

        /**
         * Result of [RangeComparator.compare] when the given [Version] is neither lower nor greater
         * than the range but nevertheless doesn't satisfy in the range.
         */
        Excluded,

        /**
         * Result of [RangeComparator.compare] when the given [Version] satisfies the bounds of the range.
         */
        Satisfied
    }

    /**
     * Compares the given [Version] to the bounds of a [Range].
     * Returns [Result.Satisfied] if the version satisfies the bounds of the range.
     * Returns [Result.Lower] if the version is lower than the range.
     * Returns [Result.Greater] if the version is greater than the range.
     * Returns [Result.Excluded] if the version is neither lower nor greater
     * than the range but nevertheless doesn't satisfy in the range.
     */
    fun compare(version: Version): Result
}