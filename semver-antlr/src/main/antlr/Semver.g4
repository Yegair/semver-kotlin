grammar Semver;

compositeRange
    : singleRange (LOGICAL_OR singleRange)* EOF
    ;

singleRange
    : exactRange
    | boundedRange
    | simpleRange (WHITESPACE+ simpleRange)*
    ;

exactRange
    : fullVersion
    ;

boundedRange
    : lower=wildcardVersion WHITESPACE* HYPHEN WHITESPACE* upper=wildcardVersion
    ;

simpleRange
    : primitiveRange
    | plainRange
    | tildeRange
    | caretRange
    ;

primitiveRange
    : primitiveOperator WHITESPACE* wildcardVersion
    ;

primitiveOperator
    : LT
    | LTE
    | GT
    | GTE
    | EQ
    ;

plainRange
    : wildcard
    | wildcardVersion
    ;

tildeRange
    : TILDE wildcardVersion
    ;

caretRange
    : CARET wildcardVersion
    ;

versions
    : fullVersion (WHITESPACE* fullVersion)* EOF
    ;

fullVersion
    : versionPrefix? major=integer PERIOD minor=integer PERIOD patch=integer (HYPHEN prerelease)? (PLUS build)?
    ;

wildcardVersion
    : versionPrefix? major=integer (PERIOD minorWildcard=wildcard (PERIOD patchWildcard=wildcard)?)?
    | versionPrefix? major=integer (PERIOD minor=integer (PERIOD patchWildcard=wildcard)?)?
    | versionPrefix? major=integer (PERIOD minor=integer (PERIOD patch=integer (HYPHEN prerelease)? (PLUS build)?)?)?
    ;

versionPrefix
    : ((EQ | LETTER_V) | (EQ LETTER_V))
    ;

prerelease
    : prereleaseText=noInts
    | prereleaseVersion=integer (PERIOD suffix=noInts)?
    | prefix=identifiers PERIOD prereleaseVersion=integer (PERIOD suffix=noInts)?
    ;

build
    : identifiers
    ;

wildcard
    : LETTER_X
    | ASTERISK
    ;

identifiers
    : identifier (PERIOD identifier)*
    ;

identifier
    : integer
    | ((HYPHEN | DIGIT_0 | DIGIT_1_9) | letter) (HYPHEN | digit | letter)*
    ;

integer
    : DIGIT_0
    | DIGIT_1_9 digit*
    ;

noInts
    : noInt (PERIOD noInt)*
    ;

noInt
    : (HYPHEN | digit)* letter+ (HYPHEN | digit | letter)*
    ;

digit
    : DIGIT_0
    | DIGIT_1_9
    ;

letter
    : LETTER_A
    | LETTER_B
    | LETTER_C
    | LETTER_D
    | LETTER_E
    | LETTER_F
    | LETTER_G
    | LETTER_H
    | LETTER_I
    | LETTER_J
    | LETTER_K
    | LETTER_L
    | LETTER_M
    | LETTER_N
    | LETTER_O
    | LETTER_P
    | LETTER_Q
    | LETTER_R
    | LETTER_S
    | LETTER_T
    | LETTER_U
    | LETTER_V
    | LETTER_W
    | LETTER_X
    | LETTER_Y
    | LETTER_Z
    ;

ASTERISK
    : '*'
    ;

LOGICAL_OR
    : WHITESPACE* '||' WHITESPACE*
    ;

PERIOD
    : '.'
    ;

HYPHEN
    : '-'
    ;

PLUS
    : '+'
    ;

TILDE
    : '~'
    ;

CARET
    : '^'
    ;

LTE
    : '<='
    ;

GTE
    : '>='
    ;

LT
    : '<'
    ;

GT
    : '>'
    ;

EQ
    : '='
    ;

WHITESPACE
    : ' '
    | '\t'
    ;

DIGIT_0
    : '0'
    ;

DIGIT_1_9
    : '1'..'9'
    ;

LETTER_A: 'A' | 'a' ;
LETTER_B: 'B' | 'b' ;
LETTER_C: 'C' | 'c' ;
LETTER_D: 'D' | 'd' ;
LETTER_E: 'E' | 'e' ;
LETTER_F: 'F' | 'f' ;
LETTER_G: 'G' | 'g' ;
LETTER_H: 'H' | 'h' ;
LETTER_I: 'I' | 'i' ;
LETTER_J: 'J' | 'j' ;
LETTER_K: 'K' | 'k' ;
LETTER_L: 'L' | 'l' ;
LETTER_M: 'M' | 'm' ;
LETTER_N: 'N' | 'n' ;
LETTER_O: 'O' | 'o' ;
LETTER_P: 'P' | 'p' ;
LETTER_Q: 'Q' | 'q' ;
LETTER_R: 'R' | 'r' ;
LETTER_S: 'S' | 's' ;
LETTER_T: 'T' | 't' ;
LETTER_U: 'U' | 'u' ;
LETTER_V: 'V' | 'v' ;
LETTER_W: 'W' | 'w' ;
LETTER_X: 'X' | 'x' ;
LETTER_Y: 'Y' | 'y' ;
LETTER_Z: 'Z' | 'z' ;