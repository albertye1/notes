# DSU

All features should take $\mathcal O(log N)$, and the height is minimized by adding smaller tree 
to root of larger tree

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

# Hash Map

Take in a hash value, but modulo a map size M. Assume we have N elements.

For each element, the index it goes to in the map is hashcode(x) mod M. There are N elements
but if it is spread out equally in the hash map we will get constant number of elements
in each bucket. This is why we resize when the **load factor** gets too high. 

Because of this construction, it doesn't matter if we have Theta(N) or even some crazy
Theta(N^2) algorithm usually, it all turns into Theta(1) amortized. So it doesn't really
matter what data structure is used to store the buckets as long as it stores a linear 
set.

# Heap

Add stuff
