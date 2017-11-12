package com.github.TKnudsen.DMandML.model.guidance.classificationBased.labels;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureVectorSupplier;
import com.github.TKnudsen.DMandML.data.classification.IProbabilisticClassificationResultSupplier;
import com.github.TKnudsen.DMandML.data.classification.ITrainingDataSupplier;

/**
 * <p>
 * Title: TrainingDataLabelDistributionBasedInterestingnessFunction
 * </p>
 * 
 * <p>
 * Description: Compares an observed (training data-based) class distribution
 * with a pre-given class distribution. Accordingly, interestingness values for
 * FVs are set to balance the deviance.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class TrainingDataLabelDistributionBasedInterestingnessFunction<FV extends AbstractFeatureVector<?, ?>> extends LabelDistributionBasedInterestingnessFunction<FV> {

	private ITrainingDataSupplier<FV> trainingDataSupplier;
	private String classAttribute;

	public TrainingDataLabelDistributionBasedInterestingnessFunction(FeatureVectorSupplier<FV> featureVectorSupplier, IProbabilisticClassificationResultSupplier<FV> classificationResultSupplier, Map<String, Double> targetLabelDistribution,
			ITrainingDataSupplier<FV> trainingDataSupplier) {
		super(featureVectorSupplier, classificationResultSupplier, targetLabelDistribution);

		if (trainingDataSupplier == null)
			throw new NullPointerException("TrainingDataLabelDistributionBasedInterestingnessFunction: trainingDataSupplier is null.");

		this.trainingDataSupplier = trainingDataSupplier;
		classAttribute = trainingDataSupplier.getClassAttribute();
	}

	@Override
	public String getDescription() {
		return "Assigns interestingness scores to FVs according to a targeted label distribution";
	}

	@Override
	protected Map<String, Double> calculateObservedLabelDistribution() {
		Map<String, Double> targetLabelDistribution = getTargetLabelDistribution();
		List<FV> trainingFeatureVectors = trainingDataSupplier.get();

		// gather observed distribution
		Map<String, Double> observedLabelDistribution = new LinkedHashMap<>();
		for (String label : targetLabelDistribution.keySet())
			observedLabelDistribution.put(label, 0.0);

		for (FV fv : trainingFeatureVectors)
			if (fv.getAttribute(classAttribute) != null) {
				String label = fv.getAttribute(classAttribute).toString();
				if (observedLabelDistribution.containsKey(label))
					observedLabelDistribution.put(label, observedLabelDistribution.get(label) + 1);
			}
		return observedLabelDistribution;
	}
}
