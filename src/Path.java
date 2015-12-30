import java.util.Collection;
import java.util.Random;

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
	}
	
	public GraphNode getNodeFrom(GraphNode source)
	{
		return (node1 == source)? node2 : node1;
	}
	
	/*
	 * 	The function rands path with respect to pheromone
	 */
	public static Path randPath(Collection<Path> paths) throws InternalException
	{
		float pheromonTmp = 0;
		float pheromoneSum = 0;
		for(Path p : paths)
			pheromoneSum += p.pheromone * FactorCentre.pheromoneFactor + 1.0;
		
		float random = generator.nextFloat();
		random *= pheromoneSum;
		
		pheromoneSum = 0;
		for(Path p : paths)
		{
			pheromonTmp = (float) (p.pheromone * FactorCentre.pheromoneFactor + 1.0);
			if(pheromoneSum + pheromonTmp >= random)
				return p;
			
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
	
	public void veporization()
	{
		pheromone *= FactorCentre.veporizationFactor;
	}
}
