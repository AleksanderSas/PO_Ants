import java.util.Collection;

public class Anthill {
	
	Ant[] ants;
	Graph graph;
	int node2VisitNumber;
	float pheromoneSpreadFactor; // = (float) 300.0;
	
	public Anthill(int antNumber, Graph graph)
	{
		node2VisitNumber = graph.getNodeNumber() / 2;
		this.graph = graph;
		ants = new Ant[antNumber];
		for(int i = 0; i < antNumber; i++)
			ants[i] = new Ant(graph.getStartNode());
		
		pheromoneSpreadFactor = computPheromoneSpreadFactor();
	}
	
	private void executeOneEpoche()
	{
		for(Ant a: ants)
		{
			a.reset();
			try 
			{
				a.walk(node2VisitNumber);
				
			} catch (InternalException e) 
			{
				System.out.println(e.getMessage());
			}
		}
		for(Ant a : ants)
		{
			a.spreadPheromone(pheromoneSpreadFactor);
		}
		
		for(Path p : graph.getPaths())
		{
			p.veporization();
		}
	}
	
	private Ant getBestAnt()
	{
		Ant ant = ants[0];
		
		for(int i = 0; i < ants.length; i++)
			if(ant.getDistance() > ants[i].getDistance())
				ant = ants[i];
		return ant;
	}
	
	public void findPath(int maxEpoche)
	{
		for(int i = 0; i < maxEpoche; i++)
		{
			executeOneEpoche();
			if(i % 50 == 0)
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
		
		for(int i = 0; i < ants.length; i++)
			sb.append("\n>>   ").append(i).append("   <<\n").append(ants[i].toString());
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
