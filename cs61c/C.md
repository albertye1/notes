*C is terrible. -Aristotle*
# Memory Allocation
*Competitive coding mfs when valgrind walks in:*
* There is `malloc(int amount_of_fucking_memory)` (most commonly used), but also `calloc(int num_items, int mem_per_item)` which may make it a bit more clear. The functionality is generally the same and they'll probably make you use `malloc` anyways.
* Any allocated memory must be freed after usage.
	* We do this with `free(some pointer)` which frees the pointer that is passed in.
# Stacks and Heaps
*The spaces, fuckwad, not the data structures. This isn't a DS/A class!*
* Stack space = memory is allocated on the function call stack
	* Generally, basic data types go here
* Heap space = memory is allocated during the execution phase
	* Allocated memory is from here
* Static storage = any global variables or string literals
* Code storage
	* Code segments store the bytecode that comprise the program you're running
	* In `x = y + 1`, the `1` is "part of" the code.
# Unions
*Call me Elon Musk the way I hate these*
* Multiple data types that can be stored in one single block of memory
* It's allocated to be the largest of its multiple data types.
	* So if you have a union of `int` and `double`, it's gonna be 8 bytes not 4.
* If we set the whole memory block to zero, all elements of the union get set to 0. This is sort of useful for when we need to potentially use multiple different datatypes
# Structs
*Ah yes, now something I remember!!!!*
* Collecting multiple 
C is compiled with [[CALL]].