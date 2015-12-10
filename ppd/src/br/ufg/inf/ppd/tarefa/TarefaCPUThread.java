package br.ufg.inf.ppd.tarefa;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.magicwerk.brownies.collections.BigList;

import br.ufg.inf.ppd.estrutura.Juncao;
import br.ufg.inf.ppd.estrutura.Retangulo;

public class TarefaCPUThread extends Tarefa {

	@Override
	protected Collection<Juncao> processarJuncao(List<Retangulo> reservas, List<Retangulo> queimadas) {

		ExecutorService executor = Executors.newFixedThreadPool(10);

		Collection<Juncao> juncoes = new BigList<Juncao>();

		reservas.stream().forEach(reserva -> {

			executor.submit(() -> {

				queimadas.stream().forEach(queimada -> {

					if (queimada.intersect(reserva)) {

						juncoes.add(new Juncao(queimada, reserva));
					}
				});
			});
		});

		try {

			executor.shutdown();

			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return juncoes;
	}
}
