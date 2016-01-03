import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Anthill {
	
	//Ant[] ants;
	List<Ant> ants = new ArrayList<>();
	Graph graph;
	int node2VisitNumber;
	float pheromoneSpreadFactor; // = (float) 300.0;
	
	public Anthill(int antNumber, Graph graph)
	{
		node2VisitNumber = graph.getNodeNumber() / 2;
		this.graph = graph;
		//ants = new Ant[antNumber];
		for(int i = 0; i < antNumber; i++)
			//ants[i] = new Ant(graph.getStartNode());
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
				a.walk(node2VisitNumber, epoche);
				
			} catch (InternalException e) 
			{
				System.out.println(e.getMessage());
			}
		}
		for(Path p : graph.getPaths())
		{
			p.veporization();
		}
		ants = ants.stream().sorted((a1, a2 ) -> a1.compareTo(a2)).collect(Collectors.toList());
		int mean = ants.get(ants.size()-1).getDistance();
		for(Ant a : ants.subList(0, ants.size()/2))
		{
			a.spreadPheromone(pheromoneSpreadFactor, mean);
		}
		
	}
	
	private Ant getBestAnt()
	{
		Ant ant = ants.get(0);
		
		for(int i = 0; i < ants.size(); i++)
			if(ant.getDistance() > ants.get(i).getDistance() && ants.get(i).complete)
				ant = ants.get(i);
		return ant;
	}
	
	public void findPath(int maxEpoche)
	{
		for(int i = 0; i < maxEpoche; i++)
		{
			executeOneEpoche(i+1);
			if(i % 200 == 0)
				System.out.println(String.format("%d iter. : best distance: %d",
						i, getBestAnt().getDistance()));
		}
	}
	
	public String getBestPath()
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
		
		return factor;
	}

}
