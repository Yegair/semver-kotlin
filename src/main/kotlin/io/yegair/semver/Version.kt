package io.yegair.semver

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
 * Represents a semantic version.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
data class Version(private val major: Int? = null,
                   private val minor: Int? = null,
                   private val patch: Int? = null,
                   private val prerelease: String? = null) : Comparable<Version> {

    override fun compareTo(other: Version): Int {

        return when (cmp(major, other.major)) {
            1 -> 1
            -1 -> -1
            else -> when (cmp(minor, other.minor)) {
                1 -> 1
                -1 -> -1
                else -> when (cmp(patch, other.patch)) {
                    1 -> 1
                    -1 -> -1
                    else -> cmp(prerelease, other.prerelease)
                }
            }
        }
    }

    private fun <T : Comparable<T>>  cmp(left: T?, right: T?): Int {
        return when {
            left == null && right == null -> 0
            left == null -> -1
            right == null -> 1
            else -> with (left.compareTo(right)) {
                when  {
                    this < 0 -> -1
                    this > 0 -> 1
                    else -> 0
                }
            }
        }
    }

    companion object {

        /**
         * Delimiter for the components (major, minor, patch) of a semantic version.
         */
        const val DELIM: String = "."

        /**
         * Delimiter for the prerelease tag of a semantic version.
         */
        const val PRERELEASE_DELIM: String = "-"

        /**
         * Parses the given [String] to a semantic version if possible.
         *
         * @throws IllegalArgumentException If the given [String] is not a valid semantic version.
         */
        fun parse(value: String): Version {

            val (version, prerelease) = with(value.split(delimiters = PRERELEASE_DELIM, limit = 2)) {
                when (size) {
                    2 -> Pair(get(0), get(1))
                    else -> Pair(get(0), null)
                }
            }

            val (major, minor, patch) = with(version.split(DELIM)) {
                when {
                    size > 2 -> Triple(get(0), get(1), get(2))
                    size == 2 -> Triple(get(0), get(1), null)
                    else -> Triple(get(0), null, null)
                }
            }

            return Version(
                major = major.toInt(),
                minor = minor?.toInt(),
                patch = patch?.toInt(),
                prerelease = prerelease
            )
        }
    }
}

/**
 * Parses this [String] into a [Version] by the rules of semantic versioning.
 * This method delegates to [Version.parse].
 */
fun String.asVersion(): Version = Version.parse(this)