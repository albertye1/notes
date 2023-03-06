# Components
## Multiplexer
* Takes in multiple signals, and chooses one of those to output
## ALU
* arithmetic logic unit
* performs some arithmetic operation
# Datapath
# Control
* i'mma need to figure out how to get the diagrams into markown format, otherwise this will be useless.
* 5 phases of execution
	* IF, ID, EX, MEM, WB
	* Not all instructions are active in all phases
* Controller specifies how to execute instructions
	* impl'd as ROM or logic
* lw is the slowest thing
	* drops the frequency down to 1.25 GHz
## Control Logic Design
Two ways of designing this:
* ROM
	* 11-bit address of inputs
	* Contains 9 bits from the instruction, andd then two branches for BrEQ and BrLT
	* Just those substrings are needed to find the ROM.
* Combinational logic

*PACK IT UP Y'ALL, WE'VE JUST MADE ALL OF SOFTWARE ENGINEERING A HARDWARE PROBLEM!*