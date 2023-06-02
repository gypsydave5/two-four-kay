grammar Curl;

import url;


/*
 * Parser Rules
 */

curl : CURL_COMMAND request? uri EOF;

//options: option* ;

option :  request ;

request : REQUEST_FLAG  UNQUOTED_STRING ;

/*
 * Lexer Rules
 */

CURL_COMMAND : 'curl';

REQUEST_FLAG : '--request' | '-X';

WHITESPACE : ' '+ -> skip ;

UNQUOTED_STRING : UPPERCASE+;

//fragment LOWERCASE  : [a-z] ;
//
fragment UPPERCASE  : [A-Z] ;

//QUOTED_STRING : '"' STRING? '"';
//STRING : STRING_CHARACTER+;
//fragment STRING_CHARACTER :  ~["\\] | ESCSEQ;
//fragment ESCSEQ : '\\' [tnfr"'\\];


//fragment FLAGNAME : ( UPPERCASE | LOWERCASE | '-' )+;


