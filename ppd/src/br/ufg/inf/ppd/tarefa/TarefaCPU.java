package br.ufg.inf.ppd.tarefa;

import java.util.Collection;
import java.util.List;

import org.magicwerk.brownies.collections.BigList;

import br.ufg.inf.ppd.estrutura.Juncao;
import br.ufg.inf.ppd.estrutura.Retangulo;

public class TarefaCPU extends Tarefa {

	@Override
	protected Collection<Juncao> processarJuncao(List<Retangulo> reservas, List<Retangulo> queimadas) {

		Collection<Juncao> juncoes = new BigList<Juncao>();

		for (Retangulo reserva : reservas) {
			
			for (Retangulo queimada : queimadas) {
				
				if (queimada.intersect(reserva)) {

					juncoes.add(new Juncao(reserva, queimada));
				}				
			}
		}
		
		return juncoes;
	}
}