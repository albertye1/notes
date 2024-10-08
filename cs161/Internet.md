*welcome to the internet*
# HTTP

^f055ef

## Cookies
* Customizing HTTP responses
	* HTTP is a request-response protocol, so the web server processes each request independently of other requests.
	* We're going to need to save some information, such as dark mode, which requires cookies.
* **Cookie:** a piece of data used to maintain state across multiple requests
	* Creating cookies
		* Server can create a cookie by including a `Set-Cookie` header in its response
		* JS in the browser can create a cookie
		* Users can manually create cookies in their browser
	* Storing cookie
		* Cookies stored in the browser, sometimes called a **cookie jar**
	* Sending cookies
		* Browser automatically attaches relevant cookies to every request
		* Server uses cookies to customize responses and connect related requests
* Parts of a cookie / Flags of note
	* Domain and Path
		* **domain attribute** and **path attribute** define which requests the browser should attach the cookie for
		* The **domain attribute** usually looks like the domain in a url (`toon.cs161.org`)
		* The **path attribute** usually looks like a path in the url. (`/xorcist`)
	* Secure and HttpOnly
		* The Secure attribute and HttpOnly attribute restrict the cookie for security purposes
		* Each attribute is either `True` or `False`
		* If the **Secure attribute** is `True`, then the browser only sends the cookie if the request is mdae over HTTPS
		* If the **HttpOnly attribute** is true, then JavaScript isn't allowed to send the cookie
	* Expires
		* The **Expires attribute** gives (usually) a timestamp defining when the cookie is no longer valid.
* Cookie Policy
	* Server shouldn't be able to set cookies for different websites!
		* Example: **evil.com** shouldn't be able to send a cookie to google.com!
	* Cookies also shouldn't be sent to the wrong websites. 
		* An authentication cookie for google.com shouldn't be sent to evil.com
	* **Cookie policy**: A set of rules enforced by the browser
	* Setting cookies
		* When the browser receives a cookie from a server, should the cookie be accepted?
		* Server w/ domain X can set a cookie with domain attribute Y if
			* The domain attribute is a domain suffix of the server's domain
				* X ends in Y
				* X is below or equal to Y on the hierarchy
				* X is more specific or equal to Y
		* Examples:
			* **mail.google.com** can set cookies for Domain=**google.com**.
			* **google.com** can set cookies for Domain=**google.com**
			* But, **google.com** *can't* set cookies for Domain=**com** because **com** is a top-level domain.
				* Reminder that the goal is so that **evil.com** can't try to add something to an http request to **google.com**
### Session Authentication
* **Session:** A sequence of requests and responses associated with the same authenticated user
* Naive solution: type user/password before each request. 
	* This is very inconvenient for the user
* Better solution: cookies!
	* **Intuition**: getting a wristband when entering a concert, so if you have to leave for some reason going back in will be easier
* **Session Token**: A secret value used to associated requests with an authenticated user
	* First time I visit the website:
		* Present username & password
		* If valid, server sends cookie w/ session token
		* Server associates you with the session token
	* When making future requests:
		* Browser attaches the session token cookie in your request
		* No need to reenter username / password
	* When log out / session times out:
		* Browser and server delete session token
## Cross Site Request Forgery
* What if attacker tricks the victim into making an unintended request? 
	* The victim's browser will automatically attach relevant cookies
	* The server will think the request came from the victim!
* **Cross-site request forgery** (CSRF): An attack that exploits cookie-based authentication to perform an action as the victim.
* How might we trick the victim into making a POST request?
	* Example POST request: Submitting a form
	* Strategy #1: Trick the victim into clicking a link
		* **Note**: clicking a link makes a GET request, so the link can't directly make the malicious POST request.
		* So instead, you'll have to specify the method you want to request if you're trying to make an exploit here.
		* The link can open an attacker's website, which contains some JS that makes the actual malicious POST
	* Strategy #2: Put some JS on a website the victim will visit
		* Example: Pay for an advertisement on the website and put JavaScript in the ad.
* CSRF is one of the top 10 software weaknesses, despite the age of the attack.
* Can even perform CSRF on IoT devices, especially because these generally use default paswords.
	* Imagine your smart refrigerator gets hacked or smth
### Defense: HTML Sanitization
* Replace HTML code with some escape codes, for example angle brackets, parentheses, slashes. So nothing that is actually in the URL can get compiled into web code.
### Defense: CSRF Tokens
* Example: HTML forms
	* Forms are vulnerable to CSRF
		* If victim visits the attacker's page, the attacker's JS can make a POST request with a filled-out form
* CSRF tokens are a defense for this
	* Secret value provided by server to the user. The user must attach the same value in the request for the server to accept it
	* CSRF tokens must be sent in a cookie, and are only valid for one or two requests.
