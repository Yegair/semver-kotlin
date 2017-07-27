package io.yegair.semver.range

import io.yegair.semver.version.SemanticVersion
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
 * A simple version range that is defined by an min inclusive lower bound and
 * an max exclusive upper bound.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
abstract internal class SimpleRange protected constructor(private val minInclusive: Version,
                                                          private val maxExclusive: Version) : Range {

    override fun satisfiedBy(version: Version): Boolean {
        return version >= minInclusive && version < maxExclusive
    }

    override fun gtr(version: Version): Boolean {
        return version >= maxExclusive
    }

    override fun ltr(version: Version): Boolean {
        return version < minInclusive
    }

    override fun toString(): String {
        return "[$minInclusive .. $maxExclusive)"
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is SimpleRange -> false
            minInclusive != other.minInclusive -> false
            maxExclusive != other.maxExclusive -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(minInclusive, maxExclusive)
    }
}