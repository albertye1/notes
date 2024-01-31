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
	* **asynchronous:** trap handler
		* requests attention by asserting some prioritized interrupt request lines.
		* saves EPC before enabling interrupts to allow nested interrupts
			* need an instruction to move EPC into GPRs 
			* need a way to move further interrupts at least until EPC can be saved. 
		* needs to read the **cause** register, indicating the reason for the trap.
		* use a special indirect jump instruction `ERET`, which 
			* enables interrupds
			* restores processor to user mode
			* restores hardware status / control state
			* sets PC to EPC and resume execution
			* essentially, pretend nothing happened.
	* **synchronous traps**: exception handling
		* caused by exception on a *particular* instruction. 
		* so this means the instruction can't be completed, must be restarted
			* may require undoing one or more unfinished instructions
		* in case of system call trap, instruction is considered complete
			* `ECALL` in RISC-V causes a trap into a higher privilege mode
		* exception handling
			* how to handle multiple simul. exceptions in different pipeline stages? and how/where to handle external async. interrupts?
			* in the 5-stage pipeline, errors are:
				* PC address exception
				* Illegal opcode
				* Overflow
				* Data address exception
			* in the 5-stage pipeline, actions are:
				* hold exception flags in the pipeline until commit point (M stage)
				* exceptions in earlier stages override later ones for a given instruction
				* inject external interrupts @ commit
				* if trap is at the commit, update the `cause` and `epc` registers, kill all stages, inject handler PC into the fetch stage.
			* speculation
				* prediction mechanism
				* check prediction mechanism
				* recovery mechanism
					* only write architectural state at the commit point, so can throw away partially executed instructions after exception.
					* launch exception harder after flushing pipeline
				* bypassing allows use of uncommitted inst. results by following instructions
	After this, we started talking about [[Pipelining]] and other possible methods for pipelining.
