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
 * when visiting a [VersionRangeParser.SimpleRangeContext]
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
object SimpleRangeVisitor : VersionRangeBaseVisitor<Range>() {

    override fun visitSimpleRange(ctx: VersionRangeParser.SimpleRangeContext?): Range {

        if (ctx == null) {
            throw IllegalStateException("[PrimitiveRangeContext] must not be null")
        }

        val primitiveRangeCtx = ctx.primitiveRange()
        if (primitiveRangeCtx != null) {
            return primitiveRangeCtx.accept(PrimitiveRangeVisitor)
        }

        val plainRangeCtx = ctx.plainRange()
        if (plainRangeCtx != null) {
            return plainRangeCtx.accept(PlainRangeVisitor)
        }

        val tildeRangeCtx = ctx.tildeRange()
        if (tildeRangeCtx != null) {
            return tildeRangeCtx.accept(TildeRangeVisitor)
        }

        return ctx.caretRange().accept(CaretRangeVisitor)
    }

    override fun visitCaretRange(ctx: VersionRangeParser.CaretRangeContext?): Range {

        if (ctx == null) {
            throw IllegalStateException("[CaretRangeContext] must not be null")
        }

        val fullVersionCtx = ctx.fullVersion() ?: throw IllegalStateException("[fullVersion] must be present")

        val version = fullVersionCtx.accept(FullVersionVisitor)
        return CaretRange(version = version)
    }
}