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
 * The version part of a [Prerelease].
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
sealed class PrereleaseVersion : Comparable<PrereleaseVersion> {

    companion object {

        fun of (value: Int?): PrereleaseVersion {
            return when {
                value == null -> NoPrereleaseVersion
                value < 0 -> NoPrereleaseVersion
                else -> IntPrereleaseVersion(value)
            }
        }
    }
}

/**
 * [PrereleaseVersion] with an integer value
 */
class IntPrereleaseVersion(private val value: Int) : PrereleaseVersion() {

    override fun compareTo(other: PrereleaseVersion): Int {
        return when (other) {
            is IntPrereleaseVersion -> value.compareTo(other.value)
            is NoPrereleaseVersion -> 1
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is IntPrereleaseVersion -> false
            else -> value == other.value
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }
}

/**
 * [PrereleaseVersion] that is used as a placeholder when no prerelease version is present.
 */
object NoPrereleaseVersion : PrereleaseVersion() {

    override fun compareTo(other: PrereleaseVersion): Int {
        return when (other) {
            is NoPrereleaseVersion -> 0
            is IntPrereleaseVersion -> -other.compareTo(this)
        }
    }

    override fun toString(): String {
        return ""
    }
}