package br.ufg.inf.ppd.tarefa;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.magicwerk.brownies.collections.BigList;

import br.ufg.inf.ppd.estrutura.Juncao;
import br.ufg.inf.ppd.estrutura.Retangulo;
import br.ufg.inf.ppd.io.LeitorArquivos;

public abstract class Tarefa {

	public void executar() {

		try {

			long inicio = System.currentTimeMillis();

			System.out.println("Carregando dataset de queimadas...");

			List<Retangulo> queimadas = LeitorArquivos.obterRetangulosDoArquivo(new File("/home/cezar/Downloads/ppd/queimadas.txt"));

			System.out.println(queimadas.size() + " retangulos de queimadas carregados.");

			System.out.println("Carregando dataset de reservas...");

			System.out.println("Realizando o particionamento...");

			File dataSetReservas = new File("/home/cezar/Downloads/ppd/reservas.txt");

			File pastaDataSet = dataSetReservas.getParentFile();

			String sufix = "." + Calendar.getInstance().getTime().getTime() + ".ppd.dataset";

			// String sufix = ".1449620254748.ppd.dataset";

			String comando = "split -l 1000000 --additional-suffix=" + sufix + " " + dataSetReservas.getAbsolutePath() + " " + dataSetReservas.getName();

			System.out.println(comando);

			Process process = Runtime.getRuntime().exec(comando, new String[0], pastaDataSet);

			process.waitFor();

			IOUtils.readLines(process.getInputStream()).forEach(System.out::println);

			File[] arquivos = dataSetReservas.getParentFile().listFiles((File file) -> file.getName().endsWith(sufix));

			List<Juncao> juncoes = processarSubArquivos(queimadas, arquivos);

			System.out.println(juncoes.size() + " junções detectadas..");

			System.out.println("Tempo de execução em minutos: " + ((System.currentTimeMillis() - inicio) / 1000 / 60));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	protected List<Juncao> processarSubArquivos(List<Retangulo> queimadas, File[] arquivos) {

		List<Juncao> juncoes = new BigList<Juncao>();

		Arrays.stream(arquivos).forEach(reservaDataSet -> {

			try {

				List<Retangulo> reservas = LeitorArquivos.obterRetangulosDoArquivo(reservaDataSet);

				System.out.println(reservas.size() + " retangulos de reservas carregados do dataset " + reservaDataSet.getName());

				juncoes.addAll(this.processarJuncao(reservas, queimadas));

				reservaDataSet.delete();

			} catch (Exception e) {

				e.printStackTrace();
			}

		});

		return juncoes;
	}

	protected abstract Collection<Juncao> processarJuncao(List<Retangulo> reservas, List<Retangulo> queimadas);
}
