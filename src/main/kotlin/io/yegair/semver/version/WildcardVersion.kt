package io.yegair.semver.version

import io.yegair.semver.version.VersionNumber.Companion.One
import io.yegair.semver.version.VersionNumber.Companion.Zero
import io.yegair.semver.antlr.ANTLRWildcardVersionParser

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
 * [Version] implementation that can include wildcards.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal class WildcardVersion(override val major: VersionNumber = NoVersionNumber,
                               override val minor: VersionComponent = NoVersionNumber,
                               override val patch: VersionComponent = NoVersionNumber) : Version() {

    constructor(major: Int) :
        this(
            major = VersionNumber.of(major),
            minor = NoVersionNumber,
            patch = NoVersionNumber
        )

    constructor(major: Int,
                minor: Wildcard) :
        this(
            major = VersionNumber.of(major),
            minor = minor,
            patch = NoVersionNumber
        )

    constructor(major: Int,
                minor: Int) :
        this(
            major = VersionNumber.of(major),
            minor = VersionNumber.of(minor),
            patch = NoVersionNumber
        )

    constructor(major: Int,
                minor: Int,
                patch: Wildcard) :
        this(
            major = VersionNumber.of(major),
            minor = VersionNumber.of(minor),
            patch = patch
        )

    init {
        if (patch is IntVersionNumber) {
            throw IllegalArgumentException("[patch] may not be a concrete number for wildcard versions")
        }
    }

    override val prerelease: Prerelease = NoPrerelease
    override val build: Build = NoBuild

    override fun nextMajor(): Version {
        return SemanticVersion(
            major = major + 1,
            minor = when (minor) {
                is NoVersionNumber -> NoVersionNumber
                else -> Zero
            },
            patch = when (patch) {
                is NoVersionNumber -> NoVersionNumber
                else -> Zero
            }
        )
    }

    override fun nextMinor(): Version {
        return SemanticVersion(
            major = when (major) {
                is NoVersionNumber -> Zero
                else -> major
            },
            minor = when (minor) {
                is Wildcard -> One
                is NoVersionNumber -> One
                is VersionNumber -> minor + 1
            },
            patch = when (patch) {
                is NoVersionNumber -> NoVersionNumber
                else -> Zero
            }
        )
    }

    override fun nextPatch() = this

    override fun nextBreakingChange() =
        when (major) {
            Zero -> when (minor) {
                Wildcard -> nextMajor()
                NoVersionNumber -> nextMajor()
                else -> nextMinor()
            }
            else -> nextMajor()
        }

    override fun release() = this

    override fun floor(): Version {
        return SemanticVersion(
            major = major,
            minor = when (minor) {
                is Wildcard -> Zero
                is NoVersionNumber -> Zero
                is VersionNumber -> minor
            },
            patch = when (patch) {
                is Wildcard -> Zero
                is NoVersionNumber -> Zero
                is VersionNumber -> patch
            }
        )
    }

    override fun ceil(): Version {
        return SemanticVersion(
            major = when (minor) {
                is Wildcard -> major + 1
                is NoVersionNumber -> major + 1
                is IntVersionNumber -> major
            },
            minor = when (minor) {
                is Wildcard -> Zero
                is NoVersionNumber -> Zero
                is IntVersionNumber -> minor + 1
            },
            patch = Zero
        )
    }

    companion object {

        internal fun parse(expression: String): Version {
            return ANTLRWildcardVersionParser.parse(expression)
        }
    }
}