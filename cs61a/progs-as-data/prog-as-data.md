# Programs as Data
- in py, code can be rep'd as a string
- Two functions exist in Python that let you run string as code
	- `eval`: evaluates an expression and returns the resulting value of this expression
		- Doesn't allow for assignment statements, for/while loops, multi line statements
	- `exec`: executes a command and returns `None`.
		- Does allow for multiple lines of code
		- Allows for assignment 
## Quasiquotation
- Scheme `quasiquote`: Use ``` ` ``` to start a quasiquote, which can be "unquoted" by `,`. Basically, everything that is unquoted must be evaluated, while everything else is just printed.
	- Impl: 
	```
	(define a +)
	>> a
	(define b 4)
	>> b
	(define c 5)
	>> c
	`(a ,b c)
	>> (a 4 c)
	````
