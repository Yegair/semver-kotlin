package io.yegair.semver.antlr

import io.yegair.semver.version.NoBuild
import io.yegair.semver.version.NoPrerelease
import io.yegair.semver.version.SemanticVersion
import io.yegair.semver.version.VersionNumber

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
 * ANTLR visitor that creates an instance of [SemanticVersion]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal object FullVersionVisitor : VisitorSupport<SemanticVersion>() {

    override fun visitFullVersion(ctx: FullVersionCtx): SemanticVersion {

        val major = ctx.major?.text?.toInt()
        val minor = ctx.minor?.text?.toInt()
        val patch = ctx.patch?.text?.toInt()

        val prerelease = ctx.prerelease()?.accept(PrereleaseVisitor)
        val build = ctx.build()?.accept(BuildVisitor)

        return SemanticVersion(
            major = VersionNumber.of(major),
            minor = VersionNumber.of(minor),
            patch = VersionNumber.of(patch),
            prerelease = prerelease ?: NoPrerelease,
            build = build ?: NoBuild
        )
    }
}