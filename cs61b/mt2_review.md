# Dynamic Method Selection

For an object `x` such that `A x = new B();`
- `A` is **static type**
- `B` is **dynamic type**
For inherited methods, if method is static choose from `A`, if non-static choose from `B`.
For non-static methods, parameters are pulled from `A`. If there are no functions in `B` with the same
parameters, we use the function in `A`. Parameters must be exact; if there is a function in `B` that takes
in a subclass of the parameter used for `A`, we still use the method in `A`.

Casting changes the static type, but not the dynamic type. If casting makes the dynamic type above the static type, it does RE but not CE.
When an object is cast, it's viewed by the compiler in its static type and not its dynamic type (obviously).

# Iteration

Any class that extends the interface `Iterable` must have a function `iterator` that returns an `Iterator`.
An iterator basically gives the current value and a way of getting to the next value until an end value.

Although practically this should be done to iterate through the entire DS, we will need to look at how it's actually
implemented in order to check how it actually behaves.

Fall 2020 Problem 3 is a good example of when the iterator behaves counter-intuitively.

# DSU

Should unite, check connection, find set. Shouldn't unite two in the same set as well

Below are some implementations of DSU:

## QuickFind
Create optimal solution to impelement `isConnected(a, b)`. Use an array `par` of ints to track which set each element belongs to, and check 
if `par[a] = par[b]`. Only problem is that `connect` is slow, we need to change all values of one set when connecting two sets.

## QuickUnion
Goal is to improve quickFind. To union `a, b` we find `root(a), root(b)` and set `root(b) = root(a)`. And to check find, just check if the roots 
are the same.

Worst case, `connect` and `isConnected` run in $\mathcal O(N)$, but this only happens when the trees are really spindly and it becomes almost like a linked list.
So.. minimize the height

## WQU
All features should take $\mathcal O(\log N)$, and the height is minimized by adding smaller tree 
to root of larger tree. Store the parent and the rank, and then merge by size.

## WQU + Path Compression
When we do findSet, we set `root(i) = findSet(root(i))`, which is essentially taking the log of a log of a log -> $\Theta(1)$ on average.

# BST
A binary tree where left child of a node is always less and right child is always greater.

## Insertion
New items in the BST are always inserted as leaves. Move down, going to the left child if our current node is too big, and right child of our current node
is too small. Duplicate nodes cannot exist.

## Deletion
If there are zero children, deletion is very easy, as you can delete the node without affecting the rest of the tree. 

If there is one child, we simply update the parent of the child to be the parent of the current node. The order is still here, because $a < b$ and $b < c$ means $a < c$.

If there are two children, this is harder. We can replace the node with either the largest descendant on the left subtree, or the smallest descendant on the right subtree.

Asymptotics:
- Bushy BST -- searching, insertion, deletion are all $\Theta(\log N)$ because the height is $\log N$.
- Spindly BST -- searching, insertion, deletion are all $\Theta(N)$ because the height is $N$.
- Taller trees run slower.

# 2-3 Tree

A 2-3 tree is a tree with two types of nodes.
- 2 node: has one node with degree 2 and one value
- 3 node: has one node with degree 3 and two values.

This is a bijection to a red-black tree. Construction of a red-black tree involves
turning the 3-nodes of a 2-3 tree into *red* edges connecting two nodes. 

## Insertion
- Insert into a leaf node, according to BST rules
- When travelling from the root to the leaf, if we see any nodes @ capacity then push the middle element up & split
- Split nodes after inserting

## Red-Black Tree
Representation of a B-tree (2-3, 2-3-4 tree) that is easier to work with
- Color a branch *red* to indicate it's in a stuffed node
- Path from root to any leaf will see the same number of black branches $\Theta(\log N)$ to travel through.

## LLRB

It's a red-black tree, but in case of any dispute, all red edges must be towards a 
left child. If they say LLRB, in most cases they're just talking about something
that a binary search can do. In C++, `std::set` is defined by RB trees.

### Insertion
When inserting into an LLRB we insert pretty much like a BST:
- Insert the item as a new leaf but color its edge red
- We have 3 operations:
	- `rotateLeft(A)`: we have $A$ as parent and $B$ as right child of $A$ with children $C, D$. then $B$ is new parent, $C$ becomes child of $A$.
	- `rotateRight(A)`: we have $A$ as parent and $B$ as left child of $A$ with children $C, D$. then $B$ is new parent, $C$ becomes child of $A$.
	- `colorFlip(B)`: if $B$ has two red children, we flip colors of all nodes linking to $B$.

Rules:
- Nodes are always inserted as red children
- If nodes are added to the right, the tree will be rotated until it's left-leaning again.
- If there are two children with red edges leading, perform a color flip.

# Hash Table

Take in a hash value, but modulo a map size M. Assume we have N elements.
For each key object, we have a hash function that gives a hash code, which is then taken modulo 
M.

A valid hash function must be:
- Deterministic: the hash function always returns the same hash code for the same input
- Equal objects will have the same hash code

A good hash function must be:
- Uniform: evenly distributes the elements, which avoids collisions
- If number of elements > number of buckets (N > M), collisions are inevitable.
- Collisions can be handled by either open addressing or **separate chaining**, the latter is covered the former is not

For each element, the index it goes to in the map is hashcode(x) mod M. There are N elements
but if it is spread out equally in the hash map we will get constant number of elements
in each bucket. This is why we resize when the **load factor** gets too high. 

Amortized: $\mathcal O(1)$
- given enough resizing to keep a good value-bucket ratio

Worst Case: $\mathcal O(N)$
- a linked list of size $N$

Best Case: $\mathcal O(1)$

Because of this construction, it doesn't matter if we have $\Theta(N)$ or even some crazy
$\Theta(N^2)$ algorithm usually, it all turns into $\Theta(1)$ amortized. So it doesn't really
matter what data structure is used to store the buckets as long as it stores a linear 
set.

# Asymptotics
Only consider the largest term, ignore coefficients. $O(f(n))$ works if $f(n)$ is any upper bound for the runtime, and $\Omega(f(n))$ works
if $f(n)$ is any lower bound for the runtime, so like $\Omega(1)$ always works.

If we want to find asymptotics in terms of $N$, we try to find the complexity for $N$ approaching infinity. So any "edge cases" for small $N$ will not cut it

# Heap

Min-Heap Property: The root of a tree/subtree is the minimum value, and other values are all less.
- Max-Heap property is pretty much the same thing.

In Java, we store the min-heap as an array, `[x, 1, 2, 4, 5, 3, 7, 8]`. Tree is traversed in level-order (which is just breadth first).
So we do level 1 (root), then level 2 (children of root), etc. So `parent[i] = i / 2;`, `leftChild(i) = 2*i`, `rightChild(i) = 2*i+1`.

## Insertion
Insert the new element to the end of the array, which is the bottom-most layer & left-most element. 
To make it agree with the principle, we bubble-up by swapping it with any parent that is larger than it. 

Because heaps are bushy by construction, the complexity is logarithmic.

## Deletion
Delete the root node, and switch it with the bottom-most level & right-most element (last element). 
TO make it agree with the principle, we bubble-down by swapping it with its smaller child.

Again, the complexity is logarithmic.
