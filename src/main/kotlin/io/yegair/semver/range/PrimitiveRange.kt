package io.yegair.semver.range

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
 *
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class PrimitiveRange(private val operator: Operator,
                              private val version: Version) : Range {

    enum class Operator constructor(private val symbol: String) {

        LT("<"),
        LTE("<="),
        GT(">"),
        GTE(">="),
        EQ("=");

        override fun toString(): String {
            return symbol
        }
    }

    override fun satisfiedBy(version: Version): Boolean {
        return when (operator) {
            Operator.LT -> version < this.version
            Operator.LTE -> version <= this.version
            Operator.GT -> version > this.version
            Operator.GTE -> version >= this.version
            Operator.EQ -> version.compareTo(this.version) == 0
        }
    }

    override fun gtr(version: Version): Boolean {
        return when (operator) {
            Operator.LT -> version >= this.version
            Operator.LTE -> version > this.version
            Operator.GT -> false
            Operator.GTE -> false
            Operator.EQ -> version > this.version
        }
    }

    override fun ltr(version: Version): Boolean {
        return when (operator) {
            Operator.LT -> false
            Operator.LTE -> false
            Operator.GT -> version <= this.version
            Operator.GTE -> version < this.version
            Operator.EQ -> version < this.version
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is PrimitiveRange -> false
            operator != other.operator -> false
            version != other.version -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(operator, version)
    }

    override fun toString(): String {
        return "$operator$version"
    }
}