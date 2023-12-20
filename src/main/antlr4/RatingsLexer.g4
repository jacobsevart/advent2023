lexer grammar RatingsLexer;

@header {
    package com.jacobsevart.aoc.grammar;
}

AND : 'and' ;
OR : 'or' ;
NOT : 'not' ;
EQ : '=' ;
COMMA : ',' ;
SEMI : ';' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
LT     : '<' ;
GT    : '>' ;
COLON : ':';
NEWLINE : '\n';
X : 'x';
M : 'm';
A : 'a';
S : 's';

REJECT : 'R';
ACCEPT : 'A';

INT : [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\n\r\f]+ -> skip ;