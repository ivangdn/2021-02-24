package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event>{
	
	private int n; //azione
	private int numGiocatori1;
	private int numGiocatori2;
	
	public Event(int n, int numGiocatori1, int numGiocatori2) {
		super();
		this.n = n;
		this.numGiocatori1 = numGiocatori1;
		this.numGiocatori2 = numGiocatori2;
	}

	public int getN() {
		return n;
	}

	public int getNumGiocatori1() {
		return numGiocatori1;
	}

	public int getNumGiocatori2() {
		return numGiocatori2;
	}

	@Override
	public int compareTo(Event other) {
		return this.n - other.n;
	}
	
}
