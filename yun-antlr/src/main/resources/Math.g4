grammar Math;

// 顶层规则：一条至多条语句
prog:   stat+ ;

// -------------给每个备选分支打标签
stat:   expr NEWLINE                # printExpr  // 表达式语句(表达式后跟换行)
    |   ID '=' expr NEWLINE         # assign     // 赋值语句(左值是标识符，右值是表达式)
    |   NEWLINE                     # blank     // 空语句(直接一个换行)
    ;

// 表达式
expr:   expr op=('*'|'/') expr      # MulDiv    // 表达式 乘除 表达式
    |   expr op=('+'|'-') expr      # AddSub    // 表达式加减表达式
    |   INT                         # int       // 一个整形值
    |   ID                          # id        // 一个标识符
    |   '(' expr ')'                # parens    // 表达式外加一对括号
    ;

//词法符号
// -------------给运算符号设置名字，使其也编成词法符号 token
MUL :   '*' ;
DIV :   '/' ;
ADD :   '+' ;
SUB :   '-' ;

// -------------词法符号 token
ID  :   [a-zA-Z]+ ;      // 标识符：一个到多个英文字母
INT :   [0-9]+ ;         // 整形值：一个到多个数字
NEWLINE:'\r'? '\n' ;     // 换行符
WS  :   [ \t]+ -> skip ; // 跳过空格和 tab
