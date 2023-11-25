*clearly i came to college to do networking!*
# Low Level Stuff
We can think of networks via the OSI model: A layered model of protocols.

1. Communication of bits
2. Local frame delivery
	* ethernet: the most common layer 2 protocol
	* MAC addresses: 6-byte addressing system used by ethernet
3. Global packet delivery
4. Transport of data
5. not mentioned
6. not mentioned
7. Apps and Services (the web)

We also have multiple types of attackers:

* Man-in-the-middle: Can modify or delete packets, and read packets. Mallory from the cryptography unit has made a comeback
* On-path atacker: Can read packets -- sort of like the eavesdropper from before.
* Off-path attacker: Cannot modify, delete, or read packets.

## ARP
* Occurs in layer 2 and 3 of this process.
* translate IP addresses to MAC addresses, assuming knowledge of only the former. 
* Assume Alice knows Bob's IP: `1.2.3.4`.
	* Broadcasts to everyone in the LAN: "What is the MAC address of `1.2.3.4`?"
	* If Bob is outside the LAN, the router will respond with its MAC address. So if Alice wants to send a packet to Bob, she sends it to the router.
	* The router can forward packet to other LANs to reach Bob.
	Alternatively, if ALice knows what addresses belong to the LAN, she will request the router's IP.
* All received ARP replies are cached, even if no request was sent.
* Bob responds by sending message only to Alice: My IP is `1.2.3.4` and my MAC address is `ca:fe:f0:0d:be:ef`.
### ARP Spoofing
* can send a message pretending to be somebody else's IP. Literally you can just say your IP is the received IP but use the wrong MAC address.
	* all types of attackers can do this, although it might be harder if you can't read or modify packets.
	* is also a race between the attacker and the legitimate user
* Now the sender has no way of verifying the ARP response, as they're only expending one machine to respond.
	* **Race condition**: As long as the attacker responds faster, the requester will accept the attacker's response.
* Required to be in the same LAN as the sender to pull off the attack, and now the attacker can be an MITM attacker!
### Defenses
* Use **switches** to avoid broadcasts and ARP requests.
* Anytime Alice wants to send a message to Bob, she sends the message to a switch w/ the cache of IP/MAC mappings.
	* Since everyone uses the switch its cache is larger than an individual user's cache
* If Bob's MAC address is in the cache, the switch sends the message directly to Bob
* Otherwise, the switch broadcasts the message
* Benefits:
	* security -- reduces the number of broadcasts
	* efficiency -- more requests sendable
	* isolation -- some switches can implement VLAN's, which split a LAN into several isolated parts.
* Can also use tools like `arpwatch` to track ARP responses for suspicious activity

## DHCP
* get configurations when first connecting to a network

Four steps:

1. **Client discover**: Client broadcasts a request for configuration
2. **DHCP Offer**: Any DHCP server can respond with a configuration offer.
	* Usually only one dhcp server responds
	* offer includes IP for client, IP of DNS, and gateway router's IP address
	* each offer also has an expiration time (how long the user can use this configuration)
3. **Client request:** The client broadcasts which config it has chosen.
	* If multiple dhcp servers made offers, the ones that weren't chosen discard their offer
	* the chosen dhcp server gives offer to client.
4. **DHCP acknowledgement:** The chosen server confirms that its configuration has been given to the client.

## WPA
* communicate securely to a wireless local network.
* something with a passphrase being passed from client to server

# TCP
## Ports
* Ports help us distinguish between different applications on the same computer or server.
* On private computers, port numbers can be random
* On public servers, the port number should be constant and well-known, so users can access the right port.
## 3-Way Handshake
* Client chooses initial sequence number x, its bytes and sends a SYN packet to the server
* Server chooses an initial sequence number y for its bytes and responds with a SYN-ACK packet
* Client then returns with an ACK packet
* Once both hosts have sync'd sequence numbers, then connection is "established"

## Sending and Receiving Data
* TCP handlers on each side track which TCP segments have been received for each connection
	* A connection is id'd by these 5 values, sometimes called a 5-tuple
		* Source IP
		* Dest IP
		* Source Port
		* Dest Port
		* Protocol
	* Data from the bytestream can be presented to the application when all data before has been received and presented.
* TCP uses packages to build connections, so it doesn't matter where each individual packet is going. So we can define a source and destination.
* So now, we can use the ACK number to **improve the 3-way handshake**.
	* If an ACK number is passed in, then it basically acknowledges that client/server has received everything up to a sequence number.
	* Then, the we set the sequence number to the old ACK number.
