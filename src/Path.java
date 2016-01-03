import java.util.Collection;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

public class Path {
	private static Random generator = new Random(System.currentTimeMillis());
	private GraphNode node1;
	private GraphNode node2;
	private int distance;
	private float pheromone;
	
	public Path(int distance, GraphNode node1,GraphNode node2) 
	{
		super();
		this.node1 = node1;
		this.node2 = node2;
		this.distance = distance;
		pheromone = (float) 1.0;
	}
	
	public GraphNode getNodeFrom(GraphNode source)
	{
		return (node1 == source)? node2 : node1;
	}
	
	/*
	 * 	The function rands path with respect to pheromone
	 */
	public static Pair<Path, Float> randPath(List<Path> paths, int epoche) throws InternalException
	{
		float epocheFactor = FactorCentre.epocheFactor / epoche;
		float pheromonTmp = 0;
		float pheromoneSum = 0;
		float total = 0;
		for(Path p : paths)
			//pheromoneSum += p.pheromone * FactorCentre.pheromoneFactor + 1.0 + epocheFactor + 3000.0 / p.getDistance();
			pheromoneSum += p.pheromone / p.getDistance();
			//pheromoneSum = (float) (1.0 / p.getDistance());
		
		total = pheromoneSum;
		float random = generator.nextFloat();
		random *= pheromoneSum;
		
		pheromoneSum = 0;
		for(Path p : paths)
		{
			//pheromonTmp = (float) (p.pheromone * FactorCentre.pheromoneFactor + 1.0 + epocheFactor + 3000.0 / p.getDistance());
			pheromonTmp = p.pheromone / p.getDistance();
			//pheromonTmp = (float) (1.0 / p.getDistance());
			if(pheromoneSum + pheromonTmp >= random)
				return new Pair<Path, Float>(p, pheromonTmp / total);
			
			pheromoneSum += pheromonTmp;
		}
		
		throw new InternalException(String.format("path tossing, rand %f, max %f", random, pheromoneSum));
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public void addPheromone(float pheromone)
	{
		this.pheromone += pheromone;
	}
	
	public float getPheromon()
	{
		return pheromone;
	}
	
	public void veporization()
	{
		pheromone *= FactorCentre.veporizationFactor;
	}
}
