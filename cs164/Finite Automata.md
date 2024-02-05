## Lexical Analysis
* Example:
```c
if (i == j) 
	z = 0;
else
	z = 1;
```
* The input is now just a sequence of chars:
	* `\tif (i == j)\n\t\tz = 0;\n\telse\n\t\tz=1`. 
* Our goal is to partition the input string into substrings, and classify them according to role.
### Token
* An output of lexical analysis is a string of tokens. The parser will rely on the distinctions between tokens.
* Tokens corresponds to sets of strings.
	* Identifier: Strings of letters / digits.
	* Keyword: `else`, `if`, `begin`, etc..
	* Integer: non-empty string of digits
	* White-space: Non-empty sequence of blanks, newlines, tabs
	* OpenPar: a left-parenthesis
* An implementation of a lexical analyzer must:
	* recognize substr corresponding to tokebn
	* return **lexeme** of the token
		* e.g. (Whitespace, `'t`), (Keywird, `if`), etc... for the example code above.
	* discard "uninteresting" tokens that don't contribute to parsing
	* **question: what happens if we remove all whitespace & comments prior to lexing?**
* Lookahead: try to find where one token ends, and the other begins.
	* We'd need a way to describe the lexemes and resolve ambiguities.
### Regular Languages
* There are several formalisms for specifying tokens: 
	* Regular languages are the most popular.
* Let $\Sigma$ be a set of characters. A **language over** $\Sigma$ is a set of strings of characters drawn from $\Sigma$, which is called the **alphabet**.
### Regular Expressions
* Each regular expression is a notation for a regular language. If $A$ is a regex, then we write $L(A)$ to refer to language denoted by $A$.
* Atomic Regex:
	* Epsilon: $L(\epsilon) = \{\texttt{""}\}$. 
	* Single character: $L(C)=\{c\}$ for any $c \in \Sigma$.
	* Concatenation: $L(AB) = \{ ab \mid a \in L(A) \land b \in L(B)\}$. 
	* Union: $L(A \mid B) = \{s \mid s \in L(A) \lor s \in L(B)\}$.
	* Iteration