package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulator {
	
	//dati in ingresso
	private int numAzioniSalienti;
	private Match m;
	private int numGiocatoriStart = 11;
	private int teamGiocatoreMigliore;
	
	//dati in uscita
	//il risultato lo deduco dalla mapGoal
	private int numEspulsi1;
	private int numEspulsi2;
	
	//modello del mondo
	Map<Integer, Integer> mapGoal; //tiene traccia dei goal fatti da ogni squadra
	
	//coda degli eventi
	PriorityQueue<Event> queue;
	
	public Simulator() {
		
	}
	
	public void init(int N, Match m, int teamGiocatoreMigliore) {
		this.numAzioniSalienti = N;
		this.m = m;
		this.teamGiocatoreMigliore = teamGiocatoreMigliore;
		
		this.numEspulsi1 = 0;
		this.numEspulsi2 = 0;
		
		this.mapGoal = new HashMap<>();
		this.mapGoal.put(m.getTeamHomeID(), 0);
		this.mapGoal.put(m.getTeamAwayID(), 0);
		
		this.queue = new PriorityQueue<>();
		for(int n=0; n<this.numAzioniSalienti; n++) {
			queue.add(new Event(n, numGiocatoriStart, numGiocatoriStart));
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int n = e.getN();
		int numGiocatori1 = e.getNumGiocatori1() - this.numEspulsi1;
		int numGiocatori2 = e.getNumGiocatori2() - this.numEspulsi2;
		
		if(n < this.numAzioniSalienti) {
			double random = Math.random();
			if(random < 0.5) {
				//GOAL: segna chi ha più giocatori in campo, se uguali segna chi ha il giocatore migliore
				if(numGiocatori1 > numGiocatori2) {
					this.mapGoal.put(this.m.getTeamHomeID(), this.mapGoal.get(this.m.getTeamHomeID())+1);
					
				} else if(numGiocatori1 < numGiocatori2) {
					this.mapGoal.put(this.m.getTeamAwayID(), this.mapGoal.get(this.m.getTeamAwayID())+1);
				
				} else {
					this.mapGoal.put(this.teamGiocatoreMigliore, this.mapGoal.get(this.teamGiocatoreMigliore)+1);
				}
				
			} else if(random < 0.8) {
				//ESPULSIONE
				if(Math.random()<0.6) {
					//espulso giocatore di teamGiocatoreMigliore
					if(teamGiocatoreMigliore == this.m.getTeamHomeID())
						this.numEspulsi1++;
					else
						this.numEspulsi2++;
					
				} else {
					//espulso giocatore dell'altra squadra
					if(teamGiocatoreMigliore == this.m.getTeamHomeID())
						this.numEspulsi2++;
					else
						this.numEspulsi1++;
				}
				
			} else {
				//INFORTUNIO: recupero -> 2 o 3 azioni salienti da aggiungere
				if(Math.random()<0.5) {
					for(int i=this.numAzioniSalienti; i<this.numAzioniSalienti+2; i++) {
						queue.add(new Event(i, numGiocatoriStart, numGiocatoriStart));
					}
					this.numAzioniSalienti += 2;
					
				} else {
					for(int i=this.numAzioniSalienti; i<this.numAzioniSalienti+3; i++) {
						queue.add(new Event(i, numGiocatoriStart, numGiocatoriStart));
					}
					this.numAzioniSalienti += 3;
					
				}
			}
		}
	}
	
	public int getNumEspulsi1() {
		return this.numEspulsi1;
	}
	
	public int getNumEspulsi2() {
		return this.numEspulsi2;
	}
	
	public int getGoalTeam1() {
		return this.mapGoal.get(m.getTeamHomeID());
	}
	
	public int getGoalTeam2() {
		return this.mapGoal.get(m.getTeamAwayID());
	}

}
