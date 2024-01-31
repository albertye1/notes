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
*the stuff i say i'd "learn later" when i was learning just how linux worked*
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

### DHCP Spoofing
* works similarly to ARP spoofing: new member has no way of verifying the DHCP response.
* so sender usually only expects one DHCP server to respond, so they will accept 1st response
	* another **race condition** like ARP
* similarly, require attacker to be in the same LAN and allow attacker to be an MITM attacker.
	* attacker claims gateway router's address is actually their address
	* so any messages meant for the gateway router are sent to the MITM
	* attacker can also claim that the DNS address is their address, with similar results.
### Defenses
* It's actually hard to defend against so we don't
* Instead, we rely on defenses provided in higher layers...
## WPA
A bottom-layer protocol to communicate securely to a wireless local network.
* something with a passphrase being passed from client to server
* Wi-Fi: A layer 2 protocol that wirelessly connects machines in a LAN
	* Access Point: A machine that will help you connect to the network
	* SSID: The name of the Wi-Fi network
	* Password: Optionally, a password to secure WiFi communications
* WPA2 is a protocol for securing Wi-Fi network communications with cryptography
* Design goals:
	* Everyone with the Wi-Fi password can join the network
	* Messages sent over the network are encrypted with keys
	* An attacker who does not know the Wi-FI password cannot learn the keys
WPA Handshake
1. The client sends an authentication request to the access point
2. Both use the password to derive the `psk` (pre-shared key)
3. Both then exchange random **nonces**. 
4. Both use the `psk`, nonces, and MAC addresses to derive the `ptk` (pairwise transport keys)
5. Both exchange [[Cryptography#^9968c9|MICs]] (these are MACs from the crypto unit) to ensure no one has tampered with the nonces, and that the `ptk` was correctly derived
6. Access point encrypts and sends `gtk` (group temporal key) to the client, used for broadcasts that anyone can decrypt
7. Client acknowledges receiving `gtk`.

### WPA handshake, in summary

^556fab

* it's kind of intuitive given how you connect, but there are some authentication layers you don't just get based off of intuition.
-> Authentication Request
(derive `psk` from wifi password)
<- ANonce
(client derives `ptk` from `psk`, nonces, MAC addresses)
-> SNonce + MIC 
(server derives `ptk` from `psk`, nonces, MAC addresses)
<- MIC + `gtk`
-> ACK of receipt of `gtk`. 

### WPA-PSK Attacks
* **Rogue AP**: Pretend to be an AP, and offer your own ANonce to the client.
	* If you know the password / `psk`, you can complete the 4-way handshake with the client and become an MITM
* **Offline BF Attack**: People tend to choose bad passwords, and we can easily check whether or not our guess is correct
	* Nonces sent unencrypted, and client and AP MAC addresses are public
	* Eavesdropper guesses a password and derives:
		* password -> `psk`
		* `psk` + nonces + MAC addresses -> `ptk`
		* eavesdropper checks that MIC's match
* **No forward secrecy**: An eavesdropper who records the values of ANonce and SNonce can derive the key if they later learn the password or PSK.
	* Compare to DHE: An eavesdropper can't learn the key even if they record $g^a$, $g^b$ and compromise the computer
## WPA-Enterprise
* Core issue: Every client starts w/ same `psk` to derive the `ptk`
	* Fix: have each user use their own username and password instead!
* Instead of using a `psk`, use a randomly generated key by an authentication server
	* For your client to trust the auth server, you accept a digital certificate
	* Form a secure channel to the authentication server, which lets you enter your username and password
	* If the username and password are correct, the authentication server sends a one-time key to use instead of `psk`.
* Rest of the handshake proceeds similar to [[#^556fab|above]]
### WPA-Enterprise Attacks
* defends against the aforementioned attacks
* but it's still vulnerable to higher-layer attacks like arp or dhcp spoofing. 
* WPA is a layer 1 protocol and can't cover these attacks!
# TCP
## Ports
* Ports help us distinguish between different applications on the same computer or server.
* On private computers, port numbers can be random
* On public servers, the port number should be constant and well-known, so users can access the right port.
## 3-Way Handshake
* Client chooses initial sequence number x, its bytes and sends a SYN packet to the server
* Server chooses an initial sequence number y for its bytes and responds with a SYN-ACK packet
* Client then returns with an ACK packet
	* sequence number = y + 1, ACK number = x + 1, saying that it's acknowledged the packets with sequence number x + 1.
* Once both hosts have sync'd sequence numbers, then connection is "established".

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
	* first send from client to 
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
### Defenses
* Using TLS
* Use true random, unpredictable sequence numbers.
# UDP
* non-reliably sending packets
* only the ports
* same possibilities for attacks as TCP, but they're easier to pull off.
* why use?
	* much faster than TCP. usually used by low-latency, high-speed applications where errors are ok
	* i.e. video streaming, games

# TLS Handshake

^4a8bd7

* step 0: Standard TCP 3-way handshake 
* step 1: Hello
	* Client sends a random number
	* Server sends a random number, an encryption protocol, and a signed copy of the server's public key.
* step 2: Certificate
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
### Replay Attacks
* add record numbers in the encrypted TLS message. every message uses a unique RN, but if an attacker replays a message the number will be repeated
	* this means we can detect record numbers
* TLS record numbers are **not** TCP sequence number
	* record numbers are encrypted & used for security
	* while sequence numbers are unencrypted and are used for correctness in the layer belowso

So finally, we have **HTTPS:** [[Internet#^f055ef|HTTP]] over a TLS connection.
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
* DNS Lookup Walkthrough
	* `dig inorecurse eecs.berkeley.edu $198.41.0.4`. Get answer:
		* `opcode: QUERY, status: NOERROR, id, 26114, flags: qr: QUERY: 1, ANSWER: Q, AUTHORITY: 13, ADDITIONL: 27`.
		* If no records for `ANSWER`, and thus must point even lower on the DNS server z
## Cache Poisoning
* Returning a malicious record to the client
	* Victim will cache the bad DNS, thus "poisoning" the cache
* Defense: Bailiwick Checking
	* Limit the amount of damage a malicious name server can do
## DNS over TLS
* First, consider DNS over a TLS connection.
	* Our goal here is integrity, not confidentiality, since DNS records are public to begin with.
	* We want to prevent things like cache poisoning.
* Idea: TLS is end-to-end secure, so let's send all DNS requests and responses over TLS.
* Issues with this design
	* Performance - DNS should be fast, but TLS is slow
	* Caching - TLS doesn't help with caching
	* Security - Doesn't protect against malicious name servers
	* Security - Doesn't defend against malicious recursive resolvers.
		* Recursive resolves are practically MITM's
* **Object security** vs **Channel security**
	* Channel security is securing the communication channel that you're passing through. DNS over TLS does this
	* Object security is securing a piece of data in transit or storage
	* Secure DNS requires both **object** and **channel** security. 
## DNSSEC
A better solution involves a return to [[Cryptography]] topics.
* an extension of DNS protocol that ensures integrity on the results
A couple design questions for this protocol:
1) what kind of cryptographic primitive should we use to ensure integrity?
	1) prolly MAC's or digital signatures 
	2) digital signatures are better since we want *everyone* to be able to verify integrity, and not just one person.
2) How to ensure return record is correct? 
	1) Recall digital signatures: Only the owner of private key can sign records. Everyone else can verify
	2) Name server should sign record with their public key
	3) Should verify record with their public key
