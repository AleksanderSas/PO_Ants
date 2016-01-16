import java.util.ArrayList;
import java.util.List;

public class Anthill_subrun implements Runnable 
{
	List<Ant> ants;
	int node2VisitNumber;
	int epoche;
	
	
	
	public Anthill_subrun(List<Ant> ants, int node2VisitNumber, int epoche) {
		super();
		this.ants = ants;
		this.node2VisitNumber = node2VisitNumber;
		this.epoche = epoche;
	}
	
	public void nextEpoche()
	{
		epoche++;
	}

	@Override
	public void run() {
		for(Ant a: ants)
		{
			a.reset();
			try 
			{
				a.walk(node2VisitNumber, epoche, (p, e) -> Path.randPath(p, e));
				
			} catch (InternalException e) 
			{
				System.out.println(e.getMessage());
			}
		}	
	}
	
}
