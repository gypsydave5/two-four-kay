grammar Curl;

parse: expression EOF;

expression: 'curl' urlOptions? url;

urlOptions: option+;

option: optionName optionValue;

optionName: '-X' | '-H' | '-d' | '-u' | '-L' | '-G' | '-A' | '-e' | '--request' | '--header' | '--data' | '--user' | '--location' | '--get' | '--user-agent' | '--referer';

optionValue: STRING | DQUOTED_STRING | SQUOTED_STRING;

url: STRING | DQUOTED_STRING | SQUOTED_STRING;

DQUOTED_STRING: '"' (~('"' | '\\' | '\r' | '\n') | '\\' .)* '"';
SQUOTED_STRING: '\'' (~('\'' | '\\' | '\r' | '\n') | '\\' .)* '\'';
OPTION_STRING: '-' ~[ \t\r\n'"]+;
STRING: ~[ \t\r\n'"]+;
WS: [ \t\r\n]+ -> skip;