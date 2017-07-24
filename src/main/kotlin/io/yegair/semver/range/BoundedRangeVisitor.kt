package io.yegair.semver.range

import io.yegair.semver.antlr.VersionRangeBaseVisitor
import io.yegair.semver.antlr.VersionRangeParser

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
 * ANTLR visitor that creates an instance of [Range]
 * when visiting a [VersionRangeParser.BoundedRangeContext]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal object BoundedRangeVisitor : VersionRangeBaseVisitor<Range>() {

    override fun visitBoundedRange(ctx: VersionRangeParser.BoundedRangeContext?): Range {

        if (ctx == null) {
            throw IllegalStateException("[BoundedRangeContext] must not be null")
        }

        val lowerCtx = ctx.fullVersion(0) ?: throw IllegalStateException("lower bound [fullVersion] must be present")
        val upperCtx = ctx.fullVersion(1) ?: throw IllegalStateException("upper bound [fullVersion] must be present")

        val lower = lowerCtx.accept(FullVersionVisitor)
        val upper = upperCtx.accept(FullVersionVisitor)

        return BoundedRange(lower = lower, upper = upper)
    }
}