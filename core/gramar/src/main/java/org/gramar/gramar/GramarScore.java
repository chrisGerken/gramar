package org.gramar.gramar;

import org.gramar.IGramar;

public class GramarScore implements Comparable<GramarScore> {

	private IGramar gramar;
	private Double score;
	
	public GramarScore(IGramar gramar, double score) {
		this.gramar = gramar;
		this.score = score;
	}

	@Override
	public int compareTo(GramarScore other) {
		return (-1) * this.score.compareTo(other.score);
	}

	@Override
	public String toString() {
		return gramar.getLabel() + " (" + score.intValue() + ")";
	}

	public IGramar getGramar() {
		return gramar;
	}

	public Double getScore() {
		return score;
	}

}
