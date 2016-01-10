import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class Anthill {
	
	//Ant[] ants;
	List<Ant> ants = new ArrayList<>();
	Graph graph;
	int node2VisitNumber;
	float pheromoneSpreadFactor;
	
	public Anthill(int antNumber, Graph graph)
	{
		node2VisitNumber = graph.getNodeNumber() / 2;
		this.graph = graph;;
		for(int i = 0; i < antNumber; i++)
			ants.add(new Ant(graph.getStartNode()));
		
		pheromoneSpreadFactor = computPheromoneSpreadFactor();
		//pheromoneSpreadFactor = (float) 1.0;
	}
	
	private void executeOneEpoche(int epoche)
	{
		for(Ant a: ants)
		{
			a.reset();
			try 
			{
				a.walk(node2VisitNumber, epoche, (p, e) -> Path.randPath(p, e));
				
			} catch (InternalException e) 
			{
				System.out.println(e.getMessage());
			}
		}
		for(Path p : graph.getPaths())
		{
			p.veporization();
		}
		
		//select subset of best ants 
		ants = ants.stream().sorted((a1, a2 ) -> a1.compareTo(a2)).collect(Collectors.toList());
		int range = 0;
		while(range < ants.size() && ants.get(range).complete)
			range++;
		//System.out.println("ok " + range + ",  bad: " + (ants.size() - range));
		if(range >= FactorCentre.AntPrunningFactor)
			range /= FactorCentre.AntPrunningFactor;
			//arnge = FactorCentre.AntPrunningFactor + (range - FactorCentre.AntPrunningFactor) / FactorCentre.AntPrunningFactor;

		int mean = 1;
		for(Ant a : ants.subList(0, range))
		//for(Ant a : ants)
		{
			a.spreadPheromone(pheromoneSpreadFactor, mean);
		}
		
	}
	
	// The method get best path, always best path is selected 
	private Ant getBestAnt() throws InternalException
	{
		Ant ant = ants.get(0);
		ant.reset();
		ants.get(0).walk(node2VisitNumber, 0, (p, e) -> Path.getBestPath(p, e));
		
		/*for(int i = 0; i < ants.size(); i++)
			if(ant.getDistance() > ants.get(i).getDistance() && ants.get(i).complete)
				ant = ants.get(i);*/
		return ant;
	}
	
	public int findPath(int maxEpoche,int maxBestRepets, IWritter writter) throws InternalException
	{
		int batch = 50;
		int best  = 0;
		int result = Integer.MAX_VALUE;
		int bestRepets = 0;
		for(int i = 0; i < maxEpoche && bestRepets < maxBestRepets; i++)
		{
			executeOneEpoche(i+1);
			if(i % batch == 0)
			{
				result = getBestAnt().getDistance();
				//System.out.println(String.format("%d iter. : best distance: %d",
					//	i, result));
				writter.write(result, i);
				if(result == best)
					bestRepets += batch;
				else
				{
					best = result;
					bestRepets = 0;
				}
			}
		}
		return result;
	}
	
	public String getBestPath() throws InternalException
	{
		Ant ant = getBestAnt();		
		return ant.toString();
	}
	
	public String getAllPaths()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < ants.size(); i++)
			sb.append("\n>>   ").append(i).append("   <<\n").append(ants.get(i).toString());
		return sb.toString();
	}
	
	public float computPheromoneSpreadFactor()
	{
		float factor = (float) 0.0;
		Collection<Path> paths = graph.getPaths();
		for(Path p : paths)
			factor += p.getDistance();
		factor /= paths.size() / graph.getNodeNumber() * 2;
		
		return (float) (factor / FactorCentre.PheromoneSpreadFactor);
	}

}
