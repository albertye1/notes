*And not the one from me to a woman!*
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
* Data hazards
	* 
* Control hazards
	* When a branch or jump information is not yet resolved but we need to do a branch/jump.