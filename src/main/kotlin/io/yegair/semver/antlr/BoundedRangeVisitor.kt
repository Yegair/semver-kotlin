package io.yegair.semver.antlr

import io.yegair.semver.range.BoundedRange

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
 * ANTLR visitor that creates an instance of [BoundedRange]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal object BoundedRangeVisitor : VisitorSupport<BoundedRange>() {

    override fun visitBoundedRange(ctx: BoundedRangeCtx): BoundedRange {

        val lower = ctx.lower?.accept(WildcardVersionVisitor)
            ?: throw IllegalStateException("lower bound [wildcardVersion] must be present")

        val upper = ctx.upper?.accept(WildcardVersionVisitor)
            ?: throw IllegalStateException("upper bound [wildcardVersion] must be present")


        return BoundedRange(lower = lower, upper = upper)
    }
}