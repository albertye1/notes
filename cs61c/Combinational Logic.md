## Basics
* We have an input and a clock. The input only updates when the clock is turned to 1. While the clock remains at 1 or is set to 0, nothing happens to the output.
	* There is also a **clock-to-queue time** which sets the amount of time between the clock turning on and the output stuff updating.
* If we have multiple registers chained together, the result after the first register becomes the input for the next.
	* This only happens at the next clock time, so it takes exactly one clock interval for the change to update, and it still goes by the state before the clock updates the previous thing.
* The **propagation time** is essentially the time it takes to update the output when passing through an operation or register.
	* i.e. the propagation time of an *OR* gate is essentially the time after the inputs get adjusted that the output will show any change.
## Constraints
* The **setup time** is the amount of time *before* a clock interval that the input must be stable / constant. Otherwise, the resulting output values that depend on the input will be undefined because of setup time violation.
* The **hold time** is the amount of time *after* a clock interval that the input must be stable / constant.
* $t_{clk-to-q} + t_{shortest-comp-path} \geq t_{hold}$
* $t_{clk-to-q} + t_{longest-comp-path} + t_{setup} \leq t_{clock-period}$
	* Using above relations, we can find the maximum hold time and the minimum clock period time.
* The combinational path is anything that happens 
## Other Timing Things
* Finding the computational paths of shortest and longest length is basically just going through all registers and gates, and checking the propagation time of them all. The shortest time to loop is the shortest computational path, and the longest time to loop is the longest computational path. 
	* clk to q, setup, and hold times not included.