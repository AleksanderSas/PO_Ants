import java.io.IOException;

import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) throws IOException, InternalException, DocumentException, GraphBuildException {
		//Wybierz plik do testowania
		Graph graph = new Graph("tests\\test6.xml", 2);

		
		Anthill anthill = new Anthill(FactorCentre.antNumber, graph);
		anthill.findPath(FactorCentre.epocheNumber, FactorCentre.maxBestRepets, 
				(r, i) -> System.out.println(String.format("%d iter. : best distance: %d",i, r)));
		System.out.println("");
		System.out.println(graph.getStartNode().toString());
		System.out.println(anthill.getBestPath());


	}

}
