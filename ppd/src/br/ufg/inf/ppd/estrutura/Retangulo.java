package br.ufg.inf.ppd.estrutura;

import java.util.StringTokenizer;

public class Retangulo {

	private Ponto cantoSuperiorDireito;

	private Ponto cantoInferiorEsquerdo;

	public Retangulo(String descricao) {

		StringTokenizer token = new StringTokenizer(descricao, " ,");

		float inferiorx = Float.parseFloat(token.nextToken());
		float inferiory = Float.parseFloat(token.nextToken());
		float superiorx = Float.parseFloat(token.nextToken());
		float superiory = Float.parseFloat(token.nextToken());

		this.cantoSuperiorDireito = new Ponto(superiorx, superiory);
		this.cantoInferiorEsquerdo = new Ponto(inferiorx, inferiory);
	}

	public Retangulo(Ponto superior, Ponto inferior) {

		this.cantoSuperiorDireito = superior;
		this.cantoInferiorEsquerdo = inferior;
	}

	public Retangulo(float inferiorx, float inferiory, float superiorx, float superiory) {

		this.cantoSuperiorDireito = new Ponto(superiorx, superiory);
		this.cantoInferiorEsquerdo = new Ponto(inferiorx, inferiory);
	}

	public Ponto getCantoSuperiorDireito() {

		return cantoSuperiorDireito;
	}

	public void setCantoSuperiorDireito(Ponto superior) {

		this.cantoSuperiorDireito = superior;
	}

	public Ponto getCantoInferiorEsquerdo() {

		return cantoInferiorEsquerdo;
	}

	public void setCantoInferiorEsquerdo(Ponto inferior) {

		this.cantoInferiorEsquerdo = inferior;
	}

	public boolean intersect(Retangulo retangulo) {

		float r1px = this.getCantoInferiorEsquerdo().getX();

		float r2px = retangulo.getCantoInferiorEsquerdo().getX();

		float r1qx = this.getCantoSuperiorDireito().getX();

		float r2qx = retangulo.getCantoSuperiorDireito().getX();

		float r1py = this.getCantoInferiorEsquerdo().getY();

		float r2py = retangulo.getCantoInferiorEsquerdo().getY();

		float r1qy = this.getCantoSuperiorDireito().getY();

		float r2qy = retangulo.getCantoSuperiorDireito().getY();

		return !((r1px < r2px) && (r1qx < r2px) || (r2px < r1px) && (r2qx < r1px) || (r1py < r2py) && (r1qy < r2py) || (r2py < r1py) && (r2qy < r1py));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantoInferiorEsquerdo == null) ? 0 : cantoInferiorEsquerdo.hashCode());
		result = prime * result + ((cantoSuperiorDireito == null) ? 0 : cantoSuperiorDireito.hashCode());
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
		Retangulo other = (Retangulo) obj;
		if (cantoInferiorEsquerdo == null) {
			if (other.cantoInferiorEsquerdo != null)
				return false;
		} else if (!cantoInferiorEsquerdo.equals(other.cantoInferiorEsquerdo))
			return false;
		if (cantoSuperiorDireito == null) {
			if (other.cantoSuperiorDireito != null)
				return false;
		} else if (!cantoSuperiorDireito.equals(other.cantoSuperiorDireito))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "R(" + cantoSuperiorDireito + ", " + cantoInferiorEsquerdo + ")";
	}

}
