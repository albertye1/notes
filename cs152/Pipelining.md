[[#^650b99]]


* Initially: The 5-stage pipeline!
	* Mostly to deal with higher latency load, float ops?
	* Branching, only if coming from another long latency operation.
* Pipeline depth = longest path from the fetch stage to the writeback stage.
## Deeper Pipelines - MIPS R4000
The 8 stages: IF -> IS -> RF -> EX -> DF -> DS -> TC -> WB. Commit point is between TC and WB.
- IF - instruction fetch
- IS - issuing operations to ALU/FPU/Load store unit/other computational unit.
- RF - regfile
- EX - execute
- DF - data fetch
- DS - data store
- TC - tag check -- checking one set of cache to find the correct address
- WB - writeback

* Looks the same as far as regfile is concerned.
* But instruction & data memory storage are larger.
* Doesn't resolve the tag until data lookup (sequential data-tag lookup)
	* Tag-followed-by-data:
		* Any data returned from the cache is going to be good data, there will be no tag mismatches (good)
		* Data comes out at the very last step, and whatever your pipelined cache does will have to be delayed by stages / cycles.
	* Data-followed-by-tag:
		* Downside
* Load-Delay Slots
	* Need one load-delay slot per load. 
	* Just an instruction
* Branch-Delay Slots
	* Need 3 branch-delay slots
	* 4-8 instructions / cycle, 25% are branches, so every instruction should be in a delay slot
* We don't use this because increased load latency means that the amount of LD slots needs to increase, which is impractical
* How would code be written?
```c
for (i = 0; i < N; i++) 
	A[i] = B[i] + C[i];
```
	becomes
```
loop:
fld f0, 0(x2)
fld f1, 0(x3)
fadd.d f2, f0, f1
fsd f2, 0(x1)
addi x1, x1, 8
addi x2, x2, 8
addi x3, x3, 8
bne x1, x4, loop // x4 holds the end
```
* Pipeline Scheduling
	* Same as above, except move the `addi`'s for `x2` and `x3` up above the `fadd.d`, move `fsd`  down to just above the branching op.
	* Here, the `fld`'s use a lot more latency, so parallelism is limited within a single loop operation.
* Loop Unrolling
	* Reduce the dynamic instruction count
	* Limited by the number of architectural registers
	* Increases the instruction cache footprint
	* More complex code generation for the compiler, as it has to understand pointers.
```
loop:
fld f0, 0(x2)
fld f1, 0(x3)
fld f10, 8(x2)
fld f11, 8(x3)
addi x3, x3, 16 // bump pointer
addi x2, x2, 16 // bump pointer
fadd.d f2, f0, f1
fadd.d f12, f10, f11
addi x1, x1, 16
fsd f2, -16(x1) // x1 points to A
fsd f12, -8(x1)
bne x1, x4, loop
```
* 

Later topics, which will be in a separate doc, include out-of-order pipelining
* Definition:
	* Fastest machine @ a given task
	* A device to turn compute-bound into i/o-bound problem
	* Any machine $30M+
	* Any machine designed by Seymour Cray
## CDC 6600 (1964)
* A machine designed by Seymour Cray
* A fast pipelined machine w/ 60-bit words
* 4 clocks to do fp add, 10 MHz
* Fastest machine in world for 5 years (until 7600)
	* Over 100 sold ($7-10M each)
### Architecture
- Separate instructors to manipulate three types of reg.
	- 8x60-bit data registers (`X`)
	- 8x18-bit address registers (`A`)
	- 8x18-bit index registers (`B`)
- All arithmetic & logic instructions are register-to-register
- Only load & store instructions refer to memory
### Some Terminology
* **Latency**: Time taken for a single operation from start to finish (initiation to usable result)
* **Bandwidth**: Rate of which operations can be performed
* **Occupancy**: Time during which the unit is blocked on an operation
### Issues in Complex Pipeline Control
* Structural conflicts @ execution stage if some FPU / memory unit is not pipelined, takes > 1 cycle.
* Structural conflicts at WB stage due to variable latencies of different functional units
* Out-of-order write hazards due to variable latencies of diff. functional units
	* What if a younger result gets written back instead of the desired result?
* Scoreboard
	* Instructions dispatched in-order to functional units provided no structural hazards or WAW
		* Stall on structural hazard, no functional units available
		* Only one pending write to any register
	* Instructions wait for input operands (RAW hazards) before execution
		* Can execute out of order
	* Instructions wait for output register to be read by preceding instructions (WAR)
		* Result held in functional unit until register free
## In-Order Superscalar Pipeline
* Fetch two instructions per cycle; issue both simultaneously if one is int/memory and other is floating point
* Inexpensive way of increasing throughput, examples include Alpha 21064 (1992) & MIPS R5000 Series (1996)
* At front end of the pipeline, we are doubling up on the fetch and the decode.
	* GPR's and FPR's. When we fetch two instructions s.t. one is an int op and one is a float op, then we send one to GPR and one to FPR so that we can compute things without any added area costs.
	* Other cases -- either give up or bump forward one. Prof isn't sure.
# Not Computer History

^650b99

I'm sorry, the previous section is a jumbled mess of word salad
- Three types of hazards:
	- Structural hazard: Instruction using a resource needed by another instruction
	- Data hazard: Instruction may depend on something produced by an earlier instruction for a data value
		- Among data hazards, we have multiple subcategorizations:
		* data-dependence: read-after-write. -- **only one that will show up in RISC 5-stage pipeline, because writes are the final stage of the pipeline.**
		```
		ADD x1, x2, x3
		SW x4, 4(x1)
		```
		- anti-dependence: write-after-read. why should we care?
		* in cases with multiple processes
		* out-of-order ISA's
		```
		ADD x1, x2, x3
		SUB x2, x4, x5
		```
		* output-dependence: write-after-write
		```
		ADD x1, x2, x3
		SUB x1, x4, x5
		```
	* solutions
		* interlock: wait until the hazard clears, or wait for some operand so it can be read
		* bypass: short-circuit the operand
		* speculate: guess the value, correct later if wrong
			* only effective in branching, sp updates, addr disambiguation
	* 
	- Control hazard: Same as above, but for a branch instruction.
- Exception Handling
	- a **precise exception** is one where you can recover the precise state when the exception happened
	- if there's let's say something overwritten to a register before the exception actually happens, then the resulting exception will be **imprecise**.
- Bypassing
	- allows for use of uncommitted instruction results by following instructions.
	- from the start / end of one pipeline stage to the start / end of another pipeline stage
		- cannot be end -> start or smth though
- Traps
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
