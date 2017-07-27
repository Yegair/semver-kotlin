package io.yegair.semver.version.antlr

import io.yegair.semver.antlr.SemverLexer
import io.yegair.semver.antlr.SemverParser
import io.yegair.semver.antlr.FullVersionVisitor
import io.yegair.semver.version.Version
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token

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
 * Parses versions using ANTLR.
 *
 * @author Hauke Jaeger, hauke.jaeger@yegair.io
 */
internal object ANTLRVersionParser {

    fun parse(value: String): Version {
//        debug(value)
        val parser = parser(value)
        val context = parser.fullVersion()
        return context.accept(FullVersionVisitor)
    }

    private fun parser(expression: String) =
        SemverParser(tokenStream(expression))

    private fun tokenStream(expression: String) =
        CommonTokenStream(lexer(expression))

    private fun lexer(expression: String) =
        SemverLexer(charStream(expression))

    private fun charStream(expression: String) =
        CharStreams.fromString(expression)

    private fun debug(expression: String) {
        val lexer = lexer(expression)

        var token: Token = lexer.nextToken()

        while (token.type != Token.EOF) {
            println(token)
            token = lexer.nextToken()
        }
    }
}