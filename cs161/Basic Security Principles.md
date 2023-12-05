* Know your threat model
	* Assume attacker can 
		* interact with systems without notice
		* know general information about systems
		* get lucky
		* coordinate complex attacks
		* has the resources to mount the attack
		* can and will obtain privileges if possible
* Consider human factors
	* If security system is hard to use, it won't be used
* Security is economics
	* If you're not a powerful target, you need less security
	* Don't put a $10 lock on a $1 item
* Detect if can't prevent
	* Always respond after detection too
* Defense in depth
	* Layer multiple defences together
* Least privilege
	* Consider what privileges are *needed* to perform actions properly.
	* Giving too much privilege usually not good
* Separation of responsibility
	* Consider requiring multiple parties to work together to exercise a privilege
* Ensure complete mediation
	* Ensure every access point is monitored and protected
	* Everything that people can think of to bypass people will
	* ToCTToU
		* Time of Check to Time of Use. Remember that there should be no vulnerabilities that can occur between when you check for vulnerabilities and when you actually perform actions that can be impacted by the vulnerability
* Don't rely on security through obscurity
	* **Shannon's Law**: the enemy knows the system
	* Assume the attacker knows what you're doing
* Use fail-safe defaults
* Design in security from the start
	* Do not think about how to make your code secure, think about how to write secure code
	* This is for you Microsoft. We'd update all our shit if you redesigned it to be secure
* Assume the attacker knows everything about the system.
	* That is, you can't use aspects of the system to provide some sort of defence

Course has 3 units:
1. [[Memory Safety Vulnerability]]
2. [[Cryptography]]
3. [[Networking]]