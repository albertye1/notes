*What I was scared of all along*

The datapath basically turns all of our operations into circuits.
![[Screenshot 2023-04-29 at 7.07.36 PM.png]]
* `PCSel`: if there's a branch or jump, you'll want to make the PC the label instead of `PC + 4`
* `ImmSel`: Different instruction types have different ways of making immediates. So, we need some way of determining which to use
* `RegWEn`: do we write to the register rd? (no if branch or store)
* `Bsel`, `Asel`: picking what to add to ALU. `A` is between PC and rs1, while `B` is between immediate and rs2.
* `ALUSel`: Pick the operation you want to do to ALU -- add, sub, mul, bsel, etc
* `MemRW`: should i read or write to the memory
* `WBsel`: What should I write to the destination register? (only if `RegWEn` is enabled)

## Control Logic
* 5 phases of execution
	* IF, ID, EX, MEM, WB
	* Not all instructions are active in all phases
* Controller specifies how to execute instructions
	* impl'd as ROM or logic
* lw is the slowest thing
	* drops the frequency down to 1.25 GHz
### Control Logic Design
Two ways of designing this:
* ROM
	* 11-bit address of inputs
	* Contains 9 bits from the instruction, andd then two branches for BrEQ and BrLT
	* Just those substrings are needed to find the ROM.
	* This is less hard! But still hard.
* Combinational logic
	* This is hard!

*PACK IT UP Y'ALL, WE'VE JUST MADE ALL OF SOFTWARE A HARDWARE PROBLEM!*