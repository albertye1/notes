- Interpolation of multithreaded and vector architectures.
- Types of parallelism
	- Instruction-Level Parallelism (ILP)
		- Superscalar, pipelining, VLIW
		- Easiest to program
	- Thread-Level Parallelism (TLP)
		- Multithreading, multiple cores
		- Most flexible -- can be used in the most situations
	- Data-Level Parallelism (DLP)
		- Vector, SIMD
		- Most efficient -- give a strong signal to the hardware that you can do a lot of work efficiently
- DLP was not widely adopted until GPU's
	- Now it'll account for more mainstream parallelism growth than TLP in the next decade, projected in Hennessy & Patterson
- What's good about GPU
	- Dedicated, fixed-function devices for generating 3D graphics, including high-performance FPU's
	- Rendering an array of pixels is completely parallel
- Nvidia CUDA -- allowed general-purpose programmability
## CUDA Example
C:
```c
void daxpy(int n, double a, double* x, double* y) {
	for (int i = 0; i < n; i++) 
		y[i] = a * x[i] + y[i];
}
```

CUDA:
```c
__host__ // piece run on host processor
int nblocks = (n + 255) / 255;
daxpy<<<nblocks, 256>>>(n, 2.0, x. y);

__device__ // piece run on gp-gpu
void daxpy(int n, double a, double* x, double* y) {
	int i = blockIdx.x * blockDim.x + threadId.x;
	if (i < n) y[i] = a*x[i] + y[i];
}
```
There's no loop here. For one value in $y$, we do an operation. For each microthread, we set the $i$ that corresponds to our block. 
## SIMT
GPU's use a single-instruction, multiple-thread model. Individual instruction streams for each CUDA thread are grouped together for SIMD execution on hardware (NVIDIA groups 32 CUDA threads into a *warp*).

Turns a scalar instruction stream into a vector-based hardware architecture.
### Implications
- All "vector" loads & stores are scatter-gather, as individual microthreads perform scalar loads and stores
	- GPU adds hardware to dynamically coalesce individual microthread loads and stores to mimic vector loads and stores.
- Every microthread has to perform stripmining calculations redundantly, as there is no scalar processor equivalent.
- Warps are multithreaded on core -- we have so many warps that are interleaved in execution that we can hide latency.
	- Similar to software pipelining and loop unrolling.

## Branch Divergence
- Hardware tracks which microthreads take or don't take branch
- If all go the same way, then keep going in SIMD fashion
- If not, create mask vector indicating taken/not-taken
- Execute not-taken first, then push taken and execute later
- When can execution of microthreads in warp reconverge?