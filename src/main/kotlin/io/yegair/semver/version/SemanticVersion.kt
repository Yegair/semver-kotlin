package io.yegair.semver.version

import io.yegair.semver.version.VersionNumber.Companion.Zero

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
internal class SemanticVersion(override val major: VersionNumber = NoVersionNumber,
                               override val minor: VersionNumber = NoVersionNumber,
                               override val patch: VersionNumber = NoVersionNumber,
                               override val prerelease: Prerelease = NoPrerelease,
                               override val build: Build = NoBuild) : Version() {

    constructor(major: Int? = null,
                minor: Int? = null,
                patch: Int? = null,
                prerelease: String? = null,
                build: String? = null) :
        this(
            major = VersionNumber.of(major),
            minor = VersionNumber.of(minor),
            patch = VersionNumber.of(patch),
            prerelease = Prerelease.parse(prerelease),
            build = Build.parse(build)
        )

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
            minor = minor + 1,
            patch = when (patch) {
                is NoVersionNumber -> NoVersionNumber
                else -> Zero
            }
        )
    }

    override fun nextPatch(): Version {
        return SemanticVersion(
            major = when (major) {
                is NoVersionNumber -> Zero
                else -> major
            },
            minor = when (minor) {
                is NoVersionNumber -> Zero
                else -> minor
            },
            patch = when (prerelease) {
                NoPrerelease -> patch + 1
                else -> when (patch) {
                    is NoVersionNumber -> Zero
                    else -> patch
                }
            }
        )
    }

    /**
     * Creates a new version with version numbers that semantically refer to the next breaking change.
     * ```
     * "1.2.7".asVersion().nextBreakingChange() // "2.0.0"
     * "0.4.2".asVersion().nextBreakingChange() // "0.5.0"
     * "0.0.3".asVersion().nextBreakingChange() // "0.1.0"
     * "2.9.1-beta.3".asVersion().nextBreakingChange() // "3.0.0"
     * ```
     */
    override fun nextBreakingChange() =
        when {
            major > Zero ->
                SemanticVersion(
                    major = major + 1,
                    minor = Zero,
                    patch = Zero
                )
            minor > Zero ->
                SemanticVersion(
                    major = Zero,
                    minor = minor + 1,
                    patch = Zero
                )
            else ->
                SemanticVersion(
                    major = Zero,
                    minor = Zero,
                    patch = patch + 1
                )
        }

    override fun release() =
        SemanticVersion(
            major = major,
            minor = minor,
            patch = patch,
            prerelease = NoPrerelease,
            build = NoBuild
        )

    override fun floor() =
        SemanticVersion(
            major = when (major) {
                NoVersionNumber -> Zero
                else -> major
            },
            minor = when (minor) {
                NoVersionNumber -> Zero
                else -> minor
            },
            patch = when (patch) {
                NoVersionNumber -> Zero
                else -> patch
            },
            prerelease = prerelease,
            build = NoBuild
        )

    override fun ceil() =
        SemanticVersion(
            major = when (minor) {
                NoVersionNumber -> major + 1
                else -> major
            },
            minor = when (minor) {
                NoVersionNumber -> Zero
                else -> minor + 1
            },
            patch = Zero,
            prerelease = NoPrerelease,
            build = NoBuild
        )
}