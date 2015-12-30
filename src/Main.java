import org.dom4j.DocumentException;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello ants!");
		try {
			Graph graph = new Graph("tests\\test1.txt");
			System.out.println(graph.printDistnce());
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
