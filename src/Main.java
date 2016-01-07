import java.io.IOException;

import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) throws IOException, InternalException {
		System.out.println("Hello ants!");
		//Graph graph = new Graph("tests\\test1.txt");
		Graph graph = new Graph(FactorCentre.graphSize);
		graph.saveGraph2XML("ttt.xml");
		//System.out.println(graph.printDistnce());
		System.out.println("");
		
		Anthill anthill = new Anthill(FactorCentre.antNumber, graph);
		anthill.findPath(FactorCentre.epocheNumber, FactorCentre.maxBestRepets);
		System.out.println("");
		System.out.println(graph.getStartNode().toString());
		System.out.println(anthill.getBestPath());
		//System.out.println(anthill.getAllPaths());

	}

}
