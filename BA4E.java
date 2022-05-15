import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BA4E {
	 public static int[] AMINO_ACID_MASS = {57,71,87,97,99,101,103,113,114,115,128,129,131,137,147,156,163,186};

	    public static void main(String[] args) {
	        ArrayList<Integer> spectrum = new ArrayList<Integer>(Arrays.asList(0,113 ,114 ,128, 129 ,227 ,242 ,242 ,257, 355, 356 ,370 ,371, 484));
	        ArrayList<ArrayList<Integer>> result = sequencing(spectrum);

	        for (ArrayList<Integer> peptide : result) {
	            System.out.println(toStringWithDelimiter(peptide, "-"));
	        }
	    }

	    private static String toStringWithDelimiter(ArrayList<Integer> peptide, String delimiter) {
	        StringBuilder stringBuilder = new StringBuilder();
	        for (Integer integer : peptide) {
	            stringBuilder.append(integer);
	            stringBuilder.append(delimiter);
	        }
	        String stringWithDelimiter = stringBuilder.toString();
	        return stringWithDelimiter.substring(0, stringWithDelimiter.length() - 1);
	    }

	    private static ArrayList<ArrayList<Integer>> sequencing(ArrayList<Integer> spectrum) {
	        ArrayList<ArrayList<Integer>> candidates = new ArrayList<>();
	        candidates.add(new ArrayList<Integer>());

	        ArrayList<ArrayList<Integer>> output = new ArrayList<>();

	        while(!candidates.isEmpty()) {
	            candidates = expand(candidates);
	            output.addAll(check(candidates, spectrum));
	            candidates = trim(candidates, spectrum);
	        }
	        return output;
	    }

	    private static ArrayList<ArrayList<Integer>> expand(ArrayList<ArrayList<Integer>> candidates) {
	        ArrayList<ArrayList<Integer>> expandedCandidates = new ArrayList<>();
	        for (ArrayList<Integer> candidate : candidates) {
	            for (int i = 0; i < AMINO_ACID_MASS.length; i++) {
	                ArrayList<Integer> expandedCandidate = new ArrayList<>(candidate);
	                expandedCandidate.add(AMINO_ACID_MASS[i]);
	                expandedCandidates.add(expandedCandidate);
	            }
	        }
	        return expandedCandidates;
	    }

	    private static ArrayList<ArrayList<Integer>> check(ArrayList<ArrayList<Integer>> candidates, ArrayList<Integer> spectrum) {
	        ArrayList<ArrayList<Integer>> matchedCandidates = new ArrayList<>();
	        for (ArrayList<Integer> candidate : candidates) {
	            if(getSpectrum(candidate, false).equals(spectrum))
	                matchedCandidates.add(candidate);
	        }
	        return matchedCandidates;
	    }

	    private static ArrayList<Integer> getSpectrum(ArrayList<Integer> candidate, boolean isLinear) {
	        ArrayList<ArrayList<Integer>> subPeptides = isLinear ? getLinearSubPeptides(candidate) : getSubPeptides(candidate);
	        ArrayList<Integer> spectrum = new ArrayList<>();
	        spectrum.add(0);
	        for (ArrayList<Integer> subPeptide : subPeptides) {
	            int sumMass = 0;
	            for (Integer mass : subPeptide)
	                sumMass += mass;
	            spectrum.add(sumMass);
	        }
	        Collections.sort(spectrum);
	        return spectrum;
	    }

	    private static ArrayList<ArrayList<Integer>> getSubPeptides(ArrayList<Integer> candidate) {
	        ArrayList<ArrayList<Integer>> subPeptides = new ArrayList<>();
	        subPeptides.add(candidate);
	        ArrayList<Integer> extendedCandidate = new ArrayList<>(candidate);
	        extendedCandidate.addAll(candidate.subList(0, candidate.size() > 2 ? candidate.size() - 2 : 0));
	        for (int i = 0; i < candidate.size(); i++) {
	            for (int j = 0; j < candidate.size() - 1; j++) {
	                subPeptides.add(new ArrayList<>(extendedCandidate.subList(i, i + j + 1)));
	            }
	        }
	        return subPeptides;
	    }

	    private static ArrayList<ArrayList<Integer>> trim(ArrayList<ArrayList<Integer>> candidates, ArrayList<Integer> spectrum) {
	        ArrayList<ArrayList<Integer>> consistentCandidates = new ArrayList<>();
	        for (ArrayList<Integer> candidate : candidates) {
	            ArrayList<Integer> linearSpectrum = getSpectrum(candidate, true);
	            ArrayList<Integer> givenSpectrum = new ArrayList<>(spectrum);
	            boolean isConsistent = true;
	            for (Integer mass : linearSpectrum) {
	                if(givenSpectrum.contains(mass)) {
	                    givenSpectrum.remove(mass);
	                } else {
	                    isConsistent = false;
	                    break;
	                }
	            }
	            if(isConsistent)
	                consistentCandidates.add(candidate);
	        }
	        return consistentCandidates;
	    }

	    private static ArrayList<ArrayList<Integer>> getLinearSubPeptides(ArrayList<Integer> candidate) {
	        ArrayList<ArrayList<Integer>> subPeptides = new ArrayList<>();
	        for (int i = 0; i < candidate.size(); i++) {
	            for (int j = 0; j < candidate.size() - i; j++) {
	                subPeptides.add(new ArrayList<>(candidate.subList(i, i + j + 1)));
	            }
	        }
	        return subPeptides;
	    }
}
