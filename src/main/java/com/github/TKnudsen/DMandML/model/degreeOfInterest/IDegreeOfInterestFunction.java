package com.github.TKnudsen.DMandML.model.degreeOfInterest;

import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.IFeatureVectorSupplier;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: IInterestingnesFunction
 * </p>
 * 
 * <p>
 * Description: Interestingness functions can be used to perform algorithmical
 * guidance support. According to the implemented criterion, the function
 * prefers interesting instances over others.
 * 
 * The default value domain is [0...1] as this allows an easy combination of
 * several (weighted) interestingness functions.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface IDegreeOfInterestFunction<FV extends AbstractFeatureVector<?, ?>> extends Function<FV, Double>, ISelfDescription {

	public IFeatureVectorSupplier<FV> getFeatureVectorSupplier();
}
