grammar Curl;

/*
 * Parser Rules
 */

curl : CURL_COMMAND opts myUri EOF;

myUri : UNQUOTED_STRING | ((SQ | DQ)? UNQUOTED_STRING (SQ | DQ)?);

opts: option* ;

option :  requestFlag | headerFlag ;

requestFlag : REQUEST_FLAG  UNQUOTED_STRING ;


headerFlag : HEADER_FLAG header ;

header: UNQUOTED_STRING | headerKeyValue ;

headerKeyValue: SQ headerKey headerValue SQ;

headerKey: UNQUOTED_STRING;

headerValue: UNQUOTED_STRING;

/*
 * Lexer Rules
 */


CURL_COMMAND : 'curl';

REQUEST_FLAG : '--request' | '-X';

HEADER_FLAG : '-H';

UNQUOTED_STRING : ~['" ]+;

SQ : '\'' ;

DQ : '"' ;

COLON: ':' ;

WHITESPACE : ' '+ -> skip ;

H_KEY : ~['" :]+':';
