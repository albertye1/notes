* Pseudoinstructions: Instructions that aren't native to assembly but can still be parsed
	* Examples: `mv a b` is just `addi a b 0`
## Compiler
* Translator: Translates from a higher-level programming language (i.e. C) into a lower-level assembly language (i.e. RISC-V)
	* Translating to lower level always means higher performance in your code
* Interpreter: Interprets code and evaluates it in the source language.
	* Useful for learning
	* Similar to emulators, a program that runs another program on top of that.
	* Scheme interpreter from 61A
* Output *can* contain pseudoinstructions.
## Assembler
* Translates from assembly into the machine code (zeroes and ones).
	* `gcc -S foo.c` makes the assembly code.
	* `gcc -c foo.s` makes the object code `foo.o`
* Reads and uses directives
	* Give directions to the assembler
	* List
		* `.text` subsequent items put in user text segment
		* `.data` subsequent items put in user data segment
		* `.globl sym` says `sym` is global, and thus can be referenced from other files.
		* `.string str` stores `str` in memory and null-terminates it
		* `.word w1 w2 ... wn` stores the $n$ 32-bit quantities in successive memory words.
* Replaces pseudoinstructions with true assembly
* Then, uses the above to produce the machine code
### Object File Format
1. **Object File Header:** size and position of other pieces of obj file
2. **Text Segment**: machine code
3. **Data Segment**: binary representation of static data in the source file
### Information Tables
4. **Relocation table**: list of items the file needs addresses of later
	* Relocate which?
		* **NOT** PC relative addr
		* **ALWAYS** external function ref
		* **ALWAYS** static data ref (e.g. using `la`)
		* External J-type ops
		* Loads and stores using `gp` to access `.data` variables
			* includes S and I type ops
5. **Symbol table**: list of items in the file usable by other files
	* labels, useful for function calling
	* data, anything in the `.data`, or variables that can be accessed across files
Completely unrelated to info tables, but
6. **Debugging information**. This is just after the information tables and there's not much to say about it
### Producing Machine Code
* Simple Case
	* Arithmetic, logic shifts, etc
	* All info is already in the instruction
* PC-Relative Case
	* Branches and jumps
	* Determine the offset by finding the number of *half-word* instructions between current & target
	* For this case, we pass over the code twice. First, store the positions of all the labels. Second, use the label positions to generate the machine code.
## Linker
* You may bring in a library file `lib.o`, and other object files. The linker `ln` is supposed to resolve all the assembled symbols.
	* Other executables: for example, what happens to even like `scanf`? That's not part of the code itself, it must be linked in.
	* References to static data
	* Assembler writes these in the **relocation table** and **symbol table**. (Specified above)
* Having a linker is why we don't need to recompile *everything* if we change one line in one file. Saves us a lot of time.
	* Linux source code is 20M lines of code, maybe compiling that many loc is bad if done everytime a file changes
* The linker outputs the final executable
### Procedure
1. Put together text segments from each .o file
2. Put together data segments from each .o file, then add this to the end of the original
3. Resolve references.
	1. Go thru reloc table and handle each entry, i.e., fill all **absolute addresses**.
4. Output an executable
Slowest part of the CALL process.
## Loader
* Running the executable and putting data into memory.
* Loader is the OS. Go back to 162.
### Statically v. Dynamically Linked Libraries
* **Static:** Put one thing into a working `a.out`, so that's all you need
* **Dynamic:** Don't compile the library into the executable, it should be the loader's job to take the dynamic library location in the `a.out` and *find* the library in real time!
	* Advantage: smaller executable size, easier to upgrade
	* Disad: need the library too!!!

WOOHOO! SO DENSE JUST LIKE MEEEEE