# Memory Hierarchy
* Registers are fast to use and close to the CPU, but they're expensive (both in energy cost and in manufacturing cost), so we can only have a few of them per CPU
* DRAM is better-suited for storing kibibytes of data (larger than registers), but are slower to access
* The goal for today is to reduce memory access times
# Caching
* Library Analogy
	* Want to write a report using library books
	* Our desk at home is the CPU, we can only do work while at home
	* The library is DMEM, which has all of the information
	* Currently: when we need a memory address, we call the library and ask them to find our data
		* The librarians search the library, finds the book, and finds the line within the book we need
		* The librarians memorize the line, put the book back, and call us back with the line of data we need
	* Clearly, this process is slower the larger the library is. This is similar to the 
* How to speed things up?
	* **option 1:** While waiting for data, we switch to a different task.
		* CPU hyperthreading. While waiting for a DMEM access, perform a context switch and move to another thread
			* This is not very efficient usually, because context switches take a lot of time in comparison to the fetch time. 
			* However, hyperthreading (which increases number of threads) improves performance significantly
	* **option 2:** Plan ahead and ask for the data ahead of time, so we can use the data when we need to
		* Prefetching. Request data several cycles before you actually use the data
			* Similar to reordering operations to reduce hazards
			* Can be done manually with prefetching commands in C, but this is out of scope
			* If not done right, you can slow down your code.
	* **option 3:** Borrow some library books, which we feel we will be most likely to use in the future
		* Caching
			* Get a bookshelf at home that you can use to kep a few books, and borrow the book from the library when you read it
			* If we need another book, go back to the library and borrow a new one
			* You hope this collection of ~10 books at home is enough, despite it being a trivially small portion of books in the library
			* Similar to DP
		* Today, we'll focus on data caches, which are hardware components that store 1KiB to 1MiB of data
## Locality
* We need to predict what memory might be accessed next based on what memory was already accessed
* Temporal Locality
	* If a memory location is referenced then it'll probably be referenced again soon
	* Keep most recently accessed data items in our cache
* Spacial Locality
	* If a memory location is referenced, then the locations with nearby addresses probably will be referenced again soon
	* Keep closest data in our cache
## Blocks
* Move blocks consisting of contiguous words instead of just one word at a time.
	* Divide all memory into blocks of some size power of 2
	* Each memory access is a line in a book, each block is a book
* Each block will be assigned a number, called a tag
	* If we have 
Caches are an intermediate memory level between fastest and most expensive memory (registers) and the slower components (DRAM).
* Cache contains copies of data in memory that are being used
* Memory contains copy of data on disk that are being used
* Caches work on the principles of temporal or spacial locality
# Associativity

## Fully Associative Cache
* A *fully associative cache* is parameterized on two aspects: block size, number of blocks that can be stored
* When a memory access occurs:
	* Check if the cache contains the data needed -> return the data
	* If the data is not present in the cache, load the block of mem into the cache
	* Return the data
* Library analogy: Buy a bookshelf that can hold N books
	* Check if your bookshelf has the book you need
	* If book not there, get it from the library
	* Return the data
* Fully associative cache (with a 32B block)
	* Tag identifies the block
	* Valid bit is a 1-bit value that says which values are real data and which are trash
* Fully associative caches **DO NOT** have index bits. They only have tag and offset
### Tag-Index-Offset
* ![[Screenshot 2023-04-03 at 10.46.25 AM.png]]
* For exapmle: We have a system that uses 10-bit addresses, and our catch has 4 byte blocks and 4 byte storage.
	* To add address `0x3C1` = `1111000001`: Split into the tag and the offset
		* `11110000` | `01`: This is in the cache, and is a valid data point, so we look into the data at index 1, which is `0xDE`.
	* To add address `0x266` = `1001100110`: Split into the tag and the offset
		* `10011001` | `10`: This is in the cache but the valid bit is 0, so it's garbage and we will need to go back to the DMEM
* Cache terminology
	* When reading memory, 3 things can happen:
		* Cache Hit
			* Cache block is valid and has proper addres, so read the word
		* Cache Miss
			* Nothing is in the appropriate block of the catch, so fetch from memory and replace an invalid block
		* Cache Miss with Eviction
			* Cache miss, but the cache is already full of valid data, so we ned to remove one block from the cache before we can load the new block
	* A cache is considered **cold** if there's no valid data in it
* Cache timing
	* Compare the naive approach (just coing to the library) to a cache.
		* With a cache: we need to check our cache for data, so this still takes some time
		* There's a chance that we don't need to go to the library, which saves a lot of time. But this actually only happens sometimes, which limits the performance boost
		* The goal is then to keep the cache as hot as possible.
	* Since the cache is a hardware component, we are forced to use it for all programs
		* Therefore, if our program is not cache-efficient this can slow down the program instead of speeding it up
	* In practice, cache hits are around 10 cycles, while cache misses are around 100-1000 cycles, so we get a performance boost
	* Other variants
		* We have talked about only the fully-associative cache. Not ideal for all purposes; if we have N blocks we need N comparators, which is expensive.
		* Wednesday: Different types of caches that sacrifice hit rate for hit time, hit speed
# Misses
1. Compulsory Miss
	1. Haven't fetched the block
2. Capacity Miss
	1. Data evicted from the cache because there is not enough space inside the cache to add more data without evicting something
3. Conflict Miss
	1. Needs to update the cache to fit the tag
4. Coherency Miss
## Eviction Policy
* FIFO (first in first out): the first block in the cache to be added is evicted
* LRU (least recently used): the block in the cache that hasn't been used for the longest time is evicted.
	* This is better, because sometimes the first block in the cache is like a global variable which needs constant use, and removing it would just defeat the purpose of having the global variable in the cache to begin with