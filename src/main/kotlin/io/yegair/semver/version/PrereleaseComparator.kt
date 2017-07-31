package io.yegair.semver.version

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
 * [Comparator] for comparing [Prerelease]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
object PrereleaseComparator : Comparator<Prerelease> {

    /**
     * Result of [compare] that is returned when the left prerelease is lower than
     * the right prerelease.
     */
    const val Lower = -4

    /**
     * Result of [compare] that is returned when the prefix of the left prerelease
     * is lower than the prefix of the right prerelease.
     */
    const val LowerPrefix = -3

    /**
     * Result of [compare] that is returned when the version of the left prerelease
     * is lower than the version of the right prerelease.
     */
    const val LowerVersion = -2

    /**
     * Result of [compare] that is returned when the suffix of the left prerelease
     * is lower than the suffix of the right prerelease.
     */
    const val LowerSuffix = -1

    /**
     * Result of [compare] that is returned when the left prerelease compares equal
     * to the right prerelease.
     */
    const val Equal = 0

    /**
     * Result of [compare] that is returned when the suffix of the left prerelease
     * is greater than the suffix of the right prerelease.
     */
    const val GreaterSuffix = 1

    /**
     * Result of [compare] that is returned when the version of the left prerelease
     * is greater than the version of the right prerelease.
     */
    const val GreaterVersion = 2

    /**
     * Result of [compare] that is returned when the prefix of the left prerelease
     * is greater than the prefix of the right prerelease.
     */
    const val GreaterPrefix = 3

    /**
     * Result of [compare] that is returned when the left prerelease is greater than
     * the right prerelease.
     */
    const val Greater = 4

    override fun compare(left: Prerelease?, right: Prerelease?): Int {
        return when {
            left === right -> Equal
            left == null -> Lower
            right == null -> Greater
            left == NoPrerelease -> when (right) {
                NoPrerelease -> Equal
                else -> Greater
            }
            right == NoPrerelease -> Lower
            else -> safeCompare(left, right)
        }
    }

    private fun safeCompare(left: Prerelease, other: Prerelease): Int {
        return when {
            left.prefix < other.prefix -> LowerPrefix
            left.prefix > other.prefix -> GreaterPrefix
            else -> when {
                left.version < other.version -> LowerVersion
                left.version > other.version -> GreaterVersion
                else -> when {
                    left.suffix < other.suffix -> LowerSuffix
                    left.suffix > other.suffix -> GreaterSuffix
                    else -> Equal
                }
            }
        }
    }
}