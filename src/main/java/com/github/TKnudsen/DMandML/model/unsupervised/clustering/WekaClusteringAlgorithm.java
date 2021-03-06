package com.github.TKnudsen.DMandML.model.unsupervised.clustering;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.IClusteringResult;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.tools.WekaClusteringTools;

import weka.clusterers.AbstractClusterer;
import weka.core.Instances;

/**
 * <p>
 * Title: WekaClusteringAlgorithm
 * </p>
 * 
 * <p>
 * Description: abstract baseline routine for WEKA-base clustering routines.
 * 
 * For more information see: Dan Pelleg, Andrew W. Moore: X-means: Extending
 * K-means with Efficient Estimation of the Number of Clusters. In: Seventeenth
 * International Conference on Machine Learning, 727-734, 2000.
 * 
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class WekaClusteringAlgorithm implements INumericalClusteringAlgorithm {

	/**
	 * data to be clustered
	 */
	protected List<NumericalFeatureVector> featureVectors;

	/**
	 * WEKA representation of the data
	 */
	protected Instances data;

	/**
	 * the WEKA-based clustering routine
	 */
	protected AbstractClusterer wekaClusterer;

	/**
	 * cluster result that can be used for downstream analysis
	 */
	protected IClusteringResult<NumericalFeatureVector, Cluster<NumericalFeatureVector>> clusterResult;

	public WekaClusteringAlgorithm() {
	}

	public WekaClusteringAlgorithm(List<NumericalFeatureVector> featureVectors) {
		this.setFeatureVectors(featureVectors);
	}

	protected abstract void initializeClusteringAlgorithm();

	@Override
	public void calculateClustering() {
		initializeClusteringAlgorithm();

		try {
			clusterResult = WekaClusteringTools.getClusterResultFromWekaClusterer(wekaClusterer, data, featureVectors,
					getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IClusteringResult<NumericalFeatureVector, Cluster<NumericalFeatureVector>> getClusteringResult() {
		if (clusterResult == null)
			throw new NullPointerException(
					"WekaClusteringAlgorithm: cluster result was null. Was the clustering calculated yet?");

		return clusterResult;
	}

	public List<NumericalFeatureVector> getFeatureVectors() {
		return featureVectors;
	}

	public void setFeatureVectors(List<NumericalFeatureVector> featureVectors) {
		this.featureVectors = featureVectors;

		if (featureVectors == null)
			data = null;
		else
			data = WekaConversion.getInstances(featureVectors, false);

		clusterResult = null;
	}
}
