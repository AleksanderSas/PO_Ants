import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello ants!");
		try {
			Graph graph = new Graph("tests\\test5.txt");
			//System.out.println(graph.printDistnce());
			System.out.println("");
			
			Anthill anthill = new Anthill(80, graph);
			anthill.findPath(1000);
			System.out.println("");
			System.out.println(anthill.getBestPath());
			System.out.println(graph.getStartNode().toString());
			//System.out.println(anthill.getAllPaths());
			
		} catch (DocumentException e) 
		{
			e.printStackTrace();
		} catch (GraphBuildException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
