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
 * Represents the build identifier of a semantic version.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
sealed class Build : Comparable<Build> {

    companion object {

        fun parse(value: String?): Build {
            val normalized = value?.trim() ?: ""
            return when (normalized.length) {
                0 -> NoBuild
                else -> BuildIdentifier(normalized)
            }
        }
    }
}

class BuildIdentifier(private val value: String) : Build() {

    override fun compareTo(other: Build): Int {
        return when (other) {
            is BuildIdentifier -> value.compareTo(other.value)
            is NoBuild -> 1
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is BuildIdentifier -> false
            else -> value == other.value
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}

object NoBuild : Build() {

    override fun compareTo(other: Build): Int {
        return when (other) {
            is NoBuild -> 0
            else -> -other.compareTo(this)
        }
    }

    override fun toString(): String {
        return ""
    }
}