* Useful for trying to detect some bit sequence in a larger sequence
* We can model this as a state graph
	* We start at one state, and then repeatedly progress through the states based on the input bit string.
	* Each state vertex has varying incoming edges, and **2** outgoing edges. 
		* Each edge from state `A` to state `B` has two values `X / Y` where `X` is the input that leads to this edge being traversed and `Y` is the output from traversing this edge.
	* We provide an output every time we traverse an edge, and we traverse one edge per input bit. Therefore, our input and output should have identical lengths. 