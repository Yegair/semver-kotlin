package io.yegair.semver.range

import io.yegair.semver.range.RangeComparator.Result
import io.yegair.semver.version.*
import java.util.*

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
 * [RangeComparator] for [BoundedRange].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class BoundedRangeComparator(lower: Version,
                                      upper: Version) : RangeComparator {

    private val lower = when (lower) {
        is WildcardVersion -> lower.floor()
        else -> lower
    }

    private val upper = when (upper) {
        is WildcardVersion -> upper.ceil()
        else -> upper.next()
    }

    override fun compare(version: Version): Result {

        val lowerCmp = compareLower(version)

        return when (lowerCmp) {
            Result.Greater -> {
                val upperCmp = compareUpper(version)
                when (upperCmp) {
                    Result.Lower -> Result.Satisfied
                    Result.Excluded -> Result.Excluded
                    else -> Result.Greater
                }
            }
            else -> lowerCmp
        }
    }

    private fun compareLower(version: Version): Result {

        val cmp = VersionComparator.compare(version, lower)

        return when (cmp) {
            VersionComparator.Lower -> Result.Lower
            VersionComparator.LowerPrerelease -> Result.Lower
            VersionComparator.LowerBuild -> Result.Satisfied
            VersionComparator.Equal -> Result.Satisfied
            VersionComparator.GreaterBuild -> Result.Satisfied
            VersionComparator.GreaterPrerelease -> Result.Satisfied
            VersionComparator.Greater -> when (version.prerelease) {
                NoPrerelease -> Result.Greater
                else -> Result.Excluded
            }

            else -> throw IllegalStateException("unexpected [VersionComparator] result $cmp")
        }
    }

    private fun compareUpper(version: Version): Result {

        val cmp = VersionComparator.compare(version, upper)

        return when (cmp) {
            VersionComparator.Lower -> when (version.prerelease) {
                NoPrerelease -> Result.Lower
                else -> Result.Excluded
            }
            VersionComparator.LowerPrerelease -> Result.Lower
            VersionComparator.LowerBuild -> Result.Satisfied
            VersionComparator.Equal -> Result.Satisfied
            VersionComparator.GreaterBuild -> Result.Satisfied
            VersionComparator.GreaterPrerelease -> Result.Greater
            VersionComparator.Greater ->  Result.Greater

            else -> throw IllegalStateException("unexpected [VersionComparator] result $cmp")
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is BoundedRangeComparator -> false
            lower != other.lower -> false
            upper != other.upper -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(lower, upper)
    }

    override fun toString(): String {
        return "[$lower .. $upper)"
    }
}