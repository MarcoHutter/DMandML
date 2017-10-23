package com.github.TKnudsen.DMandML.model.supervised.classifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.DMandML.data.classification.IProbabilisticClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.data.classification.LabelDistributionTools;
import com.github.TKnudsen.DMandML.data.classification.ProbabilisticClassificationResult;

/**
 * <p>
 * Title: EnsembleClassifier
 * </p>
 * 
 * <p>
 * Description: orchestrates a series of classifiers.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 J�rgen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class EnsembleClassifier<O extends Object, FV extends AbstractFeatureVector<O, ? extends Feature<O>>> extends Classifier<O, FV> {

	private Collection<Classifier<O, FV>> classifierEnsemble;

	@SuppressWarnings("unused")
	private EnsembleClassifier() {
	}

	public EnsembleClassifier(Collection<Classifier<O, FV>> classifierEnsemble) {
		this.classifierEnsemble = classifierEnsemble;
	}

	@Override
	protected void initializeClassifier() {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.initializeClassifier();
	}

	@Override
	protected void prepareData() {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.prepareData();
	}

	@Override
	protected void buildClassifier() {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.buildClassifier();
	}

	@Override
	protected void resetResults() {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.resetResults();
	}

	@Override
	public void train(List<FV> featureVectors, List<String> labels) {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.train(featureVectors, labels);

		refreshLabelAlphabet();
	}

	@Override
	public void train(List<FV> featureVectors, String targetVariable) {
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble)
				classifier.train(featureVectors, targetVariable);

		refreshLabelAlphabet();
	}

	private void refreshLabelAlphabet() {
		Set<String> labelAlphabet = new HashSet<>();
		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble) {
				List<String> labels = classifier.getLabelAlphabet();
				if (labels != null)
					labelAlphabet.addAll(labels);
			}

		if (labelAlphabet == null || labelAlphabet.size() == 0)
			this.labelAlphabet = null;
		else
			this.labelAlphabet = new ArrayList<>(labelAlphabet);
	}

	@Override
	public List<String> test(List<FV> featureVectors) {
		Map<FV, Map<String, Integer>> winningLabels = new LinkedHashMap<>();

		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble) {
				List<String> winning = classifier.test(featureVectors);

				if (winning == null || winning.size() != featureVectors.size())
					continue;

				for (int i = 0; i < featureVectors.size(); i++) {
					FV fv = featureVectors.get(i);
					if (winningLabels.get(fv) == null)
						winningLabels.put(fv, new HashMap<>());
					if (winningLabels.get(fv).get(winning.get(i)) == null)
						winningLabels.get(fv).put(winning.get(i), 0);
					winningLabels.get(fv).put(winning.get(i), winningLabels.get(fv).get(winning.get(i)) + 1);
				}
			}

		List<String> labels = new ArrayList<>();

		for (int i = 0; i < featureVectors.size(); i++) {
			FV fv = featureVectors.get(i);
			int count = -1;
			String winningLabel = null;

			List<String> labelAlphabet = getLabelAlphabet();
			if (labelAlphabet != null)
				for (String label : labelAlphabet)
					if (winningLabels.get(fv).get(label) != null)
						if (winningLabels.get(fv).get(label) > count) {
							winningLabel = label;
							count = winningLabels.get(fv).get(label);
						}

			labels.add(winningLabel);
		}

		return labels;
	}

	@Override
	public Map<String, Double> getLabelDistribution(FV featureVector) {

		Map<String, List<Double>> labelDistributions = new HashMap<>();

		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble) {

				Map<String, Double> labelDistribution = classifier.getLabelDistribution(featureVector);

				if (labelDistribution != null)
					for (String label : labelDistribution.keySet()) {
						if (labelDistributions.get(label) == null)
							labelDistributions.put(label, new ArrayList<>());

						labelDistributions.get(label).add(labelDistribution.get(label));
					}
			}

		if (labelDistributions == null || labelDistributions.size() == 0)
			return null;

		Map<String, Double> labelDistribution = new LinkedHashMap<>();

		List<String> labelAlphabet = getLabelAlphabet();
		if (labelAlphabet != null)
			for (String label : labelAlphabet) {
				double probability = 0.0;
				if (labelDistributions != null && labelDistributions.get(label) != null)
					probability = MathFunctions.getMean(labelDistributions.get(label));
				labelDistribution.put(label, probability);
			}

		return labelDistribution;
	}

	@Override
	public List<Map<String, Double>> getLabelDistributionResult() {
		try {
			throw new Exception("EnsembleClassifier: method results depends on correcly ordered results in the classifiers. Correctness of results cannot be guaranteed.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public IProbabilisticClassificationResult<FV> createClassificationResult(List<FV> featureVectors) {

		Map<FV, Collection<LabelDistribution>> labelUncertainties = new LinkedHashMap<>();

		if (classifierEnsemble != null)
			for (Classifier<O, FV> classifier : classifierEnsemble) {
				IProbabilisticClassificationResult<FV> classificationResult = classifier.createClassificationResult(featureVectors);
				for (FV fv : featureVectors) {
					if (labelUncertainties.get(fv) == null)
						labelUncertainties.put(fv, new ArrayList<>());

					labelUncertainties.get(fv).add(classificationResult.getLabelDistribution(fv));
				}
			}

		Map<FV, Map<String, Double>> labelDistributionMap = new LinkedHashMap<>();
		for (FV fv : labelUncertainties.keySet())
			labelDistributionMap.put(fv, LabelDistributionTools.mergeLabelUncertainties(labelUncertainties.get(fv)).getValueDistribution());

		return new ProbabilisticClassificationResult<>(labelDistributionMap);
	}

	@Override
	public List<String> getLabelAlphabet() {
		// problem of the ensemble classifier. currently the label alphabet may
		// have changed with a training of an included classifier.
		// if (labelAlphabet == null)
		refreshLabelAlphabet();

		return labelAlphabet;
	}

	public Collection<Classifier<O, FV>> getClassifierEnsemble() {
		return classifierEnsemble;
	}

	public void setClassifierEnsemble(Collection<Classifier<O, FV>> classifierEnsemble) {
		this.classifierEnsemble = classifierEnsemble;
	}

}
