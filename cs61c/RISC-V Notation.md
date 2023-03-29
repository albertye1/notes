Two ways to store primitive data
* Registers
	* Variables
	* Usually 32 bits
* Immediates
	* Constants
	* Also 32 bits

Registers and immediates can be set with operations, see [[RISC-V Operation Types]]

## Important Registers
* `x0` is the register that stores 0 under any circumstance.
* `ra` is the **r**eturn **a**ddress
	* when jumping into a function, we always set the PC back to `ra` at the end.
	* need to make sure `ra` points to the correct place, otherwise the function will run incorrectly
* `sp` is the **s**tack **p**ointer
	* in order to store any information on the stack, we need to update this value to make space on the stack.

## Calling Conventions
* There are two types of registers
	* `ak` and `tk` registers are assumed to be garbage after an inner call ($k$ can be any number such that the register exists). They are *temporary* registers.
	* `sk` registers (as well as the `ra` register, which determines where we are in the code) are meant to be *saved* registers, meaning that their value is assumed to persist from when they are allocated in the stack until they are deallocated in the stack. 
		* For allocations, we have to subtract the amount of memory (usually 4 bytes) to set an `sk` register from the `sp` (stack pointer) register. 
		* This is equivalent to adding an element to the stack, and then setting the pointer to point to the next free space.
		* **NOTE!** Don't use any `sk` register before first ensuring that it has its place in the stack. Always need two lines of code
		* In order for the `sk` registers to work as expected, we also need to place the `sk` registers back 