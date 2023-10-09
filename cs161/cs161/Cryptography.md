*Not cryptocurrency. That, unfortunately, comes later.*
# Notation
* In lecture notes, Alice & Bob are always sender and receiver, Eve is the eavesdropper, Mallory is man-in-the-middle.
# Basics
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

## Block Ciphers
* An encryption/decryption algorithm that encrypts blocks of a fixed size
* $E_k(M) = C$: Inputs a $k$-bit key $K$ and an $n$-bit plaintext $M$, outputs an $n$-bit ciphertext $C$.
* $D_k(C) = M$: Inputs a $k$-bit key, $n$-bit ciphertext $C$. Outputs an $n$-bit plaintext $M$.

### ECB
* $E(K, M) = C_1 + C_2 + \cdots + C_m$, where $m$ is number of blocks.
* Deterministic -- biggest problem
    * Once you find out the keys everything else is joever.

### CBC
* Cipher block chaining mode
    * Introduces randomness
    * XOR with the first plaintext, and then block-cipher that.
    * Use the previous ciphertext block to encrypt the next ciphertext block
        * Encryption: $C_0 = IV$
        * Decryption: $P_i = D_k(C_i) \oplus C_{i-1}$.

### CTR
* Slightly worse than CBC
    * Appends a nonce to the counter, to find which block is being encrypted
    * there is much information leakage thorugh IV reuse.
        * Encryption: $C_i = E_k(IV + i) \oplus M_i$
        * Decrytpion: $M_i = E_k(IV + i) \oplus C_i$
        * the $i$ is the counter post-nonce, and the $+$ here is string concatenation.

### IND-CPA Security
* A cipher is called IND-CPA secure if the attacker can't gain any info about the message given its ciphertext.

# MAC's 
* detect if something has been changed 

## Cryptographic Hashes
* kinda useful somewhere

# PRNG
*Pokemon Mystery Dungeon 5 when*
* Randomness is essential for symmetric-key encryption 
## Seed / Reseed 

## Security of PRNG's
* A PRNG cannot be truly random
    * Output is deterministic, given by seed. If seed is $s$ bits long, there are only $2^s$ output sequences.
    * Hence pseudorandom 
* A secure PRNG is computationally indistinguishable from random to an attacker. 
    * We use the same 
* Essentially: a PRNG is **secure** if an attacker cannot predict future thrown number 
* An example of insecure PRNG's is a broken slot machine. 
    * Someone figures out the seed and can predict when it's gonna throw a winning combo.
## Rollback Resistance 
* Definition: If the attacker learns the internal PRNG state, they can't learn anything about previous states/outputs 
    * Game: an attacker knows the current internal state of the PRNG and is given a sequence of truly random bits and a sequence of previous output from the PRNG.
    * We win if attacker cannot guess correctly w/ probability over $\frac{1}{2}$.
* Not required for a secure PRNG. 
    * But is good to have: If you generated the key with PRNG and then some other IV's with the same PRNG, then a man in the middle can just check for previous numbers and you're going to leak the key 
## HMAC-DRBG
* HMAC looks unpredictable. 
## Summary
* True randomness requires sampling a physical process, so it's really slow and biased. 
* PRNG uses a truly random seed to generate a lot of random-looking output. 
    * The seeding / reseeding process *SHOULD* make everything computationally indistinguishable
* CTR-DRBG: use block cipher in CTR mode to generate 
* HMAC-DRBG: use repeated applications of HMAC to generate pseudorandom bits.

## String Cipher
* Idea
	* Secure PRNG produces pretty much random output
	* An attacker who can't see the PRNG state can't learn any output
	* What if we used the PRNG output as a one-time pad?
* Protocol: Alice and Bob both seed a secure PRNG with their symmetric secret key, and then use the output as the key for a one-time pad.
* Randomized IV
* AES-CTR
	* Good because we can decrypt parts of the message without looking through the whole thing.

~~i aint one of the cosby's i won't go to~~
# Diffie-Hellman
*How do we find the keys?*
* Secure Color Sharing
	* Combine common paint + secret colors
	* Swap hands via public transportation process
		* assume it takes a long time to separate the mixture)
		* eavesdropper will know the disguised secret
	* Then, add secret color to received mix to get common secret.
