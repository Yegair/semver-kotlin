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
 * Name part of a [Prerelease]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
sealed class PrereleaseName: Comparable<PrereleaseName> {

    abstract val value: String

    override fun compareTo(other: PrereleaseName): Int {
        return value.compareTo(other.value)
//        return when (other) {
//            is StringPrereleaseName -> value.compareTo(other.value)
//            is NoPrereleaseName -> -1
//        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is PrereleaseName -> false
            else -> value == other.value
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }

    companion object {

        fun of(value: String?) : PrereleaseName {

            val normalized = value?.trim() ?: ""

            return when (normalized) {
                "" -> NoPrereleaseName
                else -> StringPrereleaseName(normalized)
            }
        }
    }
}

private class StringPrereleaseName(override val value: String) : PrereleaseName() {

//    override fun compareTo(other: PrereleaseName): Int {
//        return when (other) {
//            is StringPrereleaseName -> value.compareTo(other.value)
//            is NoPrereleaseName -> -1
//        }
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return when {
//            other === this -> true
//            other !is StringPrereleaseName -> false
//            else -> value == other.value
//        }
//    }
//
//    override fun hashCode(): Int {
//        return value.hashCode()
//    }
//
//    override fun toString(): String {
//        return value
//    }
}

object NoPrereleaseName : PrereleaseName() {

    override val value = ""

//    override fun compareTo(other: PrereleaseName): Int {
//        return when (other) {
//            is NoPrereleaseName -> 0
//            else -> -other.compareTo(this)
//        }
//    }
//
//    override fun toString(): String {
//        return ""
//    }
}