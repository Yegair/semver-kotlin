package io.yegair.semver.version

import io.yegair.semver.version.antlr.ANTLRVersionParser
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
 * Represents a single semantic version. For example `4.3.1-beta.7+48` where `4` is the major
 * component, `3` is the minor component, `1` is the patch component, `beta.7` is the prerelease
 * tag, `48` is the build.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
abstract class Version : Comparable<Version> {

    abstract val major: VersionComponent

    abstract val minor: VersionComponent

    abstract val patch: VersionComponent

    abstract val prerelease: Prerelease

    abstract val build: Build

    /**
     * Increments [major] by `1`, and adjusts [minor] and [patch] accordingly.
     *
     * ```
     * "3".asVersion().nextMajor() // "4"
     * "2.2".asVersion().nextMajor() // "3.0"
     * "1.2.7-beta.4+57".asVersion().nextMajor() // "2.0.0"
     * ```
     */
    abstract fun nextMajor(): Version

    /**
     * Increments [minor] by `1`, and adjusts [patch] accordingly.
     *
     * ```
     * "3".asVersion().nextMinor() // "3.1"
     * "2.2".asVersion().nextMinor() // "2.3"
     * "1.2.7-beta.4+57".asVersion().nextMinor() // "1.3.0"
     * ```
     */
    abstract fun nextMinor(): Version

    /**
     * Increments [patch] by `1`.
     *
     * ```
     * "3".asVersion().nextPatch() // "3.0.1"
     * "2.2".asVersion().nextPatch() // "2.2.1"
     * "1.2.7-beta.4+57".asVersion().nextPatch() // "1.2.8"
     * ```
     */
    abstract fun nextPatch(): Version

    /**
     * Creates a new version with version numbers that semantically refer to the next breaking change.
     *
     * ```
     * "1.2.7".asVersion().nextBreakingChange() // "2.0.0"
     * "0.4.2".asVersion().nextBreakingChange() // "0.5.0"
     * "0.0.3".asVersion().nextBreakingChange() // "0.0.4"
     * "2.9.1-beta.3".asVersion().nextBreakingChange() // "3.0.0"
     * ```
     */
    abstract fun nextBreakingChange(): Version

    /**
     * Resolves the base version of this version. For semantic versions the base is the version itself
     * without any build identifiers. For wildcard version the base is the version without any prerelease
     * and build with all wildcards replaced with `0`. Base versions are the lower inclusive bound of a
     * tilde range (e.g. `~3.2.x`).
     *
     * ```
     * "1.2.7".asVersion().floor()            // "1.2.7"
     * "0.4.2-beta.3+394".asVersion().floor() // "0.4.2-beta.3"
     * "0.x".asVersion().floor()              // "0.0.0"
     * "2.9.*-beta.3".asVersion().floor()     // "2.9.0"
     * ```
     */
    abstract fun floor(): Version

    /**
     *
     */
    abstract fun ceil(): Version

    override fun compareTo(other: Version): Int {
        return when {
            major < other.major -> -1
            major > other.major -> 1
            else -> when {
                minor < other.minor -> -1
                minor > other.minor -> 1
                else -> when {
                    patch < other.patch -> -1
                    patch > other.patch -> 1
                    else -> when {
                        prerelease < other.prerelease -> -1
                        prerelease > other.prerelease -> 1
                        else -> when {
                            build < other.build -> -1
                            build > other.build -> 1
                            else -> 0
                        }
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is Version -> false
            major != other.major -> false
            minor != other.minor -> false
            patch != other.patch -> false
            prerelease != other.prerelease -> false
            build != other.build -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(major, minor, patch, prerelease, build)
    }

    override fun toString(): String {

        val sb = StringBuilder()

        listOf(major, minor, patch)
            .filter { it !is NoVersionNumber }
            .joinTo(sb, ".")

        if (prerelease !is NoPrerelease) {
            sb.append("-").append(prerelease)
        }

        if (build !is NoBuild) {
            sb.append("+").append(build)
        }

        return sb.toString()
    }

    companion object {

        /**
         * Parses the given [String] to a semantic version if possible.
         *
         * @throws IllegalArgumentException If the given [String] is not a valid semantic version.
         */
        fun parse(value: String): Version {
            return ANTLRVersionParser.parse(value)
        }
    }
}

/**
 * Parses this [String] into a [Version] by the rules of semantic versioning.
 * This method delegates to [Version.parse].
 */
fun String.asVersion(): Version = Version.parse(this)