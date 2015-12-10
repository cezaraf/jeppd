#pragma OPENCL EXTENSION cl_khr_global_int32_base_atomics : enable
#pragma OPENCL EXTENSION cl_khr_local_int32_base_atomics : enable

__kernel void obterJuncoes(	__global float *reservacix, 
							__global float *reservaciy,
							__global float *reservacsx,
							__global float *reservacsy,
							__global float *queimadacix,
							__global float *queimadaciy,
							__global float *queimadacsx,
							__global float *queimadacsy,
							__global int *resultado, 
							__global int *tamanho) { 

	int gid = get_global_id(0);
	
	int reservasid = gid / *tamanho;
	
	int queimadasid = gid % *tamanho;
	
	float r1px = reservacix[reservasid];
	float r1qx = reservacsx[reservasid];
	float r1py = reservaciy[reservasid];
	float r1qy = reservacsy[reservasid];

	float r2px = queimadacix[queimadasid];
	float r2qx = queimadacsx[queimadasid];
	float r2py = queimadaciy[queimadasid];
	float r2qy = queimadacsy[queimadasid];
	
	if (!((r1px < r2px) && (r1qx < r2px) || (r2px < r1px) && (r2qx < r1px) || (r1py < r2py) && (r1qy < r2py) || (r2py < r1py) && (r2qy < r1py))) {

		resultado[gid] = 1;
		
	} else {
	
		resultado[gid] = -1;
	}
}