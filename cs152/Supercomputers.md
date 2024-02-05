* Definition:
	* Fastest machine @ a given task
	* A device to turn compute-bound into i/o-bound problem
	* Any machine $30M+
	* Any machine designed by Seymour Cray
# CDC 6600 (1964)
* A machine designed by Seymour Cray
* A fast pipelined machine w/ 60-bit words
* 4 clocks to do fp add, 10 MHz
* Fastest machine in world for 5 years (until 7600)
	* Over 100 sold ($7-10M each)
## Architecture
- Separate instructors to manipulate three types of reg.
	- 8x60-bit data registers (`X`)
	- 8x18-bit address registers (`A`)
	- 8x18-bit index registers (`B`)
- All arithmetic & logic instructions are register-to-register
- Only load & store instructions refer to memory
## Some Terminology
* **Latency**: Time taken for a single operation from start to finish (initiation to usable result)
* **Bandwidth**: Rate of which operations can be performed
* **Occupancy**: Time during which the unit is blocked on an operation
## Issues in Complex Pipeline Control
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