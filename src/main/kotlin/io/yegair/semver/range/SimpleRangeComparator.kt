package io.yegair.semver.range

import io.yegair.semver.range.RangeComparator.Result
import io.yegair.semver.range.RangeComparator.Result.*
import io.yegair.semver.version.NoPrerelease
import io.yegair.semver.version.Version
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
 * Compares a [Version] to the bounds of a [SimpleRange].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 *
 * @param lower The min inclusive lower bound.
 * @param upper The max exclusive upper bound.
 */
internal class SimpleRangeComparator(private val lower: Version,
                                     private val upper: Version): RangeComparator {

    override fun compare(version: Version): Result {

        val lowerCmp = compareLower(version)

        return when(lowerCmp) {
            Greater -> {
                val upperCmp = compareUpper(version)
                when (upperCmp) {
                    Lower -> Satisfied
                    else -> upperCmp
                }
            }
            else -> lowerCmp
        }
    }

    private fun compareLower(version: Version): Result {

        if (lower.prerelease == NoPrerelease) {

            if (version.prerelease == NoPrerelease) {
                val cmp = version.compareTo(lower)
                return when {
                    cmp < 0 -> Lower
                    cmp > 0 -> Greater
                    else -> Satisfied
                }
            } else {
                return Excluded
            }

        } else {
            if (version.prerelease == NoPrerelease) {
                val cmp = version.compareTo(lower)
                return when {
                    cmp < 0 -> Lower
                    cmp > 0 -> Greater
                    else -> Satisfied
                }
            } else {
                if (version.release() == lower.release()) {
                    val cmp = version.prerelease.compareTo(lower.prerelease)
                    return when {
                        cmp < 0 -> Lower
                        cmp > 0 -> Greater
                        else -> Satisfied
                    }
                } else {
                    return Excluded
                }
            }
        }
    }

    private fun compareUpper(version: Version): Result {

        val cmp = version.release().compareTo(upper)

        return when {
            cmp < 0 -> Lower
            else -> Greater
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is SimpleRangeComparator -> false
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