import java.util.Collection;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

public class Path implements Comparable<Path>{
	private static Random generator = new Random(System.currentTimeMillis());
	private GraphNode node1;
	private GraphNode node2;
	private float distance;
	private float pheromone;
	private float factorizedDistance;
	
	public Path(float distance, GraphNode node1,GraphNode node2) 
	{
		super();
		this.node1 = node1;
		this.node2 = node2;
		this.distance = distance;
		pheromone = (float) 1.0;
		factorizedDistance = (float) Math.pow(distance, FactorCentre.DistanceExpFactor);
		//factorizedDistance = 1;
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
			pheromoneSum += 1.0 * p.pheromone / p.factorizedDistance + epocheFactor;//p.getDistance();
			//pheromoneSum = (float) (1.0 / p.getDistance());
		
		total = pheromoneSum;
		float random = generator.nextFloat();
		random *= pheromoneSum;
		
		pheromoneSum = 0;
		for(Path p : paths)
		{
			//pheromonTmp = (float) (p.pheromone * FactorCentre.pheromoneFactor + 1.0 + epocheFactor + 3000.0 / p.getDistance());
			pheromonTmp = p.pheromone / p.factorizedDistance + epocheFactor;
			//pheromonTmp = (float) (1.0 / p.getDistance());
			if(pheromoneSum + pheromonTmp >= random)
				return new Pair<Path, Float>(p, pheromonTmp / total);
			
			pheromoneSum += pheromonTmp;
		}
		
		throw new InternalException(String.format("path tossing, rand %f, max %f", random, pheromoneSum));
	}
	
	public static Pair<Path, Float> getBestPath(List<Path> paths, int epoche) throws InternalException
	{
		Path best = paths.get(0);
		float sum = 0;
		for(Path p : paths)
		{
			sum += p.pheromone / p.factorizedDistance;
			if(p.pheromone / p.factorizedDistance > best.pheromone / best.factorizedDistance)
				best = p;
		}
		return new Pair<Path, Float>(best, best.pheromone / sum / best.factorizedDistance);
	}
	
	public float getDistance()
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
		pheromone = (pheromone-1) * FactorCentre.veporizationFactor + 1;
	}

	public void reset()
	{
		pheromone = (float) 1.0;
	}
	
	@Override
	public int compareTo(Path o) 
	{
		float cmpVal1 = pheromone / factorizedDistance;
		float cmpVal2 = o.pheromone / o.factorizedDistance;
		if(cmpVal1 < cmpVal2)
			return 1;
		return (cmpVal1 == cmpVal2)? 0 : -1;
	}
}
