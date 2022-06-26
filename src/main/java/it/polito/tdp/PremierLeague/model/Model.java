package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private List<Match> matches;
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	private int goalTeam1;
	private int goalTeam2;
	private int numEspulsi1;
	private int numEspulsi2;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
		dao.listAllPlayers(idMap);
	}
	
	public List<Match> getAllMatches() {
		if(this.matches==null)
			this.matches = dao.listAllMatches();
		
		return this.matches;
	}
	
	public void creaGrafo(Match m) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(m, idMap));
		
		for(Adiacenza a : dao.getArchi(m, idMap)) {
			if(a.getPeso()>=0) {
				Graphs.addEdge(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			} else {
				Graphs.addEdge(this.grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
			}
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public GiocatoreMigliore getGiocatoreMigliore() {
		if(this.grafo==null)
			return null;
		
		Player best = null;
		double deltaMax = -1000.0;
		
		for(Player p : this.grafo.vertexSet()) {
			double somma = 0.0;
			for(DefaultWeightedEdge oe : this.grafo.outgoingEdgesOf(p)) {
				somma += this.grafo.getEdgeWeight(oe);
			}
			for(DefaultWeightedEdge ie : this.grafo.incomingEdgesOf(p)) {
				somma -= this.grafo.getEdgeWeight(ie);
			}
			
			if(somma > deltaMax) {
				deltaMax = somma;
				best = p;
			}
		}
		return new GiocatoreMigliore(best, deltaMax);
	}
	
	private int getTeamGiocatoreMigliore(GiocatoreMigliore best) {
		return dao.getTeamGiocatoreMigliore(best);
	}
	
	public void simula(int N, Match m) {
		creaGrafo(m);
		Simulator sim = new Simulator();
		sim.init(N, m, getTeamGiocatoreMigliore(getGiocatoreMigliore()));
		sim.run();
		this.goalTeam1 = sim.getGoalTeam1();
		this.goalTeam2 = sim.getGoalTeam2();
		this.numEspulsi1 = sim.getNumEspulsi1();
		this.numEspulsi2 = sim.getNumEspulsi2();
	}
	
	public int getGoalTeam1() {
		return this.goalTeam1;
	}
	
	public int getGoalTeam2() {
		return this.goalTeam2;
	}
	
	public int getNumEspulsi1() {
		return this.numEspulsi1;
	}
	
	public int getNumEspulsi2() {
		return this.numEspulsi2;
	}
	
}
