# Dynamic Address Translation
# Page Table
* program-generated (virtual or logical) address split into page num and offset.
* contains physical address of start of each fixed-size page in virt address space
* makes it possible to store large contiguous virtual memory given non-contiguous physical memory
* page tables, however, live in the memory.
	* simple page tables are too large so usually hierarchical page tables are used.
* but primary memory is limited, so 
## PPN, VPN
* introduced in 61C -- essentially, we have indices in the page table where pages are stored, and we also use the VPN to get the index in PTE and thus the PPN
	* VPN stores the index in the page table followed by the offset, although this depends on the question
* if there's a page table miss, then we get the next free page from the operating system
* if there's a page table hit,
## Virtual Memory with Multiple Levels
* sometimes, there's multiple levels to virtual memory
* then, we have PTP's going from one level to the next