package br.ufg.inf.ppd.estrutura;

public class Ponto {

	private float x;

	private float y;

	public Ponto(float x, float y) {

		this.x = x;
		this.y = y;
	}

	public float getX() {

		return x;
	}

	public void setX(float x) {

		this.x = x;
	}

	public float getY() {

		return y;
	}

	public void setY(float y) {

		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Ponto other = (Ponto) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "P[" + x + ", " + y + "]";
	}
}
