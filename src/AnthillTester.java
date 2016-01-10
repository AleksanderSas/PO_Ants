import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class AnthillTester 
{
	private BufferedWriter bw;
	private Graph graph;
	
	int[] antNoParam 	= new int[]{100, 200, 300, 400, 500, 600, 800, 1000, 1200, 1400};
	//int[] prunningParam = new int[]{19, 21, 23, 25, 27, 29, 31};
	int[] prunningParam = new int[]{1, 2, 5, 10, 15, 15, 35};
	float[] pheromonrExpParam = new float[]{(float) 1.0, (float) 2.0, (float) 2.7, (float) 3.5, (float) 4.0};
	//float[] pheromonrExpParam = new float[]{(float) 1.0, (float) 2.0, (float) 2.7, (float) 3.5, (float) 4.0};
	
	public AnthillTester(String outFileName, Graph graph) throws IOException
	{
		bw = new BufferedWriter(new FileWriter(outFileName));
		this.graph = graph;
	}
	
	public void close() throws IOException
	{
		if(bw != null)
			bw.close();
	}
	
	public void test()
	{
		for(int antNo : antNoParam)
		{
			Anthill anthill = new Anthill(antNo, graph);
			oneTest(anthill, antNo, FactorCentre.AntPrunningFactor, FactorCentre.pheromoneExpFactor);
			System.out.println("one test complete");
		}
		System.out.println("ant no tests complete");
		
		for(int pruning : prunningParam)
		{
			Anthill anthill = new Anthill(FactorCentre.antNumber, graph);
			oneTest(anthill, FactorCentre.antNumber, pruning, FactorCentre.pheromoneExpFactor);
			System.out.println("one test complete");
		}
		System.out.println("prunning no tests complete");
		
		for(float pherExp : pheromonrExpParam)
		{
			Anthill anthill = new Anthill(FactorCentre.antNumber, graph);
			oneTest(anthill, FactorCentre.antNumber, FactorCentre.AntPrunningFactor, pherExp);
			System.out.println("one test complete");
		}
		System.out.println("pheromonr factor tests complete");
	}
	
	private void oneTest(Anthill anthill, int ants, int prunning, float pheromonExpFactor)
	{
		int best  = Integer.MAX_VALUE;
		StringBuilder results = new StringBuilder();
		StringBuilder iters = new StringBuilder();
		
		String results_best = "";
		String iters_best = "";
		
		try 
		{
			
			writeHeader(ants, prunning, pheromonExpFactor);
			int current = Integer.MAX_VALUE;
			
			for(int k = 0; k < 5; k++)
			{
				graph.reset();
				current = anthill.findPath(FactorCentre.epocheNumber, FactorCentre.maxBestRepets, 
						(r, i) -> {results.append(r).append(", "); iters.append(i).append(", ");});
			}
			if(current < best)
			{
				//write down current values, and clean stringBuilders
				best = current;
				results_best = results.toString();
				iters_best = iters.toString();
				results.setLength(0);
				iters.setLength(0);
			}
			
			bw.write(iters_best);
			bw.newLine();
			bw.write(results_best);
			bw.newLine();
			bw.newLine();
						
		} catch (InternalException | IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void writeHeader(int ants, int prunning, float pheromonExpFactor) throws IOException
	{
		bw.write("ants: " + ants + " ");
		bw.write("prunning: " + prunning + " ");
		bw.write("Pheromon Factor: " + pheromonExpFactor + " ");
		bw.newLine();
	}
	
}