grammar VersionRange;

compositeRange
    : singleRange (LOGICAL_OR singleRange)* EOF
    ;

LOGICAL_OR
    : WHITESPACE* '||' WHITESPACE*
    ;

singleRange
    : hyphenRange
    | simpleRange (' ' simpleRange)*
    ;

hyphenRange
    : fullVersion ' - ' fullVersion
    ;

simpleRange
    : primitiveRange
    | plainRange
    | tildeRange
    | caretRange
    ;

plainRange
    : wildcardVersion
    ;

primitiveRange
    : ( '<' | '>' | '>=' | '<=' | '=' | ) wildcardVersion
    ;

tildeRange
    : '~' fullVersion
    ;

caretRange
    : '^' fullVersion
    ;

fullVersion
    : VERSION_PREFIX? major=versionNumber ('.' minor=versionNumber ('.' patch=versionNumber ('-' prerelease=identifier ('+' build=identifier)?)?)?)?
    ;

wildcardVersion
    : VERSION_PREFIX? major=versionNumber ('.' WILDCARD ('.' WILDCARD)?)?
    | VERSION_PREFIX? major=versionNumber ('.' minor=versionNumber ('.' WILDCARD)?)?
    | VERSION_PREFIX? major=versionNumber ('.' minor=versionNumber ('.' patch=versionNumber ('-' prerelease=identifier ('+' build=identifier)?)?)?)?
    ;


versionNumber
    : ZERO
    | NON_ZERO (ZERO | NON_ZERO)*
    ;

identifier
    : versionNumber
    | WORD
    ;

WORD
    : ('-' | NON_ZERO | LETTER) ('-' | ZERO | NON_ZERO | LETTER)+
    ;

LETTER
    : 'A'..'Z'
    | 'a'..'z'
    ;

VERSION_PREFIX
    : EQ
    | V
    | EQ V
    ;

EQ: '=' ;
V: 'V' | 'v' ;

//semanticVersion:
//    XR ( '.' XR ( '.' XR QUALIFIER? )? )? ;

//XR         : 'x' | 'X' | '*' | NR ;
//NR         : ( '0' | [1-9] [0-9]* ) ;
//QUALIFIER  : (( '-' PRE ) | ( '+' BUILD )) ;
//PRE        : PARTS ;
//BUILD      : PARTS ;
//PARTS      : PART ( '.' PART )* ;
//PART       : NR | ( '-' | [0-9] | [A-Z] | [a-z] )+ ;

WILDCARD       : '*' | 'X' | 'x' ;

//fragment LOWERCASE_LETTER : [a-z] ;
//fragment UPERCASE_LETTER  : [A-Z] ;
//fragment DIGIT_0_9        : [0-9] ;
//fragment DIGIT_1_9        : [1-9] ;
fragment WHITESPACE       : ' ' | '\t' ;

ZERO: '0' ;
NON_ZERO: '1'..'9' ;

//range-set  ::= range ( logical-or range ) *
//logical-or ::= ( ' ' ) * '||' ( ' ' ) *
//range      ::= hyphen | simple ( ' ' simple ) * | ''
//hyphen     ::= partial ' - ' partial
//simple     ::= primitive | partial | tilde | caret
//primitive  ::= ( '<' | '>' | '>=' | '<=' | '=' | ) partial
//partial    ::= xr ( '.' xr ( '.' xr qualifier ? )? )?
//xr         ::= 'x' | 'X' | '*' | nr
//nr         ::= '0' | ['1'-'9'] ( ['0'-'9'] ) *
//tilde      ::= '~' partial
//caret      ::= '^' partial
//qualifier  ::= ( '-' pre )? ( '+' build )?
//pre        ::= parts
//build      ::= parts
//parts      ::= part ( '.' part ) *
//part       ::= nr | [-0-9A-Za-z]+