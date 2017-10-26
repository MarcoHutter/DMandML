package com.github.TKnudsen.DMandML.data.cluster.numerical;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.data.cluster.ClusteringResult;

/**
 * <p>
 * Title: NumericalFeatureVectorClusterResult
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class NumericalFeatureVectorClusterResult extends ClusteringResult<NumericalFeatureVector, NumericalFeatureVectorCluster> {

	public NumericalFeatureVectorClusterResult(List<NumericalFeatureVectorCluster> clusters) {
		super(clusters);
	}

}
