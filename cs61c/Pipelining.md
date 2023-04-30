*Woohoo!*
# Pipelines
* Pipelining increases the latency (number of cycles per operation), but also increases the clock speed (amount of time per operation) and the throughput (number of operations per cycle).
* In pipelining, we essentially only run certain parts of the circuit on each execution
	* There is a reference pipelined circuit on the ref sheet
## RISC-V, Specifically
* We have five stages:
	* `IF`: Instruction Fetch
		* Contains the PC MUX, PC, IMEM, and PC+4 components
	* `ID`: Instruction Decode
		* Contains immediate generator, register
	* `EX`: Execute
		* Has ALU and branch comp. + MUXes that determine actions of ALU
	* `MEM`: Memory Access
		* Access DMEM
	* `WB`: Write Back
		* Write back to the register
* Cuts the single-cycle datapath into various smaller component parts
# Hazards
* Structural hazards
	* The hardware does not support having these instructions run at the same time
	* Solution 1 (inefficient): stall the instruction
	* Solution 2: add more hardware
	* RISC-V's required regfile design will avoid these structural hazards.
		* In one stage, assembly reads up to two operands in ID stage and writes up to 1 operand in WB stage.
		* We thus have instruction memory (IMEM) for data read during the IF stage and data memory (DMEM) for data read during the MEM stage. 
		* Otherwise, we would be reading more than two operands which would lead to a structural hazard and thus a stall
* Data hazards
	* Instructions have an unfulfilled data dependency, and we need to use data that has yet to be written
	* Three cases to consider:
		* **Register Access**: If the same register is written and read in one cycle: We need to write the value before ID then reads the new value. The solution in this case is to write then read.  
		* **ALU Result:** Instruction depends on WB's regfile write from the previous instruction. We have two solutions: the first is just stalling, which reduces performance; the second is forwarding.
			* We don't wait for the value to be stored into the regfile, and just grab the operand from the pipeline stage.
			* This is implemented by adding extra connections in the datapath.
		* **Load data hazard**: This one actually needs a stall to fix.
			* Consider the following sequence:
				* `lw s1, 8(s0)` then `or t3, s1, t1`. 
			* We need to access something only changed during the end of the clock cycle at the beginning of the clock cycle. The only way this can happen is if we have a load delay slot (one `nop` between `lw` and `or` instructions).
			* One way to salvage performance is to do **code scheduling**, which is adding an instruction unrelated to the load result during the compilation stage.
* Control hazards
	* When a branch or jump information is not yet resolved but we need to do a branch/jump.
	* The flow of execution depends on a previous instruction
	* Note that if we take the branch, then we have to kill all instructions after the branch, if taken. We flush the pipeline by converting all invalid operations into `nop`.
		* At the next stage, we start again with the IF stage of the instruction at `Label`.
	* Solution: Branch Prediction
		* Every taken branch in the simple RV32I pipeline costs **3 clock cycles.**
			* If branch not taken, then pipeline is not stalled, the correct instructions are fetched after the branch instruction.
		* However, if the prediction is incorrect it will still cost 3 clock cycles.