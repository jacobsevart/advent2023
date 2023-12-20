parser grammar RatingsGrammar;
options { tokenVocab=RatingsLexer; }

@header {
    package com.jacobsevart.aoc.grammar;
}

problem: workflows NEWLINE* parts;

workflows: workflow (NEWLINE workflow)*;

workflow: ID LCURLY stmts RCURLY;

stmts: stmt (COMMA stmt)*;

stmt: comparison COLON consequence | consequence;

consequence: REJECT | ACCEPT | ID;

comparison: (X | M | A | S) comparator INT;

comparator: LT | GT;

parts : part (NEWLINE part)*;

part :
    LCURLY
    X EQ INT COMMA
    M EQ INT COMMA
    A EQ INT COMMA
    S EQ INT
    RCURLY;