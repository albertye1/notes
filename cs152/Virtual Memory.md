# TLB and Cache?
what if we wanted to access both the TLB and the cache?
- the virtual index $L$ is available without consulting the TLB
- let $L$ be index length, $b$ be offset length, and $k$ be the total virt. address length.
	- we now have three cases: $L + b = k$, $L + b < k$, $L + b > k$.
- if $L + b < k$, the TIO structure for a direct mapped cache works
- if $L + b = k$, we need to make the cache associative and use part of the VPN as the tag to map to PPN, as there's no tag
- if $L + b > k$, most of the time it will work. but that's not enough and we gotta get creative :
## L2 Cache
- if the L1 cache is extremely large, then $L > k - b$. Then, the address numbers will start to eat up the 
	- Consider the case where the suffix of the VPN is the same, the page offset is the same, the mapped PPN is the same, but the two VPN's are actually different. In this case, we can't store both $VA_1, VA_2$ in the cache.
- usually, a common L2 cache backs up both L1 I-cache and D-cache. 
	- L2 has a copy of any line in either L1.
	- ensures that there is only one copy of any physical data that lives in the L2 cache