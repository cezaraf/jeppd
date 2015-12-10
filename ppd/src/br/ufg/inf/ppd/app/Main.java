package br.ufg.inf.ppd.app;

import br.ufg.inf.ppd.tarefa.Tarefa;
import br.ufg.inf.ppd.tarefa.TarefaCPU;

public class Main {

	public static void main(String[] args) {

		Tarefa tarefa = obterTarefa(args);

		tarefa.executar();
	}

	private static Tarefa obterTarefa(String[] args) {

		Tarefa tarefa = new TarefaCPU();

		if (args.length > 0 && args[0] != null) {

			try {

				tarefa = (Tarefa) Class.forName(args[0]).newInstance();

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return tarefa;
	}
}
