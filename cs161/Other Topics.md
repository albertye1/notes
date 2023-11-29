Other topics: Malware, Tor, BTC
# Malware
* Attacker code running on victim computers that shouldn't be running on victim computers.
## Overview
* Viruses and worms both malware
* Virus: code that requires user action
* Worm: code that doesn't require user action
* Applications of malware: Botnets
	* Set of compromised machines that can do a ddos attack
## Virus
* Code that requires user action to propagate
	* Usually infects a computer by altering some stored code
* Strategies include to 
	* infect existing code that will eventually be executed by the user, i.e. code that runs when opening an app.
	* modify existing code
	* when running, a malcode will look for opportunities to infect more system
* Detection Strategies
	* Signature-based detection: Here's a pattern that's really common, if you see it it's a virus
		* If I see a virus once, I can note that this is indeed a virus
		* Capture a virus on one system and look for bytes corresponding to the virus code 
	* Antivirus
		* Usually includes a checklist of common values. When code is run, the AV checks against the signatures, and if familiar, it will flag as a virus.
* Polymorphic Code
	* Contains:
		* Decryptor Code
		* Key
		* Encrypted Virus Code
	* Can use different IV's, different keys. Encryption isn't for confidentiality but rather for the encrypted code to look different
	* Defenses:
		* The decryptor code is still a red flag. Everything still has the 
* Metamorphic Code
	* Each time the virus propagates, it generates a semantically different version of the code
	* Includes a code rewriter with the virus to change the code randomly each time
		* Renumber registers
		* Change order of conditional statements
		* Hopefully the signature detector doesn't notice by avoiding as many patterns as possible
	* Defense: Behavioral detection
		* Doesn't even look at the input to see if it's an attack
		* Need to analyze behavior instead of syntax
	* Defense: Flag Unfamiliar Code
		* It's impossible to write a perfect algorithm to separate malicious code from safe code.
		* A perfect algorithm reduces to the halting problem, which is unsolvable.
			* `if malicious: terminate` is like going to need to actually run through the code
		* Antivirus software instead looks for new, unfamiliar code
			* Keep a central repository of previously-seen code
			* If some code has never ben seen before, treat it as more suspicious
			* The central repository can store secure cryptographic hashes of previously-seen code snippets for efficiency
				* The software hashes code to see if the hash matches a hash in the repo
		* Flagging unfamiliar code is a powerful defense
			* You have a detector for malicious behavior -- e.g. signature detection
			* Now you also have a strategy for people avoiding your first detector
			* Thus, If code is unfamiliar, you're going to think it's suspicious. If code is familiar and malicious, then we already have defenses against that.
			* **Moral:** If you ever see code you've never seen before, you should really think twice before running it.
* Antiviruses are there to make money. So they market by arguing that variants of the same malware are "akchually 🤓🤓🤓" different software.
## Worms
* How does the worm find new users?
	* Randomly choose machines: generate a random 32-bit IP and try connecting to it.
	* Look for users on search apps like Google
	* Scanning: Look for targets, such as pre-generated hitlists or lists of users stored on infected hosts
* How does the worm force code to run?
	* Buffer overflows for code injection
	* A web worm might use an [[Internet#^eeb234]] vulnerability. 
	* Goal is to get code to run without the user doing anything.
* Model worm propagation
	* Worm propagation can be modeled as an infections epidemic.
	* Spread of the worm depends on: 
		* The size of the population
		* The proportion of the population vulnerable to infection
		* The number of infected hosts.
		* The contact rate
	* So this is quite similar to infectious disease analysis.
		* similarly, # of infected hosts grows logistically
# Tor
* Anonymity on the web
## Proxies
* Alice wants to send a message to Bob. But Bob shouldn't know that the message was from Alice
	* Moreover: An eavesdropper Eve cannot deduce that Alice is talking to Bob
	* Encrypt the message from Alice to Proxy, but also encrypt the identity of the intended recipient so the attacker has no idea who the message is meant for.
		* If we didn't care about eavesdroppers, we wouldn't need to do this encryption.
### VPN's
* A virtual connection to an internal network
* Allows access to an internal network through an encrypted tunnel.
	* Appear as though you're coming from the virtually connected network instead of your real network!
* So this is essentially a proxy
## Tor
* Send the packet through multiple proxies instead of just one proxy
* A network of many Tor relays for forwarding packets
	* Directory server: Lists all Tor relays and their public keys
* Threat model:
	* Security: Client anonymity and censorship resistance
		* Optional: Server anonymity with onion services
	* Performance: Reasonably low-latency. Obviously slower than normal connections
	* Tor protects against local adversaries
## Tor Circuit
* We basically just encrypt a bunch of stuff 
	* Sender queries the directory server and chooses 3 relays
	* Forms TLS connection with an entry node, then 2nd through the first, then 3rd through the 2nd, then connect to the exit node, then to recipient.
	* 1st relay knows Alice sent, 3rd knows sends to Bob, so to them it's all anonymous
* Exit Node
	* Can see the message and the recipient
	* The exit node, is then a man-in-the-middle attacker
	* Not a worry if [[Networking#^4a8bd7|TLS]] is used.
## Weaknesses
### Timing Attack
* A network attacker who has a full global view of the network can learn that Alice and Bob are talking
	* Exploit a timing attack: Observe when Alice sends a message, when Bob receives a message, and link the two together.
* Global adversaries aren't part of the threat model, and thus aren't protected against.
### Distinguishable Traffic
* You need your specs to be as commonplace as possible.
* Defense: Tor bridges
	* Entry nodes not viewable on any public list
## Tor In Practice
* Benefit: Free to use
	* Mostly funded by the US government
	* By using Tor, users "pay" by providing traffic for other users to hide in.
	* Drawback: Exit nodes are a man-in-the-middle attacker
		* However, your internet is ALSO full of MITMs so get fucked
	* Drawback: Performance
	* Drawback: Usability tradeoffs for full anonymity
* Tor for Censorship Resistance
	* Tor hides the sites a user is connecting to
	* Censors can easily block access to all public Tor entry points
		* Bridge services provide a set of unlisted entry points, so they can't be blocked by IP
	* Censors can block traffic that looks like Tor traffic
		* Pluggable transports make traffic look more like normal traffic
	* Censors can pretend to be a tor client to see if an endpoint is a Tor node.
	* Arms race b/w Tor and censors.
* Hosting Illegal Services on Tor
	* Tor onion services are often used for services widely considered illegal around the world.
		* Legitimate hosting services like Cloudflare will refuse to host these services
		* Most countries will take legal action against things
		* **SUPER** illegal -peyrin
	* Dark Markets: Marketplaces for illegal goods
		* Silk road -- libertarians and free market people start doing shit
		* You can buy drugs here
		* "Places I would advise staying away from... makes a lot of money these days" -peyrin
		* ppl scamming asw
	* Cybercrime forums: Websites for discussing illegal activities
# Bitcoin
* Should've been part of [[Cryptography]] but we out of time
* Sending and receiving money without trusting a central
## Identity and Transactions
* Trusted, public ledger.
## Bitcoin (Nick's version)
* Nick Weaver FUCKING HATES bitcoin