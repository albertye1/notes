*clearly i came to college to do networking!*
# HTTP
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
* Parts of a cookie
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
		* **Note**: clicking a link makes a GET request, so the link can't directly make the malicious POSt request.
		* The link can open an attacker's website, which contains some JS that makes the actual malicious POST
	* Strategy #2: Put some JS on a website the victim will visit
		* Example: Pay for an advertisement on the website and put JavaScript in the ad.
* CSRF is one of the top 10 software weaknesses, despite the age of the attack.
* Can even perform CSRF on IoT devices, especially because these generally use default paswords.
	* Imagine your smart refrigerator gets hacked or smth that's gotta be depressing. This is why I'm not buying into all this smart x smart y stuff
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