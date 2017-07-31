package io.yegair.semver.range

import io.yegair.semver.range.RangeComparator.Result
import io.yegair.semver.version.NoPrerelease
import io.yegair.semver.version.Version
import io.yegair.semver.version.VersionComparator

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
 * [RangeComparator] that compares a given [Version] to it's inclusive [lower] bound.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 *
 * @param lower Inclusive lower bound versions are compared to.
 */
internal class GteRangeComparator(private val lower: Version) : RangeComparator {

    override fun compare(version: Version): Result {

        val cmp = VersionComparator.compare(version, lower)

        return when (cmp) {
            VersionComparator.Greater -> when (version.prerelease) {
                NoPrerelease -> Result.Satisfied
                else -> Result.Excluded
            }
            VersionComparator.GreaterPrerelease -> when (lower.prerelease) {
                NoPrerelease -> Result.Excluded
                else -> Result.Satisfied
            }
            VersionComparator.Equal -> Result.Satisfied
            VersionComparator.GreaterBuild -> Result.Satisfied
            else -> Result.Lower
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is GteRangeComparator -> false
            else -> lower == other.lower
        }
    }

    override fun hashCode(): Int {
        return lower.hashCode()
    }

    override fun toString(): String {
        return "[$lower .. *)"
    }
}