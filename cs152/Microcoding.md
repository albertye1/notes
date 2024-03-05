* The goal of microcoding is essentially that of ROM in the 61C project -- to turn each assembly operation into some signals processable by the hardware.
* 
## Practices
- `MA` is the memory address, `A` and `B` are ALU registers
- `A := Mem` sets value of `A` to value of `MA`
- `Mem := A` sets memory value at `MA` equal to value of `A`