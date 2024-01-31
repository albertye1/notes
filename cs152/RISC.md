# RISC
* "iron law" of processor performance
	* $\dfrac{\text{time}}{\text{program}} = \dfrac{\text{inst}}{\text{prog}} \cdot \dfrac{\text{cycles}}{\text{inst}} \cdot \dfrac{\text{time}}{\text{cycles}}$.
	* instructions per program depends on source code, compiler technology, and ISA.
	* cycles per instruction depends on ISA and microarchitecture
		* microcoding - >1 CPI, short cycle
		* single-cycle unpipelined - 1 CPI, long cycle
		* pipelined - 1 CPI, short cycle
	* how to find: 
		* pipelined only counts after the initial stage
* data hazards
	* data-dependence: read-after-write.
	* anti-dependence: write-after-read. why should we care?
		* in cases with multiple processes
		* out-of-order ISA's
	* output-dependence: write-after-write
	* solutions
		* interlock: wait until the hazard clears, or wait for some operand so it can be read
		* bypass: short-circuit the operand
		* speculate: guess the value, correct later if wrong
			* only effective in branching, sp updates, addr disambiguation
	* 
* traps
	* trap handler
		* saves EPC before enabling interrupts to allow nested interrupts
			* need an instruction to move EPC into GPRs 
			* need a way to move further interrupts at least until EPC can be saved. 
	* exception handling
		* Should all work.
		* PC address exception
		* Illegal opcode
		* Overflow
		* Data address exception