3) What should name server send to ensure integrity on a record?
	1) The record
	2) Signature over the record, signed with the private key
	3) The public key
	4) Issues with design?
		1) Name server can be malicious
		2) How to ensure no one's tampered with the public key?
		3) Solution is to use certificates.
4) How does a name server delegate trust to a child name server?
	1) Parent must sign child's pub key
5) PKI's need a trust anchor. Who do we implicitly trust in DNSSEC?
	1) We implicitly trust the top of the certificate hierarchy, which is the root name server.
### Design
* Sign records
	* Digital signatures provide integrity
	* Digital signatures defeat network attackers
	* Signatures can be cached with the records for object security.
* Public Key Infrastructure (Certificates)
	* Name servers can be arranged into a hierarchy, as in ordinary DNS.
	* Parents can delegate trust to children
	* Trust anchor: We implicitly trust the root nameserver
	* PKI defeats malicious name servers.
### Steps of a Lookup

# Denial-of-Service Attacks
## SYN flooding
denying myself the service of looking through this shit
# Firewalls
* Certain conditions for accepting or not accepting traffic going in and out of a certain server.
* A couple attributes of firewalls:
	* **Network-based**: Determines what traffic is allowed to enter or exit a network
	* **Host-based**: Determines what information can be sent to or from certain hosts in the network
* Firewalls all have some sort of policy, which can be customized.
	* **Default-Allow**: Defaults to allowing all traffic
	* **Default-Deny**: Defaults to denying all traffic
	* Essentially here, we have to pick between blacklisting suspicious traffic or whitelisting desired traffic.
	* On top of a default-allow or default-deny policy, certain rules can be added, depending on whether or not the firewall is **stateless** or **stateful**.
* Stateless Packet Filters
	* Have no history
	* All decisions must be made from the information of the packet
	* Can have trouble implementing complex policies with knowledge of history
	* Ex: `Allow inbound traffic in response to an outbound connection`.
		* Can be implemented in TCP by only allowing traffic with the ACK flag set.
			* If the internal computer sees an ACK packet without having formed a connection, it will drop packet / send RST.
		* Can't be implemented in UDP.
	* But in most cases, you're limited to just anything you can implement via knowledge from the ALC
		* So this is like sender, port, and whether or not it's UDP or TCP
* Stateful Packet Filters
	* Keep state in the implementation of the packet filter.
	* This allows us to check things like the intended recipient, the size, what type of request (http, ftp) the packet is, 
	* Downside is that u actually need to brain to see what to put the states as.
### Subverting Packet Filters
* Consider an example: Deny all connections containing the word "root"
	* Split the word across packets
	* Send the split packets out of order, to be reassembled by TCP by sequence number.
* IP Packets have a TTL, so using the TTL the attacker can find how many hops to a server. So, an attacker can send packets with a shorter TTL that go past the firewall, but not to the recipient.
	* This only works if the recipient is farther than the firewall.