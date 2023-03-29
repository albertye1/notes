*C is terrible. -Aristotle*
# Memory Allocation
*Competitive coding mfs when valgrind walks in:*
* There is `malloc(int amount_of_fucking_memory)` (most commonly used) which allocates a block of uninitialized memory, but also `calloc(int num_items, int mem_per_item)` which allocates blocks of zeroed memory. The functionality is generally the same and they'll probably make you use `malloc` anyways.
* Any allocated memory must be freed after usage.
	* We do this with `free(some pointer)` which frees the pointer that is passed in.
* What if, for a chuckle, we wanted to free some memory then use it for something else?
	* `realloc(void *ptr, size_t new_size)`!!
		* First, it expands or contracts the amount of memory that the pointer points to conform with `new_size`.
		* Then, it allocates a new memory block of size `new_size`, copying memory area with size equal the lesser of the new and the old sizes, and freeing the old block.
			* The old block is freed.
### Alignment
* There are rules as to how to ensure proper behavior of different data types
	* `char`: only one byte, has no alignment rule
	* `short`: 2 bytes, so aligned by the half word
	* `int`: 4 bytes, aligned by the word
		* Pointers follow the same rules as `int`, as they store the same amount of memory (4 bytes).
* By alignment, we basically start our allocation of data for the variable at the next available (whatever it's aligned to). So a short will always be at the start of a half word, and an int will always be at the start of a word.
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
# Structs
*Ah yes, now something I remember!!!!*
* Collecting multiple blocks of data in one "structure". 
* The information is basically all bunched next to each other, and is spaced according to the alignment rules.
# Unions
*Call me Elon Musk the way I hate these*
* Multiple data types that can be stored in one single block of memory
* It's allocated to be the largest of its multiple data types.
	* So if you have a union of `int` and `double`, it's gonna be 8 bytes not 4.
* If we set the whole memory block to zero, all elements of the union get set to 0. This is sort of useful for when we need to potentially use multiple different datatypes

C is compiled with [[CALL]], which turns it into assembly. Assembly literally works with the silicon, as seen in [[RISC-V Hardware]]. 