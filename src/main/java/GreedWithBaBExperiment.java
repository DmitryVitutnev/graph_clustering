import clusterers.BaBClusterer;
import clusterers.GreedClusterer;
import clusterers.GreedSortedAscClusterer;
import clusterers.GreedSortedDescClusterer;
import clusterers.IClusterer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreedWithBaBExperiment {

    public static void main(String[] args) throws IOException {

        List<Double> pList = Arrays.asList(0.33, 0.5, 0.66);

        List<IClusterer> clusterers = new ArrayList<IClusterer>();
        clusterers.add(new GreedClusterer());
        clusterers.add(new GreedSortedDescClusterer());
        clusterers.add(new GreedSortedAscClusterer());

        IClusterer etalon = new BaBClusterer();

        for (Double p : pList) {
            ClustererComparation.doExperiment("results_bab", p, 20, 100, clusterers, etalon);
        }

    }
}
