package br.ufg.inf.ppd.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.magicwerk.brownies.collections.BigList;

import br.ufg.inf.ppd.estrutura.Retangulo;

public class LeitorArquivos {

	public static List<Retangulo> obterRetangulosDoArquivo(File arquivo) throws IOException {

		List<Retangulo> retangulos = new BigList<Retangulo>();

		RandomAccessFile acessoAoArquivo = new RandomAccessFile(arquivo, "r");

		FileChannel canal = acessoAoArquivo.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(1024);

		StringBuffer linha = new StringBuffer();

		Retangulo retangulo = null;

		while (canal.read(buffer) > 0) {

			buffer.flip();

			for (int i = 0; i < buffer.limit(); i++) {

				char ch = ((char) buffer.get());

				if (ch == '\n') {

					retangulo = new Retangulo(linha.toString());

					retangulos.add(retangulo);

					linha = new StringBuffer();

				} else {

					linha.append(ch);
				}

			}

			buffer.clear();
		}

		canal.close();

		acessoAoArquivo.close();

		return retangulos;
	}
}