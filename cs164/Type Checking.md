$\vdash e : T$ means that we can prove $e$ has type $T$.
$\vdash s$ means that $s$ type checks. 

We let $O$ be the **type environment**; all of our possible types essentially are inside the type environment and we type check within the set of all possible types.
## Dynamic and Static Types
- Recall from like 61B -- dynamic type is the class $C$ used in the $C()$ expression of initialization
	- this is a runtime notion, and is used even in "untyped" languages
- Static type is a notation that captures all possible dynamic types $C$ can take upon, which is a compile-time notion.
- Early type systems: Set of static types correspond directly with the set of dynamic types: for all expressions $E$, we have 
- A variable of static type $A$ can hold values of static type $B$, if $B = A$.

We can make an update to the soundness theorem:
- 
## Variable Rules
Here, we define $T_1 \leq T$ to mean that $T_1$ is a **subtype** of $T$.
	$O(id) = T, O \vdash e_1 : T_1, T_1 \leq T \implies O \vdash id: T = e_1$. (var-init)
	$O(id) = T, O \vdash e_1 : T_1, T_1 \leq T \implies O \vdash id: T = e_1 : T_1$. (var-assign)
For example, for strings, we have:
### Least Upper Bounds
* $lub(x, y)$, the least upper bound of $x$ and $y$\, is $z$ if 
	* $X \leq Z \land Y \leq Z$
		* $Z$ is an upper bound
	* $X \leq Z' \land Y \leq Z' \implies Z \leq Z'$
		* $Z$ is least among upper bounds
	* the $lub$ of two types in chocopy will be its least common ancestor in the inheritance tree.
## Function Rules
- Invocation
	- type information stored about a function $f$ is stored in $O$
- Definition
- Wrong Definition
	- $O \vdash e_1 : T"_1, O(f) = \{\$ret: T_0, x_1 : T_1\}, T_1 \leq T"_1 \implies O \vdash f(e_1) : T_0$  
		- bad program is well-typed
## Return Statement Rule
- idk
- we ended at method dispatch
- i have no clue what's going on
- though im hopeful yes i am hopeful for today