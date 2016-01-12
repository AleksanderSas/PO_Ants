import java.io.IOException;

import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) throws IOException, InternalException, DocumentException, GraphBuildException {
		//System.out.println("Hello ants!");
		Graph graph = new Graph("tests\\test6.xml", 2);
		//Graph graph = new Graph(FactorCentre.graphSize);
		/*graph.saveGraph2XML("ttt.xml");*/
		//System.out.println(graph.printDistnce());
                /*Float f = new Float("2.000000000000000e+00");
                float f1 = (float) f;
                int f2 = (int) f1;
                System.out.println(f2);*/
		//System.out.println("");
		
		Anthill anthill = new Anthill(FactorCentre.antNumber, graph);
		anthill.findPath(FactorCentre.epocheNumber, FactorCentre.maxBestRepets, 
				(r, i) -> System.out.println(String.format("%d iter. : best distance: %d",i, r)));
		System.out.println("");
		System.out.println(graph.getStartNode().toString());
		System.out.println(anthill.getBestPath());
		//System.out.println(anthill.getAllPaths());*/
		
		//AnthillTester anthillTester = new AnthillTester("D:\\studia\\antTest2.txt", graph);
		//anthillTester.test();
		//anthillTester.close();

	}

}
