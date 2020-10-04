package comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import devproblem.GrapeComponent;

public class GrapeComponentComparator implements Comparator<GrapeComponent> {

	private List<Comparator<GrapeComponent>> listComparators;

	@SafeVarargs
	public GrapeComponentComparator(Comparator<GrapeComponent>... comparators) {
		this.listComparators = Arrays.asList(comparators);
	}

	@Override
	public int compare(GrapeComponent o1, GrapeComponent o2) {
		for (Comparator<GrapeComponent> comparator : listComparators) {
			int result = comparator.compare(o1, o2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}
