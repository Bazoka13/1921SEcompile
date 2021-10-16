grammar sysy;
program
    : compUnit
    ;
compUnit
    : funcDef
    ;
funcDef
    : funcType Ident L_PAREN R_PAREN block
    ;
funcType
    : 'int'
    ;
Ident
    : 'main'
    ;
block
    : L_BRACE stmt R_BRACE
    ;
stmt
    : RETURN number SEMICOLON
    ;
RETURN
    : 'return'
    ;
L_PAREN
    : '('
    ;
R_PAREN
    : ')'
    ;
L_BRACE
    : '{'
    ;
R_BRACE
    : '}'
    ;
SEMICOLON
    : ';'
    ;
number
    : (DECIMAL_CONST | OCTAL_CONST | HEXADECIMAL_CONST)
    ;
DECIMAL_CONST
    : [1-9]
    | [1-9] [0-9]+
    ;
OCTAL_CONST
    : '0'
    | ('0' [0-7]+)
    ;
HEXADECIMAL_CONST
    : ('0x'|'0X') [0-9A-Fa-f]+
    ;
DIVIDE
    : ('\t'|'\n'|'\r'|' ')+ ->  skip
    ;
SINGLE_COMMENT
    : '//' (~('\n'|'\r'))* '\n' -> skip
    ;
MULTI_COMMENT
    : '/*' .*?
       '*/' ->skip
    ;