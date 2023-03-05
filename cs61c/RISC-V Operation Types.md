All operations are 32 bits, which are partitioned between different parameters.
## R-Type
* arithmetic between variables
* `rd`, `rs1`, `rs2`: destination register, 2 source registers
	* doing arithmetic in 2 operators
* each operation has 32 bits
	* `0-6`: opcode
	* `7-11`: rd
	* `12-14`: funct3
	* `15-19`: rs1
	* `20-24`: rs2
	* `25-31`: funct7
## I-Type
* arithmetic from source and immediate to destination
	* `0-6`: opcode
	* `7-11`: rd
	* `12-14`: funct3
	* `15-19`: rs1
	* `20-31`: `imm[11:0]`
* Designed for operations between 2 registers and one immediate
	* Arithmetic operations w/ immediates
	* jalr
	* ecall
	* not stores (those r s-type)
* Most components stored similar to r-type, except with the imm component.
	* `0-6`: opcode
	* `7-11`: rd
	* `12-14`: funct3
	* `15-19`: rs1
	* `20-24`: `imm[4:0]`
	* `25-31`: funct7
### I*-Type
* Shift instructions have an immediate of at most 31, so we specify a funct7 for this case. Then, our operation looks like
## S-Type
* Store-type operations
* Looks kinda like the r-type operation.
	* The immediate is cut into two, with the `[4:0]` part replacing the destination register and the `[11:5]` part replacing the funct7
	* Therefore, our breakdown looks like
		* `0-6`: opcode
		* `7-11`: `imm[4:0]`
		* `12-14`: funct3
		* `15-19`: rs1
		* `20-24`: rs2
		* `25-31`: `imm[11:5]`
* rs2 is before rs1 btw
## U-Type
* `lui`
	* `lui`: load upper immediate: `lui rd imm`
	* Load a much larger immediate than possible
		* `li 0x12345678` must become `lui t0 0x12345 0x678`
	* Corner case: `lui 0xABCDEFFF`. The last three `FFF` are going to 
*  `auipc`
	* Used primarily as a way to save an arbitrary value when used with an addi
		* The main diff is that it adds the result to program counter
* Each immediate has 20 bits
	* `0-6`: opcode
	* `7-11`: rd
	* `12-31`: immediate
## B-Type
* use relative instead of absolute addresses
	* the number of bytes we need to jump to get to next instruction
* storing offsets
	* all offsets must be multiples of 4
	* if we store the immediate directly as a signed number, the last two bits would always be 0
	* we just don't store the bottom, we still store the second-bottom because some operations will need
* bit structure
	* `0-6`: opcode
	* `7-11`: `imm[4:1|11]`
	* `12-14`: funct3
	* `15-19`: rs1
	* `20-24`: rs2
	* `25:31`: `imm[12|10:5]`
* branch instructions have 13-bit immediates = \[-4096, 4094\]
## J-Type
* Only one destimation and immediate (like U-Type)
	* `0-6`: opcode
	* `7-11`: rd
	* `12-31`: immediate (stored in order `[20|10:1|11|19:12]` because the circuits are a lot simpler this way)
* Note that the immediate is stored in an even stranger pattern
	* Jumps have 21-bit immediates, so up to $2^{18}$ instructions up/down.
## Translation
* it's not hard to translate a 32-bit number into an instruction.
	* Look at the table to see how the number is broken down
	* Last 7 digits will *always* be an opcode, and then we can use that to find what the operation type is
	* If there is a funct3 or funct7, we can use that to find the operation itself
	* Translate all the registers to decimal to find their register counts
		* Then, check the register count (`x##` where `##` is a two digit number, first digit can be 0). See which register it corresponds w/
	* Then, translate the immediate to a decimal number (unscramble, do any manipulations before the conversion)a
## Immediate too big?
* R or U type
	* Unneeded, since no imms or very specific use cases that never need to exceed the given imm length
* I or S type
	* For arith instructions, it's generally possible to store the imm in a temporary first
	* For loads and stores, we can add the offset first then do a 0-offset load
* B or J
	* If a branch is within 1024 instructions
		* Branch normally (ex `beq t0 t1 label`)
	* \>1024 instructions
		* invert the branch condition, and do a `j` instruction instead
			* `bne t0 t1 Next`
			* `j label`
			* `Next: # rest of code`
			* This is possible because `j` can go up to $2^{18}$
	* If a jump is within $2^{18}$ instructions
		* jump normally
	* \>$2^{18}$ instructions
		* do `auipc`, then use `jalr`'s immediate to offset the rest.