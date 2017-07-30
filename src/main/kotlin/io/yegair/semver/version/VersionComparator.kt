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
 * [Comparator] for comparing [Version]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
object VersionComparator : Comparator<Version> {

    /**
     * Result of [compare] that is returned when the left version is lower than the right version.
     */
    const val Lower = -3

    /**
     * Result of [compare] that is returned when the left version is equal to the right version but has
     * a lower prerelease.
     */
    const val LowerPrerelease = -2

    /**
     * Result of [compare] that is returned when the left version is equal to the right version but has
     * a lower build.
     */
    const val LowerBuild = -1

    /**
     * Result of [compare] that is returned when the left version is equal to the right version.
     */
    const val Equal = 0

    /**
     * Result of [compare] that is returned when the left version is equal to the right version but has
     * a greater build.
     */
    const val GreaterBuild = 1

    /**
     * Result of [compare] that is returned when the left version is equal to the right version but has
     * a greater prerelease.
     */
    const val GreaterPrerelease = 2

    /**
     * Result of [compare] that is returned when the left version is lower than the right version.
     */
    const val Greater = 3

    override fun compare(left: Version?, right: Version?): Int {
        return when {
            left === right -> Equal
            left == null -> Lower
            right == null -> Greater
//            left is AnyVersion -> Equal
//            right is AnyVersion -> Equal
            else -> safeCompare(left, right)
        }
    }

    private fun safeCompare(left: Version, other: Version): Int {
        return when {
            left.major < other.major -> Lower
            left.major > other.major -> Greater
            else -> when {
                left.minor < other.minor -> Lower
                left.minor > other.minor -> Greater
                else -> when {
                    left.patch < other.patch -> Lower
                    left.patch > other.patch -> Greater
                    else -> when {
                        left.prerelease < other.prerelease -> LowerPrerelease
                        left.prerelease > other.prerelease -> GreaterPrerelease
                        else -> when {
                            left.build < other.build -> LowerBuild
                            left.build > other.build -> GreaterBuild
                            else -> Equal
                        }
                    }
                }
            }
        }
    }
}