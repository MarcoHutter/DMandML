package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.data.cluster.featureFV.FeatureVectorClusteringResult;
import com.github.TKnudsen.DMandML.data.cluster.featureFV.IFeatureVectorClusteringResultSupplier;

/**
 * <p>
 * Title: IClusterValidityMeasure
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
 * @version 1.03
 */
public interface IClusterValidityMeasure<FV extends AbstractFeatureVector<?, ?>> extends ISelfDescription {

	public IFeatureVectorClusteringResultSupplier<FeatureVectorClusteringResult<FV>> getClusterResultSet();

	public void run();

	public double getClusterValidity();
}
