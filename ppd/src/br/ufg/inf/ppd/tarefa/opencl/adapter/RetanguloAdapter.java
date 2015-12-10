package br.ufg.inf.ppd.tarefa.opencl.adapter;

import org.jocl.struct.CLTypes.cl_float4;

import br.ufg.inf.ppd.estrutura.Retangulo;

public class RetanguloAdapter {

	private RetanguloAdapter() {

	}

	public static cl_float4 get(Retangulo retangulo) {

		cl_float4 floatsRetangulo = new cl_float4();

		floatsRetangulo.set(0, retangulo.getCantoInferiorEsquerdo().getX());
		floatsRetangulo.set(1, retangulo.getCantoInferiorEsquerdo().getY());
		floatsRetangulo.set(2, retangulo.getCantoSuperiorDireito().getX());
		floatsRetangulo.set(3, retangulo.getCantoSuperiorDireito().getY());

		return floatsRetangulo;
	}

	public Retangulo toRetangulo(cl_float4 retangulo) {

		return new Retangulo(retangulo.get(0), retangulo.get(1), retangulo.get(2), retangulo.get(3));
	}
}