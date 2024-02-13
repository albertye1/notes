# DRAM (dynamic)
- DRAM cells are a bunch of capacitors, that are very tall to increase the capacitance
	- One transistor for the world line, power stored in a capacitor
- Bits are stored in 2-dimensional arrays on chip
	- Rows are word lines, going from left to right
	- Columns are bit lines, going from bottom to top
	- Take in the $N+M$ address bits, where the upper bits are $N$ and lower bits are $M$.
		- Think of it as a bus that is $N+M$ bits wide.
	- The $N$ bits will down-**select the row** we want via **row address decoder**, and the $M$ bits will down-**select the column** we want via **column decoder & sense amplifier**.
- Modern chips have around 4-8 logical banks.
	- Each logical bank is physically 
### Packaging
* bitch ass professor rubbed fucking SALT in the WOUND by reminding me of fry's electronics, rest in peace.
* **DIMM**
	* DIMM (dual inline memory module) contains multiple chips w/ clock/control/address signals connected in parallel
	* Data pins work together to return wide word (e.g. 64-bit data bus using 16x4-bit parts).
* Apple M1
	* Two DRAM chips on same package as system SOC. 128b databus, running at 4.2Gb/s
	* 68 GB/s bandwidth
* DDR2 DRAM
	* At the start, send in a signal on the row. 
	* Turn onto the column signal
	* Maybe something in `DQ`, which can be used for either storage or loading
	* Be sure to precharge to reduce confusion
	* Go to row', which is next access
# SRAM (static)
- Doesn't prioritize density, but prioritizes minimizing access latency
- Two transistors, one for each bit line.
	- Two bit lines so precharge not needed
		- Still done on some SRAM but more intelligently
	- Either we'd have `01` for a 1 on the bit line, and a `10` for a 0 on the bitline
		- Our sense amp will be doing a much easier job here.
	- Writing wasn't covered in the lecture, but essentially in practice each of the **NOT** gates is a precisely sized transistor, such that charging both wires flips the state around.
- Cross-coupled inverters remove the need for refresh like on DRAM, as there is no logical inconsistency
# CPU-Memory Bottleneck
* Performance of high-speed computers is usually limited by memory bandwidth & latency
	* Latency -- time for a single access
	* Bandwidth -- number of accesses per unit time
* CPU speed is increasing 60%, while DRAM speed increasing 7%
* Larger cells (physically) also increase latency -- signals have further to travel, and fan out to more locations
## Management of Memory Hierarchy
- small/fast, e.g. registers
	- addr ususally specified in instr
	- generally implemented directly as a regfile
- large/slow, e.g. main memory
	- address usually computed from values in register
# Cache
* hardware-optimized hash table
* key -> hash function -> bin -> slots
	* bin = cache set
	* slot = cache way
	* 1 bin -> fully associative, 1 slot/bin -> direct mapped, $M$ slots / $N$ bins where $M, N > 1$ -> set associative
* fixed # of slots per bin, slots read out in parallel
### Replacement Policy
- In an associative cache, which block from a set should be evicted when set becomes full?
	- random
	- least-recently used
	- first-in first-out
	- not most-recently used
- second-order effect only matters when you miss!
	- you are trying to minimize the miss rate so usually hopefully replacement policy hopefully doesn't matter that much