### Defense: Referer Header
* In a CSRF attack, the victim usually makes the malicious request from a different request
* Referer header: A header in an HTTP request that indicates which webpage made the request
	* "referer" is a 30-year typo in the HTTP standard
	* If JavaScript on an attacker's website forces your browser to make a request, the Referer header for that request is the attacker's URl
	* If an `img` HTML tag on a forum forces your browser to make a request, the referer header for that request is the forum's URL
* Gives you some clue as to where your requests are actually coming from.
	* Allow *same-site requests,* but not cross-site requests
* Issues: 
	* May leak private information
	* Might be removed before request reaches the server
	* Optional
* SameSite Cookie Attribute
	* Implement a flag on a cookie that will make it unexploitable by CSRF attacks
	* Should be sent only when the domain of the cookie **exactly** matches the domain of origin
	* Issue: Not yet implemented on all browsers.

# Other Web Attacks
## XSS
* Relies on flaws in the Same-Origin Policy
	* Basically says that if the website has the same scheme, host and port. So this means using http:// or https:// when required, and an identical domain name. Different subdomains, however, are ok.
	* However, there are a few things that don't need to be of the same origin:
		* **JavaScript**: runs with the origin of the page that loads it
		* **Images**: have the origin of the pages they come from
		* **Frames**: have the origin of the retrieval URL
* Websites use untrusted content as control data
### Stored XSS
* Attacker's JS is stored on legit server and sent to browsers
* Classic example: Make a post on a social media
### Reflected XSS
* Attacker causes victim to input JS into a request
* Content is reflected in the server
* Requires the victim to click the link w/ JS

## Clickjacking
* Trick the victim into clicking on something from the attacker
* Main vulnerability: the browser trusts the user's clicks
	* When user clicks, browser assumes user intended to click
* Example
	* Fake DL buttons
	* Show user one frame when clicking on invis frame
	* Temporal attack: Change cursor
	* Cursorjacking: Fake mouse cursor
* Defenses
	* Enforce visual integrity; Focus the user's vision on the relevant part of the screen
	* Enforce temporal integrity: give user time to understand what they're clicking on
	* Ask the user for confirmation
	* Frame-busting: The legitimate website forbids other websites from embedding it in an iframe
## Phishing
* Trick victim into sending the attacker personal information
	* Defense: 2FA
		* Vulnerable to relay attacks
		* Vulnerable to social engineering attacks: Trick humans to subvert 2FA
		* Example: Authentication tokens for generating secure 2F codes
		* Example: Security keys
# SQL
*)': DROP TABLE websites; --*
* HTTP gets information between client and web server
	* But what if web server needs to call data from a db server?
* SQL Syntax Info
	* `SELECT`: Select certain data from a table
		* `SELECT *` gets everything.
		* can select rows or strings.
		* Example: `SELECT * FROM bots` selects all columns from the table `bots`.
	* `WHERE`: Filters out certain rows according to some condition. Similar to C but just remember `=` instead of `==` and `<>` instead of `!=`
	* `SET` sets all values to a new number
		* Can filter with `WHERE`
	* `DELETE` deletes all rows satisfying a `WHERE` condition.
	* `CREATE` creates a new table
	* `DROP TABLE` deletes a specified table
	* `UNION` works similar to a `;` in joining two commands
	* `--` comments out all following blocks of code

## SQL Injection
* Weird requests get appended in SQL as SQL, so it could be entered and hijack the backend code.
	* Protection: Escape sequences for critical characters that prevent SQL from executing in storage
	* Other protection: Parse a prepared SQL statement, and then insert the data
		* We will never parse any untrusted data
		* Not part of the SQL standard, so they rely on the actual SQL implementation to implement prepared statements.,
		* Must rely on API to correctly convert prepared statement into impl-specific protocol.
* Blind SQL injection:
	* Not all SQL queries are used in a visible way
	* Visible: shopping carts, comment threads, list of accounts
	* Blind: Password verification, account creation
	* Some SQL vulnerabilities only return T/F to determine whether your exploit worked!

# CAPTCHA
*verify ur not an evan bot*
* should be easy for humans but hard for robots to solve
* issues:
	* arms race: CAPTCHAs need to get harder as they're being trained to solve
	* accessibility: harder CAPTCHAs are harder for humans to solve
	* ambiguity: what if the validator doesn't know the solution either
	* not all bots are bad: CAPTCHAs can't distinguish good bots from bad bots
* attacks:
	* outsourcing attack -- paying humans to solve the CAPTCHA for you
		* so it just distinguishes attackers who want to pay vs attackers who don't