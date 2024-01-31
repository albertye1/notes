# pipelining
* Initially: The 5-stage pipeline!
	* Mostly to deal with higher latency load, float ops?
	* Branching, only if coming from another long latency operation.
* Pipeline depth = longest path from the fetch stage to the writeback stage.
* Deeper Pipelines - MIPS R4000
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
	fld f0, 0(x2)
	fld f1, 0(x3)
	fadd.d f2, f0, f1
	fsd f2, 0(x1)
	addi x1, x1, 8
	addi x2, x2, 8
	addi x3, x3, 8
	bne x1, x4, loop // x4 holds the end
	```
	* Here, the `fld`'s use a lot more latency than needed
	* Loop unrolling.
		* Somehow, it reduces the number of fld's. 
        * Check the slides later
* Machine capable limited

Later topics, which will be in a separate doc, include out-of-order pipelining
