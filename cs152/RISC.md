* "iron law" of processor performance
	* $\dfrac{\text{time}}{\text{program}} = \dfrac{\text{inst}}{\text{prog}} \cdot \dfrac{\text{cycles}}{\text{inst}} \cdot \dfrac{\text{time}}{\text{cycles}}$.
	* instructions per program depends on source code, compiler technology, and ISA.
	* cycles per instruction depends on ISA and microarchitecture
		* microcoding - >1 CPI, short cycle
		* single-cycle unpipelined - 1 CPI, long cycle
		* pipelined - 1 CPI, short cycle
	* how to find: 
		* pipelined only counts after the initial stage
* Techniques that improve each term?
	* Instructions/Program
		* Better algorithms
		* Loop unrolling
		* Different programming languages
		* Complex instructions
		* Remove branch delay slots
	* CPI
		* Pipelining
		* Optimizing the microcode
		* Bypassing (forwarding)
		* Adding branch delay slots
		* Decoupling
		* Branch prediction
		* Prefetching (from instruction cache)
		* Essentially -- reducing # of flushes, wasted operations, etc
	* Cycle Time
		* Shorten the critical path
		* Minimize logic depth
		* In other words, just make the hardware less complex
		* RISC instead of CISC
		* Deeper pipelining
		* Different clock domains
	* Each action we perform is usually a tradeoff between these values.
	After this, we started talking about [[Pipelining]] and other possible methods for pipelining.
