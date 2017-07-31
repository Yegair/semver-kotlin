package io.yegair.semver.version

import io.yegair.semver.antlr.ANTLRPrereleaseParser
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
 * Represents the prerelease identifier of a semantic version.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
sealed class Prerelease : Comparable<Prerelease> {

    abstract val prefix: PrereleaseName
    abstract val version: PrereleaseVersion
    abstract val suffix: PrereleaseName

    override fun compareTo(other: Prerelease): Int {
        return PrereleaseComparator.compare(this, other)

//        return when {
//            other === this -> 0
//            else -> {
//                val prefixCmp = prefix.compareTo(other.prefix)
//                if (prefixCmp == 0) {
//                    val versionCmp = version.compareTo(other.version)
//                    if (versionCmp == 0) {
//                        suffix.compareTo(other.suffix)
//                    } else {
//                        versionCmp
//                    }
//                } else {
//                    prefixCmp
//                }
//            }
//        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is Prerelease -> false
            prefix != other.prefix -> false
            version != other.version -> false
            suffix != other.suffix -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(prefix, suffix, version)
    }

    companion object {

        fun of(prefix: String? = null,
               version: Int? = null,
               suffix: String? = null): Prerelease {

            val pre = PrereleaseName.of(prefix)
            val ver = PrereleaseVersion.of(version)
            val suf = PrereleaseName.of(suffix)

            if (pre is NoPrereleaseName && suf is NoPrereleaseName && ver is NoPrereleaseVersion) {
                return NoPrerelease
            } else {
                return PrereleaseIdentifier(pre, suf, ver)
            }
        }

        fun parse(expression: String?): Prerelease {

            val normalized = expression?.trim() ?: ""

            return when (normalized) {
                "" -> NoPrerelease
                else -> ANTLRPrereleaseParser.parse(normalized)
            }
        }
    }
}

class PrereleaseIdentifier(override val prefix: PrereleaseName = NoPrereleaseName,
                           override val suffix: PrereleaseName = NoPrereleaseName,
                           override val version: PrereleaseVersion = NoPrereleaseVersion) : Prerelease() {


    override fun toString(): String {
        return listOf(prefix.toString(), version.toString(), suffix.toString())
            .filter { !it.isEmpty() }
            .joinToString(".")
    }
}

object NoPrerelease : Prerelease() {

    override val prefix = NoPrereleaseName
    override val version = NoPrereleaseVersion
    override val suffix = NoPrereleaseName

    override fun toString(): String {
        return ""
    }
}

fun String.asPrerelease(): Prerelease = Prerelease.parse(this)