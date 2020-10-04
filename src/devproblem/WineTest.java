package devproblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import comparator.GrapeComponentComparator;
import comparator.PercentageComparator;
import comparator.RegionComparator;
import comparator.VarietyComparator;
import comparator.YearComparator;

public class WineTest {

	public static void main(String[] args) {

		Wine w = new Wine("11YVCHAR001", 1000);
		w.setDescription("2011 Yarra Valley Chardonnay");
		w.setTankCode("T25-01");
		w.setProductState("Ready for bottling");
		w.setOwnerName("YV Wines Pty Ltd");

		w.getComponents().add(new GrapeComponent(80D, 2011, "Chardonnay", "Yarra Valley"));
		w.getComponents().add(new GrapeComponent(10D, 2010, "Chardonnay", "Macedon"));
		w.getComponents().add(new GrapeComponent(5D, 2011, "Pinot Noir", "Mornington"));
		w.getComponents().add(new GrapeComponent(5D, 2010, "Pinot Noir", "Macedon"));

		printYearBreakdown(w);
		printVarietyBreakdown(w);
		printRegionBreakdown(w);
		printYearAndVarietyBreakdown(w);

	}

	private static void printVarietyBreakdown(Wine w) {
		List<GrapeComponent> list = sumPerProperties(new ArrayList<GrapeComponent>(w.getComponents()), "variety");
		Collections.sort(list, new GrapeComponentComparator(new PercentageComparator().reversed(),
				new VarietyComparator().reversed()));
		System.out.println("printVarietyBreakdown");
		list.forEach(grapeComponent -> System.out
				.println(grapeComponent.getPercentage() + "% - " + grapeComponent.getVariety()));
	}

	private static void printYearBreakdown(Wine w) {
		List<GrapeComponent> list = sumPerProperties(new ArrayList<GrapeComponent>(w.getComponents()), "year");
		Collections.sort(list,
				new GrapeComponentComparator(new PercentageComparator().reversed(), new YearComparator().reversed()));
		System.out.println("printYearBreakdown");
		list.forEach(grapeComponent -> System.out
				.println(grapeComponent.getPercentage() + "% - " + grapeComponent.getYear()));
	}

	private static void printRegionBreakdown(Wine w) {
		List<GrapeComponent> list = sumPerProperties(new ArrayList<GrapeComponent>(w.getComponents()), "region");
		Collections.sort(list,
				new GrapeComponentComparator(new PercentageComparator().reversed(), new RegionComparator().reversed()));
		System.out.println("printRegionBreakdown");
		list.forEach(grapeComponent -> System.out
				.println(grapeComponent.getPercentage() + "% - " + grapeComponent.getRegion()));
	}

	private static void printYearAndVarietyBreakdown(Wine w) {
		List<GrapeComponent> list = new ArrayList<GrapeComponent>(w.getComponents());

		Map<Integer, Map<String, Double>> groupBy = list.stream().collect(
				Collectors.groupingBy(GrapeComponent::getYear, Collectors.groupingBy(GrapeComponent::getVariety,
						Collectors.summingDouble(GrapeComponent::getPercentage))));
		
		List<GrapeComponent> listToSort = new ArrayList<GrapeComponent>();
		for (Entry<Integer, Map<String, Double>> entry : groupBy.entrySet()) {
			Map<String, Double> insideMap = entry.getValue();
			for (Entry<String, Double> insideEntry : insideMap.entrySet()) {
				listToSort.add(new GrapeComponent(insideEntry.getValue(), entry.getKey(), insideEntry.getKey(), ""));
			}
		}
		Collections.sort(listToSort, new GrapeComponentComparator(new PercentageComparator().reversed(),
				new YearComparator().reversed(), new VarietyComparator().reversed()));

		System.out.println("printYearAndVarietyBreakdown");
		listToSort.forEach(grapeComponent -> System.out.println(grapeComponent.getPercentage() + "% - "
				+ grapeComponent.getYear() + " " + grapeComponent.getVariety()));
	}

	private static List<GrapeComponent> sumPerProperties(List<GrapeComponent> grapeComps, String... properties) {
		Map<String, GrapeComponent> map = new HashMap<String, GrapeComponent>();
		for (GrapeComponent p : grapeComps) {
			GrapeComponent grape = null;

			if (properties[0].equalsIgnoreCase("variety")) {
				grape = map.computeIfAbsent(p.getVariety(), n -> new GrapeComponent(0d, 00, p.getVariety(), ""));
			} else if (properties[0].equalsIgnoreCase("year")) {
				String year = Integer.toString(p.getYear());
				grape = map.computeIfAbsent(year, n -> new GrapeComponent(0d, p.getYear(), "", ""));
			} else if (properties[0].equalsIgnoreCase("region")) {
				grape = map.computeIfAbsent(p.getRegion(), n -> new GrapeComponent(0d, 00, "", p.getRegion()));
			}

			grape.setPercentage(grape.getPercentage() + p.getPercentage());
		}
		return new ArrayList<>(map.values());
	}

}
