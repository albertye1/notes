# Hash Map

Take in a hash value, but modulo a map size M. Assume we have N elements.

For each element, the index it goes to in the map is hashcode(x) mod M. There are N elements
but if it is spread out equally in the hash map we will get constant number of elements
in each bucket. This is why we resize when the **load factor** gets too high. 

Because of this construction, it doesn't matter if we have Theta(N) or even some crazy
Theta(N^2) algorithm usually, it all turns into Theta(1) amortized. So it doesn't really
matter what data structure is used to store the buckets as long as it stores a linear 
set.
