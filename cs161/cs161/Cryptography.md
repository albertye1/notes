*Not cryptocurrency. That, unfortunately, comes later.*
* Three Goals of Cryptography
	* Confidentiality - adversary cannot *read* our messages
	* Integrity - adversary cannot *change* our messages w/o being detected
	* Authenticity - can *prove* that this message came from the person who claims to have written it
		* Integrity & authenticity are closely related properties
			* Before I can prove that a message came from a certain person, I have to prove that the message wasn't changed
		* But they aren't identical
* Keys
	* Most basic building block of any cryptographic scheme: the **key**
	* We can use the key in our algorithms to secure messages
	* Two models of keys:
		* **Symmetric Key Model:** Alice and Bob both know the value of the same secret key.
		* **Asymmetric Key Model**: Everybody has two keys, a secret and a public key.
			* Recall RSA from CS 70.
	* Kerckhoff's principle:
		* Cryptosystems should remain secure even when the attacker knows all internal details of the system
		* Key should be the **only** thing that's kept secret.
		* System should be designed to make it easy to change leaked keys
	* Assumption: Attacker knows the algorithm. The only info attacker misses is the secret key(s).
* Confidentiality: An adversary cannot read our message. Analogy is locking and unlocking the message.
	* If you need to pass an insecure channel, we put the message into a locked box, only to unlock it after it has passed into secure hands.
* Integrity (and authenticity)
	* Adversary cannot *change* messages without being detected.
	* Add a seal on the message, send it over the channel, and then remove seal w/ key when received.
	* Real cryptographic term is **tag**, don't use seal unless you want Peyrin to clown on your bitch ass
* Threat Models
	* What if Eve can do more than eavesdrop (between Alice -> Bob)?
	* Real world schemes are often vulnerable to more sophisticated attackers, so cryptographers have created more sophisticated threat models too
	* Some threat models:
		* **Ciphertext-only**
			* Eve can't trick Alice into encrypting messages or trick Bob into decrypting messages of Eve's choosing
		* **Chosen-plaintext**
			* Eve can trick Alice into encrypting messages but cannot trick Bob into decrypting
		* **Chosen-ciphertext**
			* Eve can only trick Bob into decrypting
		* **Chosen plaintext-ciphertext**
			* Eve can trick anyone.
* Symmetric-Key Encryption
	* The next few schemes are symmetric-key encryption schemes
		* Encryption schemes aim to provide confidentiality, only
		* Symmetric-key means alice and bob share the same secrete key that the attacker doesn't know
* What is a Confidentiality?
	* New propaganda documentary out now
	* "An adversary cannot read our messages" is not very specific.
		* What if Eve can read 1st half of Alice's message?
		* Or can figure out the words "Dear Bob"?
	* Also forgets prior knowledge.
	* Ciphertext should not give the attacker any **additional** information about the plaintext.
		* That's better.
		* So, now we design an experiment / security game to test our definition:
			* Eve chooses 2 messages $M_0, M_1$ of the same length.
			* Alice chooses one message $M_b$ at random, encrypts it, and sends the ciphertext.
			* Eve knows $M_b = M_0$ r $M_b = M_1$.
			* Eve reads the ciphertext and tries to guess which message was sent.
			* If probability that Eve correctly guesses which message was sent is $\frac{1}{2}$, **then** scheme is confidential.
	* IND-CPA
		* Eve may choose plaintexts to send to Alice and receives their ciphertexts.
		* Eve issues a pair of plaintexts $M_0$ and $M_1$ to Alice
		* It's insecure if a guess can wind the game with more than $\frac{1}{2}$ probability.
	* Edge Cases
		* Length
			* Cryptographic schemes are (usually) allowed to leak the length
## Ciphers
* Caesar
	* One of the earliest cryptographic schemes 
	* KeyGen(): choose a key $K$ randomly between 0 and 25.
	* Enc(K, M):
		* Replace each letter in M with the letter K positions later in the alphabet
		* if K = 3, plaintext DOG becomes GRJ
	* Dec(K, C):
		* Replace each letter in C w/ letter K positions earlier
	* How to break:
		* Domain knowledge
		* Check messages that make sense
		* Trick Alice into encrypting ABCD...Z
* Enigma
	* Done by machine
	* Breakable because letters never mapped to themselves
		* Also b/c Germans forgot:
			* Kerchhoff's Principle
			* Known plaintext attack
			* Chosen plaintext attack