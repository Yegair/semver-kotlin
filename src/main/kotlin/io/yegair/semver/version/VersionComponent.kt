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
 * Represents one component of a [SemanticVersion]. Can be one of [VersionNumber], [Wildcard]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
sealed class VersionComponent : Comparable<VersionComponent> {

    abstract operator fun plus(operand: Int): VersionComponent
}

/**
 * Represents an actual version number.
 */
sealed class VersionNumber : VersionComponent() {

    /**
     * The numeric value of this version number.
     */
    abstract val value: Int

    override fun plus(operand: Int): VersionNumber = IntVersionNumber(value + operand)

    companion object {
        val Zero: VersionNumber = IntVersionNumber(0)
        val One: VersionNumber = IntVersionNumber(1)

        /**
         * Creates a new version number with the given value.
         */
        fun of(value: Int?): VersionNumber {
            return when(value) {
                null -> NoVersionNumber
                else -> IntVersionNumber(value)
            }
        }
    }
}

/**
 * Represents a version number with an integer value greater than or equal to `0`.
 */
class IntVersionNumber(override val value: Int) : VersionNumber() {

    init {
        if (value < 0)
            throw IllegalArgumentException("version number must be greater or equal to [0] but is [$value]")
    }

    override fun compareTo(other: VersionComponent): Int {
        return when (other) {
            is Wildcard -> 0
            is NoVersionNumber -> 1
            is IntVersionNumber -> value.compareTo(other.value)
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is IntVersionNumber -> false
            else -> value == other.value
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

/**
 * Represents a version number with no value.
 */
object NoVersionNumber : VersionNumber() {

    override val value: Int = 0

    override fun compareTo(other: VersionComponent): Int {
        return when (other) {
            is Wildcard -> 0
            is NoVersionNumber -> 0
            is IntVersionNumber -> -1
        }
    }

    override fun toString(): String {
        return ""
    }
}

object Wildcard : VersionComponent() {

    override fun plus(operand: Int): VersionComponent = Wildcard

    override fun compareTo(other: VersionComponent) = 0

    override fun toString(): String {
        return "*"
    }
}