package main.java.com.github.TKnudsen.DMandML.model.supervised.regression;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.Feature;

/**
 * <p>
 * Title: RandomTree
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.02
 */
public class BasicRandomTree<O, FV extends AbstractFeatureVector<O, ? extends Feature<O>>> extends WekaRegressionWrapper<O, FV> {


	private boolean allowUnclassifiedInstances = false;
	private int backfittingFolds = 0;
	private boolean breakTiesRandomly = false;
	private double classVarianceProportion = 1e-3;
	private boolean debug = false;
	private int decimalPlaces = 2;
	private boolean doNotCheckCapabilities = false;
	private int maximumTreeDepth = 0;
	private int minimumInstancesPerLeaf = 1;
	private int randomlyInvestigatedAttributes = 0;
	private int randomSeed = 1;

	/**
	 * @return the backfittingFolds
	 */
	public int getBackfittingFolds() {
		return backfittingFolds;
	}

	/**
	 * @return the classVarianceProportion
	 */
	public double getClassVarianceProportion() {
		return classVarianceProportion;
	}
	
	/**
	 * @return the decimalPlaces
	 */
	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	@Override
	public String getDescription() {
		return "WEKA's implementation of the RandomTree regression algorithm.";
	}

	/**
	 * @return the maximumTreeDepth
	 */
	public int getMaximumTreeDepth() {
		return maximumTreeDepth;
	}

	/**
	 * @return the minimumInstancesPerLeaf
	 */
	public int getMinimumInstancesPerLeaf() {
		return minimumInstancesPerLeaf;
	}

	@Override
	public String getName() {
		return "RandomTree";
	}

	/**
	 * @return the randomlyInvestigatedAttributes
	 */
	public int getRandomlyInvestigatedAttributes() {
		return randomlyInvestigatedAttributes;
	}

	/**
	 * @return the randomSeed
	 */
	public int getRandomSeed() {
		return randomSeed;
	}

	/**
	 * @return the allowUnclassifiedInstances
	 */
	public boolean isAllowUnclassifiedInstances() {
		return allowUnclassifiedInstances;
	}

	/**
	 * @return the breakTiesRandomly
	 */
	public boolean isBreakTiesRandomly() {
		return breakTiesRandomly;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the doNotCheckCapabilities
	 */
	public boolean isDoNotCheckCapabilities() {
		return doNotCheckCapabilities;
	}

	/**
	 * @param allowUnclassifiedInstances the allowUnclassifiedInstances to set
	 */
	public void setAllowUnclassifiedInstances(boolean allowUnclassifiedInstances) {
		this.allowUnclassifiedInstances = allowUnclassifiedInstances;
	}

	/**
	 * @param backfittingFolds the backfittingFolds to set
	 */
	public void setBackfittingFolds(int backfittingFolds) {
		this.backfittingFolds = backfittingFolds;
	}

	/**
	 * @param breakTiesRandomly the breakTiesRandomly to set
	 */
	public void setBreakTiesRandomly(boolean breakTiesRandomly) {
		this.breakTiesRandomly = breakTiesRandomly;
	}

	/**
	 * @param classVarianceProportion the classVarianceProportion to set
	 */
	public void setClassVarianceProportion(double classVarianceProportion) {
		this.classVarianceProportion = classVarianceProportion;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @param decimalPlaces the decimalPlaces to set
	 */
	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * @param doNotCheckCapabilities the doNotCheckCapabilities to set
	 */
	public void setDoNotCheckCapabilities(boolean doNotCheckCapabilities) {
		this.doNotCheckCapabilities = doNotCheckCapabilities;
	}

	/**
	 * @param maximumTreeDepth the maximumTreeDepth to set
	 */
	public void setMaximumTreeDepth(int maximumTreeDepth) {
		this.maximumTreeDepth = maximumTreeDepth;
	}

	/**
	 * @param minimumInstancesPerLeaf the minimumInstancesPerLeaf to set
	 */
	public void setMinimumInstancesPerLeaf(int minimumInstancesPerLeaf) {
		this.minimumInstancesPerLeaf = minimumInstancesPerLeaf;
	}

	/**
	 * @param randomlyInvestigatedAttributes the randomlyInvestigatedAttributes to set
	 */
	public void setRandomlyInvestigatedAttributes(int randomlyInvestigatedAttributes) {
		this.randomlyInvestigatedAttributes = randomlyInvestigatedAttributes;
	}

	/**
	 * @param randomSeed the randomSeed to set
	 */
	public void setRandomSeed(int randomSeed) {
		this.randomSeed = randomSeed;
	}

	@Override
	protected void initializeRegression() {
		wekaRegressionModel = new weka.classifiers.trees.RandomTree();

		List<String> aryOpts = new ArrayList<String>();

		aryOpts.add("-K"); // Number of attributes to randomly investigate (default 0)
		aryOpts.add(String.valueOf(getRandomlyInvestigatedAttributes()));
		
		aryOpts.add("-M"); // Set minimum number of instances per leaf (default 1)
		aryOpts.add(String.valueOf(getMinimumInstancesPerLeaf()));
		
		aryOpts.add("-V"); // Set minimum numeric class variance proportion of train variance for split (default 1e-3)
		aryOpts.add(String.valueOf(getClassVarianceProportion()));
		
		aryOpts.add("-S"); // Seed for random number generator (default 1)
		aryOpts.add(String.valueOf(getRandomSeed()));
		
		aryOpts.add("-depth"); // The maximum depth of the tree, 0 for unlimited (default 0)
		aryOpts.add(String.valueOf(getMaximumTreeDepth()));
		
		aryOpts.add("-N"); // Number of folds for backfitting (default 0, no backfitting)
		aryOpts.add(String.valueOf(getBackfittingFolds()));
		
		if (isAllowUnclassifiedInstances())
			aryOpts.add("-U"); // Allow unclassified instances

		if (isBreakTiesRandomly())
			aryOpts.add("-B"); // Break ties randomly when several attributes look equally good

		if (isDebug())
			aryOpts.add("-output-debug-info"); // Enables debug mode

		if (isDoNotCheckCapabilities())
			aryOpts.add("-do-not-check-capabilities"); // Classifier capabilities are not checked before classifier is built (use with caution)

			aryOpts.add("-num-decimal-places"); // The number of decimal places for the output of numbers in the model (default 2)
			aryOpts.add(String.valueOf(getDecimalPlaces()));

		String[] opts = aryOpts.toArray(new String[aryOpts.size()]);

		try {
			wekaRegressionModel.setOptions(opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
