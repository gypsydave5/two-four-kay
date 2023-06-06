grammar Curl;

parse: expression EOF;

expression: 'curl' (urlOptions? url | url urlOptions?) ;

urlOptions: option+;

option: optionName optionValue?;

optionName: OPTION_STRING;

optionValue: STRING | DQUOTED_STRING | SQUOTED_STRING;

url: STRING | DQUOTED_STRING | SQUOTED_STRING;

DQUOTED_STRING: '"' (~('"' | '\\' | '\r' | '\n') | '\\' .)* '"';
SQUOTED_STRING: '\'' (~('\'' | '\\' | '\r' | '\n') | '\\' .)* '\'';
OPTION_STRING: '-' STRING;
STRING: ~[ \t\r\n'"]+;

WS: [ \t\r\n]+ -> skip;
NEWLINE_ESCAPE: '\\' NEWLINE -> skip;
fragment NEWLINE: ('\r\n'|'\n'|'\r');
