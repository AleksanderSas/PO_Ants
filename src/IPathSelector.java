import java.util.List;

import javafx.util.Pair;

public interface IPathSelector {
	 Pair<Path, Float> selectPath(List<Path> paths, int epoche) throws InternalException;
}
