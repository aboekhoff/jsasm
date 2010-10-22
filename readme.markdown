# jsasm

A Clojure tool for emitting JS

## Usage

'jsasm.ast/emit-tokens takes a sequence of tokens and returns      
a javascript string.

The syntax of the ast is as follows:

    token    = lit | array | object | project | call | function |        
               if | while | enum | throw | try | operator |      
               var | return | break    
    tokens   = [token*]    
    lit      = [:LIT (symbol | number | string)]     
    array    = [:ARRAY tokens]     
    entry    = [:ENTRY token token]               ; foo : bar    
    entries  = [entry*]    
    object   = [:OBJECT entries]                  ; { foo : bar, ... }    
    function = [:FUNCTION tokens tokens]          ; function ( ... ) { ... }     
    project  = [:PROJECT token token]             ; foo[bar]    
    call     = [:CALL tokens]                     ; foo(...) { ... }    
    if       = [:IF token tokens tokens?]    
    while    = [:WHILE token tokens]                  
    enum     = [:ENUM token token tokens]    
        
    throw    = [:THROW token]    
    catch    = [:CATCH token tokens]             
    finally  = [:FINALLY tokens]    
    try      = [:TRY tokens catch finally?]    
        
    operator = [:OPERATOR symbol [token+]] ; =, +, <, ==    
    var      = [:VAR tokens]    
    return   = [:RETURN token]    
    break    = [:BREAK]    
    
        
Some helpers for creating tokens live in jsasm.ast.

The ast is awkward to write by hand, but is a convenient compile target.
For an example of a simple compiler with clojure-like syntax    
that uses jsasm as a backend see Scrypt.

## Installation

;;;;

## License

Copyright (C) 2010 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
