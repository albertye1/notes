# Instruction Scheduling
- Data hazards due to register operands can be found at the decode stage, but data hazards due to memory dependence can only be found after computing the address.
- First, we must find all potential hazards.
	- Then, we can put anything together. 
# Out of Order Pipeline
* Tomasulo pipelining -- gets rid of WAR and WAW hazards!
	* Rename registers. 
		* Decode does register renaming and adds instructions to the issue-stage reorder buffer.
		* Any instruction in ROB without outstanding RAW hazards can be issued.
	* There's a sliding instruction window -- the next set of instructions that the program thinks it's going to execute but hasn't yet
	* When an instruction finishes operations, it broadcasts that it has a new tag / value `t1`.
		* Deallocate `t1`.
	* Younger instructions that are listening for an update will then replace their tag with the value from `t1`.
		* Replacing the tag by its value is an expensive operation
* Reorder Buffer Management
	* ROB is managed circularly
	* Instruction slot candidate for exec. when:
		* Holds valid instruction unit with set `use` bit
		* Execution hasn't started yet
		* Both operands are valid -- p1, p2 are set
	* Add entries to the table, there will a period before, while, and after execution.
		* After execution, the page table entries will still be there
* Traps?
	* Have in-order fetch and decode
	* Out of order reorder buffer & execution
	* Between execution and commit stage, check for traps
		* We can figure out where it happened because it's just the last operation before we flush the reorder buffer.
## Speculative Store Buffer
- During decode, store buffer slot allocated to program order
- Split a store operation into two steps: store address and store data. We only commit to the memory at the "store data" step.
- If data is in both store buffer & cache, use data in the store buffer
- If same address is in store buffer twice, use the youngest store that is older than the load.
Consider the following:
```
sd x1, (x2)
ld x3, (x4)
```
* Idea: In-Order Memory Queue
	- Execute all loads & stores in program order
		- Load and store cannot leave ROB for execution until all previous loads have completed execution
		- Can still execute loads/stores speculatively, and out-of-order with respect to other instructions
- Idea: Conservative OoO Load Execution
	- Can execute load before store, if addresses known and are not the same as the younger load address.
	- Each load address compared w addresses of all previous uncommitted stores
	- 
- Final: Address speculation
	- Speculate on younger load `x4`, and execute before the store address is known
	- Need to hold all completed by uncommitted load/store address in program order
	- If subsequently find that `x4 = x2`, then squash the load and all following instr.
		- So, the cost for an inaccurate speculation is very high even though the ideal runtime is faster
# Branch Prediction
If you have a 4-way superscalar with 8 pipeline stages from fetch to dispatch, an 80-entry TOB, and 3 cycles from issue to branch resolution, then a mispredict could throw away $8 \cdot 4 + 80 - 1 = 111$ instructions.

So having a good branch predictor is a good idea!
## Dynamic Branch Prediction
- Temporal correlation
	- The way a branch resolves may be a good predictor of the way it will resolve at the next execution
- One-Bit Branch History Predictor
	- For each branch, remember last way branch went
	- Has problem with loop-closing backward branches, as two mispredicts occur on every loop execution
		- first iter. predicts loop backwards branch not taken
		- last iter. predicts loop backwards branch taken (loop continued last time)
- Terminology
	- Branch Prediction Bits (BP bits)
	- Branch History Table (BHT)
	- Branch Target Buffer (BTB)
		- Keep branch & target PC in the BTB
		- PC+4 fetched if match fails
		- Only **taken** branches and jumps are held in the BTB
- Two-Level Branch Predictor
	- Use results from last two branches to select one of the four sets of BHT bits (~95% correct), the last two branches being a mux
- BHT flaws
- Jump Register (JR)
	- Switch statements (jump to address of matching case)
		- BTB works well if same case used repeatedly
	- Dynamic function call
		- BTB works well if same function usually called, e.g. in C++ when objects have same type in virtual function call
	- Subroutine returns (jump to return address)
		- BTB only works well if the function always returns to the same place -- which kind of defeats the purpose of functions!
- Out-of-order Branch Prediction
	- In-order issue
		- Speculative fetch but not speculative execution -- branch resolves before later instructions complete
		- Completed values hold in bypass network until commit
	- Out-of-order issue
		- 
- Out-of-order Mispredict Recovery
	- Rename Table Recovery
		- Have to quickly recover rename table on branch mispredict
	