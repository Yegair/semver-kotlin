package io.yegair.semver.antlr

import io.yegair.semver.version.Prerelease
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

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
 * Parses prerelease expressions using ANTLR.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal object ANTLRPrereleaseParser {

    fun parse(expression: String): Prerelease {
        val parser = parser(expression)
        val context = parser.prerelease()
        return context.accept(PrereleaseVisitor)
    }

    private fun parser(expression: String) =
        SemverParser(tokenStream(expression))

    private fun tokenStream(expression: String) =
        CommonTokenStream(lexer(expression))

    private fun lexer(expression: String) =
        SemverLexer(charStream(expression))

    private fun charStream(expression: String) =
        CharStreams.fromString(expression)
}