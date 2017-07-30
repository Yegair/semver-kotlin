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
 *
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class PrimitiveRange(operator: Operator,
                              version: Version) : Range {

    private val comparator: RangeComparator = when (operator) {
        Operator.LT -> LtRangeComparator(version)
        Operator.LTE -> LteRangeComparator(version)
        Operator.EQ -> EqRangeComparator(version)
        Operator.GTE -> GteRangeComparator(version)
        Operator.GT -> GtRangeComparator(version)
    }

    enum class Operator {
        LT,
        LTE,
        GT,
        GTE,
        EQ
    }

    override fun satisfiedBy(version: Version): Boolean {

        return comparator.compare(version) == RangeComparator.Result.Satisfied

//        val cmp = VersionComparator.compare(version, this.version)
//
//        return when (operator) {
//            Operator.LT -> when (cmp) {
//                VersionComparator.Lower -> when (version.prerelease) {
//                    NoPrerelease -> true
//                    else -> false
//                }
//                VersionComparator.LowerPrerelease -> when (this.version.prerelease) {
//                    NoPrerelease -> false
//                    else -> true
//                }
//                VersionComparator.LowerBuild -> true
//                else -> false
//            }
//            Operator.LTE -> version <= this.version
//            Operator.GT -> version > this.version
//            Operator.GTE -> version >= this.version
//            Operator.EQ -> version.compareTo(this.version) == 0
//        }
    }

    override fun gtr(version: Version): Boolean {
        return comparator.compare(version) == RangeComparator.Result.Greater
    }

    override fun ltr(version: Version): Boolean {
        return comparator.compare(version) == RangeComparator.Result.Lower
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is PrimitiveRange -> false
            else -> comparator == other.comparator
        }
    }

    override fun hashCode(): Int {
        return comparator.hashCode()
    }

    override fun toString(): String {
        return comparator.toString()
    }
}