# Mark & Sweep
- mark phase: trace reachable objects
	- every obj has extra bit: the mark bit. reserved for mem. management, initially set to 0 and set to 1 if marked as reachable
	- problems
		- it's invoked when we're out of space
		- yet constructing the to-do list needs space
		- and we can't reserve the space since we don't know size of todo list
		- also the memory can be fragmented
	- advantages
		- no need to update pointers to objects
		- works for C/C++
```

```
- sweep phase: collect garbage objects
	- scans the heap looking for objects w/ mark bit 0
	- any such objects will be garbage, and will be put onto the free list
	- pseudocode:
```
# sizeof p is size of block starting at p
p := bottom of beap
while p < top of heap:
	if mark(p) = 1:
		mark(p) := 0
	else:
		add block p...(p + sizeof(p) - 1) to freelist
	p := p + sizeof(p)
```
# Stop & Copy
- memory arranged into two areas
	- old space: used for allocation
	- new space: used as reserve for gc
- start when old space is full
	- copies all reachable objects from old -> new space
	- reverse the roles of old & new space, resume program
- C doesnt allow copy