* Discrete Log Problem
	* Every agrees on the public value, which is a large prime $p$, and a generator $g$. 
		* $g$ is specially chosen and we don't need to know anything more about it
	* **Discrete Log Problem** says: Given $g, p, g^a \pmod p$ for random $a$, it is computationally hard to find $a$.
	* **Diffie-Hellman Assumption**: Given $g, p, g^a \pmod p$, and $g^b \pmod b$ for random $a, b$, no polynomial time attacker can distinguish between $g^{ab} \pmod p$ and some random value.
* So, the analogous key exchange:
	* Alice, Bob = sender/recipient, Eve=eavesdropped
	* Alice generates $a$, Bob generates $b$.
	* Alice calculates $g^a \pmod p$ where $p$ public, and sends to Bob. 
	* Alice then receives $g^b \pmod p$. Then, we calculate $(g^b)^a \pmod p$. 
	* Bob performs all actions with swapped $a$ and $b$. 
	* So, we have the shared symmetric key of $g^{ab} \pmod p$.
* Ephemerality
	* Once we get $g^{ab}$, we can destroy $a$ and $b$. 
	* Benefit of DHE: Forward secrecy, which we'll see later. Will hyperlink when we get there.
* Security
	* If someone tampers with the Diffie-Hellman exchange, then they can cause recipients to get different keys that the man-in-the-middle know.
	* DHE is also an active protocol, so Alice and Bob need to both be online at the same time to exchange keys.
	* DHE doesn't provide authentication. 
* Elliptic-Curve Diffie-Hellman: Not really needed
# Public-Key Encryption
Three parts:
* KeyGen() -> PK, SK: generate a public/private keypair, where PK is public key and SK is secret key
* Enc(PK, M) -> C: Encrypt plaintext M using public key PK to produce ciphertext C
* Dec(SK, C) -> M: Decrypt ciphertext C to plaintext M.
Properties:
* correctness: Decrypting a ciphertext should result in the message that was originally encrypted
	* Dec(SK, Enc(PK, M)) = M for all PK, SK <- KeyGen() and M
* efficiency: should be fast to enc / dec
* Securrity: similar to IND-CPA, but sender just gives eavesdropped the public key, and eavesdropper doesn't request encryptions except for pair $M_0, M_1$.
	* This is secure key cryptography. Not needed.
## ElGamal Encryption
* Diffie-Hellman key exchange is great -- lets Alice and Bob share a secret over an insecure channel
* Problem: Diffie-Hellman can't send messages. The escret $g^{ab} \pmod p$ is random.
* protocol:
	* KeyGen():
		* Bob generates private key $b$, public key $B = g^b \pmod p$.
			* Intuition: Bob is completing his half of DH
	* Enc($B$, $M$):
		* Alice generates a random $r$ and computes $R = g \pmod p$.
			* Intuition: Alice completing her half of DH
		* Alice computes $M \times B \pmod p$.
			* Intuition: Alice derives shared secret and multiplies her message by secret
		* Alice sends $C_1 = R$, $C_2 = M \times B \pmod p$.
	* Dec($b$, $C_1$, $C_2$):
		* Bob computes $C_2 \cdot C_1^{-b} = M \cdot B \cdot R^{-b} = M \cdot g^b \cdot g^{-b} = M \pmod p$.
			* Use $B, C_1$ to generate
* What makes ElGamal secure?
	* It does just devolve into Diffie-Hellman.
	* Discrete log problem is hard, which means ElGamal is secure.
	* However, ElGamal is not IND-CPA secure. Adversary can send $M_0 = 0$ , $M_1 \neq 0$. Then we can beat the semantic game.
	* **Malleability**: can still mess with the message predictably.
		* Therefore, vulnerable to tampering
## RSA Encryption
zzzzz cs 70
# Digital Signatures
* If you want to sign message $M$:
	* First hash $M$
	* Then sign $H(M)$
* Why do digital signatures use a hash?
	* Allows signing arbitrarily long message