package br.ufg.inf.ppd.tarefa;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;
import org.magicwerk.brownies.collections.BigList;

import br.ufg.inf.ppd.estrutura.Juncao;
import br.ufg.inf.ppd.estrutura.Retangulo;

public class TarefaGPUOpenCL extends Tarefa {

	private static String PROGRAMA = new String();

	static {

		InputStream is = TarefaGPUOpenCL.class.getResourceAsStream("/br/ufg/inf/ppd/tarefa/tarefaGPUOpenCLKernel.cl");

		Scanner scanner = new Scanner(is);

		StringBuilder sb = new StringBuilder();

		while (scanner.hasNextLine()) {

			sb.append(scanner.nextLine());
			sb.append("\n");
		}

		scanner.close();

		PROGRAMA = sb.toString();
	}

	@Override
	protected Collection<Juncao> processarJuncao(List<Retangulo> reservas, List<Retangulo> queimadas) {

		List<Juncao> juncao = new BigList<Juncao>();

		float[][] pontosReservas = this.extrairArrayFloat(reservas);

		float[][] pontosQueimadas = this.extrairArrayFloat(queimadas);

		int[] resultado = new int[reservas.size() * queimadas.size()];

		Arrays.fill(resultado, -1);

		// __global float reservacix,
		// __global float reservaciy,
		// __global float reservacsx,
		// __global float reservacsy,
		// __global float queimadacix,
		// __global float queimadaciy,
		// __global float queimadacsx,
		// __global float queimadacsy,

		Pointer reservacix = Pointer.to(pontosReservas[0]);
		Pointer reservaciy = Pointer.to(pontosReservas[1]);
		Pointer reservacsx = Pointer.to(pontosReservas[2]);
		Pointer reservacsy = Pointer.to(pontosReservas[3]);

		Pointer queimadacix = Pointer.to(pontosQueimadas[0]);
		Pointer queimadaciy = Pointer.to(pontosQueimadas[1]);
		Pointer queimadacsx = Pointer.to(pontosQueimadas[2]);
		Pointer queimadacsy = Pointer.to(pontosQueimadas[3]);

		Pointer resultadop = Pointer.to(resultado);

		final int indiceDaPlataforma = 0;
		final long tipoDeDispositivo = CL_DEVICE_TYPE_ALL;
		final int indiceDoDispositivo = 0;

		CL.setExceptionsEnabled(true);

		int numeroPlataformas[] = new int[1];
		clGetPlatformIDs(0, null, numeroPlataformas);
		int numPlatforms = numeroPlataformas[0];

		cl_platform_id plataformas[] = new cl_platform_id[numPlatforms];
		clGetPlatformIDs(plataformas.length, plataformas, null);
		cl_platform_id plataforma = plataformas[indiceDaPlataforma];

		cl_context_properties propriedadesDoContexto = new cl_context_properties();
		propriedadesDoContexto.addProperty(CL_CONTEXT_PLATFORM, plataforma);

		int numeroDosDispositivos[] = new int[1];
		clGetDeviceIDs(plataforma, tipoDeDispositivo, 0, null, numeroDosDispositivos);
		int numeroDispositivo = numeroDosDispositivos[0];

		cl_device_id dispositivos[] = new cl_device_id[numeroDispositivo];
		clGetDeviceIDs(plataforma, tipoDeDispositivo, numeroDispositivo, dispositivos, null);
		cl_device_id dispositivo = dispositivos[indiceDoDispositivo];

		cl_context contexto = clCreateContext(propriedadesDoContexto, 1, new cl_device_id[] { dispositivo }, null, null, null);

		cl_command_queue linhaDeComando = clCreateCommandQueue(contexto, dispositivo, 0, null);

		// ALOCANDO NA MEMÓRIA OS DADOS DE ENTRADA/SAÍDA
		cl_mem parametros[] = new cl_mem[11];

		parametros[0] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * reservas.size(), reservacix, null);
		parametros[1] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * reservas.size(), reservaciy, null);
		parametros[2] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * reservas.size(), reservacsx, null);
		parametros[3] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * reservas.size(), reservacsy, null);

		parametros[4] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * queimadas.size(), queimadacix, null);
		parametros[5] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * queimadas.size(), queimadaciy, null);
		parametros[6] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * queimadas.size(), queimadacsx, null);
		parametros[7] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * queimadas.size(), queimadacsy, null);

		parametros[8] = clCreateBuffer(contexto, CL_MEM_READ_WRITE, Sizeof.cl_int * resultado.length, null, null);
		
		parametros[9] = clCreateBuffer(contexto, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int, Pointer.to(new int[] { reservas.size() } ), null);;

		// CRIANDO PROGRAMA ATRAVÉS DO CÓDIGO FONTE
		cl_program programa = clCreateProgramWithSource(contexto, 1, new String[] { TarefaGPUOpenCL.PROGRAMA }, null, null);

		// CONSTRUINDO O PROGRAMA
		clBuildProgram(programa, 0, null, null, null, null);

		// CRIANDO O KERNEL
		cl_kernel kernel = clCreateKernel(programa, "obterJuncoes", null);

		// DEFININDO OS ARGUMENTOS PARA O KERNEL
		clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(parametros[0]));
		clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(parametros[1]));
		clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(parametros[2]));
		clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(parametros[3]));
		clSetKernelArg(kernel, 4, Sizeof.cl_mem, Pointer.to(parametros[4]));
		clSetKernelArg(kernel, 5, Sizeof.cl_mem, Pointer.to(parametros[5]));
		clSetKernelArg(kernel, 6, Sizeof.cl_mem, Pointer.to(parametros[6]));
		clSetKernelArg(kernel, 7, Sizeof.cl_mem, Pointer.to(parametros[7]));
		clSetKernelArg(kernel, 8, Sizeof.cl_mem, Pointer.to(parametros[8]));
		clSetKernelArg(kernel, 9, Sizeof.cl_mem, Pointer.to(parametros[9]));

		// DEFININDO O TAMANHO DA DIMENSÃO
		long global_work_size[] = new long[] { reservas.size() * queimadas.size() };
		long local_work_size[] = new long[] { 1 };

		// EXECUTANDO O KERNEL
		clEnqueueNDRangeKernel(linhaDeComando, kernel, 1, null, global_work_size, local_work_size, 0, null, null);

		// LENDO OS DADOS DE SAÍDA
		clEnqueueReadBuffer(linhaDeComando, parametros[8], CL_TRUE, 0, resultado.length * Sizeof.cl_int, resultadop, 0, null, null);

		// Release kernel, program, and memory objects
		clReleaseMemObject(parametros[0]);
		clReleaseMemObject(parametros[1]);
		clReleaseMemObject(parametros[2]);
		clReleaseMemObject(parametros[3]);
		clReleaseMemObject(parametros[4]);
		clReleaseMemObject(parametros[5]);
		clReleaseMemObject(parametros[6]);
		clReleaseMemObject(parametros[7]);
		clReleaseMemObject(parametros[8]);
		clReleaseMemObject(parametros[9]);
		clReleaseKernel(kernel);
		clReleaseProgram(programa);
		clReleaseCommandQueue(linhaDeComando);
		clReleaseContext(contexto);

		for (int i = 0; i < resultado.length; i++) {

			if (resultado[i] == 1) {

				int rid = i / reservas.size();
				int qid = i % reservas.size();

				Retangulo a = reservas.get(rid);
				
				Retangulo b = queimadas.get(qid);
				
				if (a.intersect(b)) {
					
					juncao.add(new Juncao(a, b));
				}
			}
		}

		return juncao;
	}

	private float[][] extrairArrayFloat(Collection<Retangulo> retangulos) {

		float[] cix = new float[retangulos.size()];
		float[] ciy = new float[retangulos.size()];
		float[] csx = new float[retangulos.size()];
		float[] csy = new float[retangulos.size()];

		AtomicInteger atomicInteger = new AtomicInteger();

		atomicInteger.set(-1);

		retangulos.stream().forEach(retangulo -> {

			int indice = atomicInteger.incrementAndGet();

			cix[indice] = retangulo.getCantoInferiorEsquerdo().getX();
			ciy[indice] = retangulo.getCantoInferiorEsquerdo().getY();
			csx[indice] = retangulo.getCantoSuperiorDireito().getX();
			csy[indice] = retangulo.getCantoSuperiorDireito().getY();
		});

		return new float[][] { cix, ciy, csx, csy };
	}

	public static void main(String[] args) {

		System.out.println(Integer.MAX_VALUE);
	}
}