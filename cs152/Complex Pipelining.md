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