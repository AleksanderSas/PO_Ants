import java.io.IOException;

import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) throws IOException, InternalException {
		System.out.println("Hello ants!");
		//Graph graph = new Graph("tests\\test1.txt");
		Graph graph = new Graph(30);
		graph.saveGraph2XML("ttt.xml");
		//System.out.println(graph.printDistnce());
		System.out.println("");
		
		Anthill anthill = new Anthill(280, graph);
		anthill.findPath(1200);
		System.out.println("");
		System.out.println(anthill.getBestPath());
		System.out.println(graph.getStartNode().toString());
		//System.out.println(anthill.getAllPaths());

	}

}
