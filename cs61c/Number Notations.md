*Everyone knows binary to decimal, but have you also found ways to make it painful?*

# Unsigned
**Representable Range**: $[0, 2^n - 1$]
* Just ignores sign completely. 
	* Trying to make a negative number gives undefined results.
* However, still useful for nonnegative only metrics like length.
* But what if we wanted signs?

# Signed
There are multiple ways to process signed binary.

## Signed Magnitude
**Representable Range:** $[-2^{n-1} + 1, 2^{n-1} - 1]$
* One bit determining the sign, and then the rest of the bits just comprising integers as normal
* Arithmetic might be a little weird though

## Bias
**Representable Range:** $[B, 2^n - 1 + B]$. usually, $B < 0$.
* We have a "bias" which is essentially adding a certain negative bias $B$. This allows us to represent negative numbers as nonnegative numbers.
* We would have to have the bias externally, and the number itself would work similar to an unsigned

## 2's Complement
**Representable Range:** $[-2^{n-1}, 2^{n-1} - 1]$
* The first step is to create a bit that stores the sign (+ or -).
* Then, we need to make arithmetic compatible; that is, $a_2 + b_2 = (a+b)_2$.
* This is doable by essentially flipping all of the bits in the original number and then adding $1$ to the final result. We can verify that taking $x + -x$ where $-x$ is represented in two's complement negative notation results in a sum of $0$.

# Floating Point
There are four key components to a floating point number expressed in binary, at least by the IEEE-754 criteria:
1. The sign: This is just the first bit in the floating point number, determines... the sign!
2. The exponent: Stored with biased encoding. In the IEEE protocol, it is after the sign.
3. The mantissa: Stored as a number, which stores everything after the decimal point. We assume that there is a $1$ before everything in the mantissa; that is, it is $1.[\text{things in mantissa}]$.
4. The bias: $B$ is usually negative, and we add this to the exponent to find the actual exponent that we are going to multiply $1$ by.

Conversion from decimal to binary is done by converting the decimal to binary, and then using this conversion to find good values for the exponent and mantissa. 
* The power of the most significant bit is usually the exponent.
* The rest of the bits will become the mantissa.

## Denorms
What if you don't want the implicit 1?
1. Change the behavior of the exponent `0b000...0` such that its range extends to $0$.
2. So instead of `0b000...000` being equivalent to taking the exponent of the bias $B$, we instead make it a leading zero for $B+1$.
3. Decreases precision somewhat, but that's a price to pay for being able to represent numbers less than 1.

## Infinity
*But not the gates to it.*
If our exponents are all 1's, then our value is synonymous with infinity **ONLY IF** the mantissa is 0. Otherwise, our result is not a number.

# Endianness
*I don't care about little or big end, I just want it all to end. -me, 23:53 3/5/2023*
* *Big endian*: The big end is on the left, so the most significant byte is first and then it goes in decreasing order of significance.
* *Little endian*: The opposite, the least significant byte is first and it goes in increasing order of significance.
	* Important because this is how RISC-V and C get 32-bit or 64-bit integers from bytes.
