# Tail Call Optimization
Recursion is like really memory intensive, and the recursion stack has a maximum depth that can't be exceeded.

If we are just doing a recursive call at the end of a frame without doing any more manipulation of the frame, then there is no need to store the frame and we can just delete it.

This way, we can afford to do more recursion calls and increase the recursion depth.

# Tail Calls
Procedure call that has not yet returned is active
Some procedure calls are tail calls, and a Scheme interp should support an unbounded number of active tail calls in const. space.

A tail call is a call xp in a tail context:
- The last body sub-exp in a `lambda`
- Sub expressions 2 & 3 in a tail context `if` expression
- All non-predicate sub-exp's in a tail context `cond`
- The last sub-exp in a tail context `and`, `or`, `begin`, or `let`

## EX: Length of a list
```
(define (length s))
	(if (null? s) 0
		(+ 1 (length (cdr s)))
```
Above is not in a tail context, since we are recursing on `(length (cdr s))` at the end but we are also still menipulating the value we get from `(length (cdr s))`.

Linear recursive procedures can often be written to use tail calls, however
```
(define (length-tail s)
	(define (length-iter s n)
		(if (null? s) n
			(length-iter (cdr s) (+ 1 n))))
	(length-iter s 0))
```
does only use tail calls, because it calls each of the frames we've written at the end without needing to do anything else in the current frame.

With tail call optimization, we are not increasing our space complexity for recursion, so we can have arbitrarily large recursion depth. Therefore, we can find the length of arbitrarily long lists.

## Ex: Map with only a Constant # of Frames
```
(define (map precedure s)
	(if (null? s)
		nil
		(cons (procedure (car s))
			(map procedure (cdr s)))))
```
^^^ This is not a tail context, because the `cons` is a manipulation of the return value.

## EX: Project Extra Credit!!!!
- Get rid of useless frames somehow
- Will make doing 100000! possible
- Wolfram Alpha is prolly doing this