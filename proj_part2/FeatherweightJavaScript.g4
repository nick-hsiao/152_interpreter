grammar FeatherweightJavaScript;

@header { package edu.sjsu.fwjs.parser; }

// Reserved words
IF        : 'if' ;
ELSE      : 'else' ;
WHILE     : 'while';
FUNCTION  : 'function';
VAR       : 'var';
PRINT     : 'print';


// Literals
INT       : [1-9][0-9]* | '0' ;
BOOL      : 'true' | 'false' ;
NULL      : 'null';

// Symbols
MUL       : '*' ;
DIV       : '/' ;
ADD       : '+' ;
SUB       : '-' ;
MOD       : '%' ;

ASSIGN    : '=' ;

GREATER_THAN       : '>' ;  
LESS_THAN          : '<' ; 
GREATER_THAN_EQ    : '>=' ; 
LESS_THAN_EQ       : '<=' ; 
EQUAL              : '==' ;

SEPARATOR          : ';' ;

// Identifiers
IDENTIFIER      : [a-zA-Z_]+[a-zA-Z_0-9]* ;

// Whitespace and comments
NEWLINE       : '\r'? '\n' -> skip ;
LINE_COMMENT  : '//' ~[\n\r]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;
WS            : [ \t]+ -> skip ; // ignore whitespace

// ***Parsing rules *** 

/** The start rule */
prog: stat+ ;

stat: expr SEPARATOR                                    # bareExpr
    | IF '(' expr ')' block ELSE block                  # ifThenElse
    | IF '(' expr ')' block                             # ifThen
    | WHILE '(' expr ')' block                          # whileExprBlock
    | PRINT '(' expr ')' SEPARATOR						# printExpr
    | SEPARATOR                                         # EmptyStatement
    ;

expr: expr op=( '*' | '/' | '%' ) expr                      # MulDivMod
	| expr op=( '+' | '-') expr                             # AddSub
	| expr op=('<' | '<=' | '>' | '>=' | '==') expr         # Equality
	| '(' expr ')'                                          # parens
	| VAR IDENTIFIER ASSIGN expr                            # Declaration
	| IDENTIFIER ASSIGN expr                                # Assign
	| IDENTIFIER '(' expr? (',' expr)* ')'                  # ahhhhh
	| FUNCTION '(' IDENTIFIER? (',' IDENTIFIER)* ')' block  # FunctionDec
    | BOOL                                                  # bool
    | NULL                                                  # null
    | INT                                                   # int
    | IDENTIFIER                                            # iden
    ;	


block: '{' stat* '}'                                    # fullBlock
     | stat                                             # simpBlock
     ;
