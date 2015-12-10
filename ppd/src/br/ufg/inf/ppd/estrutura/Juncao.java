package br.ufg.inf.ppd.estrutura;

public class Juncao {

	private Retangulo retanguloA;

	private Retangulo retanguloB;

	public Juncao(Retangulo retanguloA, Retangulo retanguloB) {

		this.retanguloA = retanguloA;
		this.retanguloB = retanguloB;
	}

	public Retangulo getRetanguloA() {

		return retanguloA;
	}

	public void setRetanguloA(Retangulo retanguloA) {

		this.retanguloA = retanguloA;
	}

	public Retangulo getRetanguloB() {

		return retanguloB;
	}

	public void setRetanguloB(Retangulo retanguloB) {

		this.retanguloB = retanguloB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((retanguloA == null) ? 0 : retanguloA.hashCode());
		result = prime * result + ((retanguloB == null) ? 0 : retanguloB.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Juncao other = (Juncao) obj;
		if (retanguloA == null) {
			if (other.retanguloA != null)
				return false;
		} else if (!retanguloA.equals(other.retanguloA))
			return false;
		if (retanguloB == null) {
			if (other.retanguloB != null)
				return false;
		} else if (!retanguloB.equals(other.retanguloB))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "J{" + retanguloA + ", " + retanguloB + "}";
	}

}