## TCP Flags
* ACK
	* indicator that the user is acknowledging the receipt of something (in ack num)
	* p much always set except for 1st packet
* SYN
* FIN
	* ends a connection
	* needs ACK
* RST
	* end a connection
	* no ACK
	* no longer sends or receives packets
## TCP Attacks
* Weird, we didn't talk about cryptography!
* This opens TCP protocol up to attacks.
* **TCP Hijacking**: Tampering with an existing session to modify or inject data into a connection
	* **Data Injection:** Spoofing packets to inject malicious data into a connection
		* Need to know: The sender's sequence number.
		* Easy for MITM and on-path attackers, but off-path attackers must guess 32-bit sequence number (called **blind injection/hacking**, considered difficult)
		* For on-path attackers, this becomes a race condition since they must beat server's legit response.
	* **RST Injection**: Spoofing a RST packet to forcibly terminate a connection.
		* Same reqs as packet injection, so easy for on-path and MITM attackers, but hard for off-path attackers
		* Often used in censorship scenarios to block access to sites.
# UDP
* non-reliably sending packets
* only the ports
* same possibilities for attacks as TCP, but they're easier to pull off.
* why use???
	* much faster than TCP. usually used by low-latency, high-speed applications where errors are ok
	* i.e. video streaming, games

# TLS Handshake

* step 1:
* step 2:
* step 3: Premaster Secret
	* rsa
		* client randomly generates a premaster secret (ps)
			* encrypts ps with the server's public key and sends it to the server
			* the client knows the server's public key from the certificate
		* the server decrypts the premaster secret, and then now share a secret.
		* proves that the server owns the private key, otherwise, it couldn't decrypt ps.
	* diffie hellman
		* generate secret $a$, and computes $g^a \pmod p$. 
		* server signs $g^a \pmod p$ with its private key, and sends the message & signature
		* client verifies signature, then generates a secret $b$ and computes $g^b \pmod p$.
		* client and server now share premaster secret $g^{ab} \pmod p$.
	* both of these strategies work. which one is better? diffie hellman.z
## Replay Attacks
* add record numbers in the encrypted TLS message. every message uses a unique RN, but if an attacker replays a message the number will be repeated
	* this means we can detect record numbers
* TLS record numbers are **not** TCP sequence number
	* record numbers are encrypted & used for security
	* while sequence numbers are unencrypted and are used for correctness in the layer belowso

So finally, we have **HTTPS:** HTTP over a TLS connection.
This is similar to 61C's comparch unit in terms of finally understanding up to the top-level of networking, and also similar in that I pretty much understood nothing.
# DNS
* need to reconcile IP address and domain names.
	* **note:** in a url, the domain name doesn't include any file specs or protocols.
* **DNS**: An internet protocol for translating human-readable domain names to IP address.
* name servers
	* A server on the internet responsible for answering DNS requests
		* Name servers have domain names and IP addresses too
		* ex: Domain `a.edu-servers.net` with IP `192.5.6.39` is a name server
	* To perform a lookup, send a *DNS query* (i.e. what is the IP address of `www.google.com`?)
	* The name server then sends a *DNS response* with the answer (i.e. "the IP address of `www.google.com` is `74.125.25.99`").
	* issues:
		* one name server won't be able to handle every DNS request from the entire internet
		* If there are many name servers, how do you know which to contact?
* Steps of a DNS lookup
	* Start at the root server `.`
	* Then, we start with TLD's, such as `.edu` or `.org`.
	* And then keep going down to, for example, `berkeley.edu`, `mit.edu`, `cs161.org`.
* DNS uses UDP, as it is designed to be lightweight and fast. 
	* Any access that involves a domain name is preceded by a DNS query, so we want DNS lookups to be fast.
	* Can fit queries and responses in just one UDP message -- DNS payload.
		* Z
* DNS Lookup Walkthrough
	* `dig inorecurse eecs.berkeley.edu $198.41.0.4`. Get answer:
		* `opcode: QUERY, status: NOERROR, id, 26114, flags: qr: QUERY: 1, ANSWER: Q, AUTHORITY: 13, ADDITIONL: 27`.
		* If no records for `ANSWER`, and thus must point even lower on the DNS server z
## Cache Poisoning
* Returning a malicious record to the client
	* Victim will cache the bad DNS, thus "poisoning" the cache
* Defense: Bailiwick Checking
	* Limit the amount of damage a malicious name server can do