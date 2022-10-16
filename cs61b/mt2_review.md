# Dynamic Method Selection

For classes that inherit other classes, for example `A x = new B();`, the methods run are going to be those in A unless you cast A to B.
If there is a method `m` that is only in B, the only way to run `m` is to do `(B)x.m()`

# DSU

Should unite, check connection, find set. Shouldn't unite two in the same set as well

Below are some implementations of DSU:

## QuickFind`
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

# 2-3 Tree

A 2-3 tree is a tree with two types of nodes.
- 2 node: has one node with degree 2 and one value
- 3 node: has one node with degree 3 and two values.

This is a bijection to a red-black tree. Construction of a red-black tree involves
turning the 3-nodes of a 2-3 tree into *red* edges connecting two nodes. 


---insert more on how to do this---

## LLRB

It's a red-black tree, but in case of any dispute, all red edges must be towards a 
left child. If they say LLRB, in most cases they're just talking about something
that a binary search can do. In C++, `std::set` is defined by RB trees.

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

# Heap

Add stuff
