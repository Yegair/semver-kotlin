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
 * A [Range] that matches exactly one [Version].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class ExactRange(private val version:Version): Range {

    override fun satisfiedBy(version: Version): Boolean {
        return this.version.compareTo(version) == 0
    }

    override fun gtr(version: Version): Boolean {
        return version > this.version
    }

    override fun ltr(version: Version): Boolean {
        return version < this.version
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is ExactRange -> false
            else -> version == other.version
        }
    }

    override fun hashCode(): Int {
        return version.hashCode()
    }

    override fun toString(): String {
        return version.toString()
    }
}