import java.util.*;
import java.io.*;

public class DecisionTree{
	//private String [] attributes = {"Buying","Maintenance","Doors","Persons","LuaageBoot", "Safety"};
	private DecTreeNode root;
	public List <String> labels;
	public List <List<String>> attributeValues;
	public int mode;

	// Decision Tree Structure
	DecisionTree(DataSet train, int mode){
		this.labels = train.labels;
		this.attributeValues = train.attributeValues;
		this.mode = mode;
		List <List<String>> cars = train.cars;
		Set <Integer> questions = new HashSet <Integer> ();
		for(int i = 0; i < attributeValues.size();i++) questions.add(i);
		root = buildTree(cars,questions,null,majority(cars));
	}

	// Build Tree
	private DecTreeNode buildTree(List<List<String>> cars, Set <Integer> questions, String answerToParent, String defaultLabel){
		if(cars.isEmpty()) return new DecTreeNode(defaultLabel,-1,answerToParent,true,true);
		String sameLabel = sameLabels(cars);
		if(sameLabel != null) return new DecTreeNode(sameLabel,-1,answerToParent,true,true);
		String majorityLabel = majority(cars);
		// System.out.println(questions.size());
		if(questions.isEmpty()) return new DecTreeNode(majorityLabel,-1,answerToParent,true,false);

		int bestQ = bestQuestion(cars,questions,mode);
		//System.out.println(attributes[bestQ]);
		DecTreeNode node = new DecTreeNode(majorityLabel,bestQ,answerToParent,false,false);
		List<List<List<String>>> subCars = getSubCars(cars,bestQ);
		questions.remove(bestQ);
		for(int i = 0; i < attributeValues.get(bestQ).size();i++)
			node.addChild(buildTree(subCars.get(i),questions,attributeValues.get(bestQ).get(i),majorityLabel));
		questions.add(bestQ);
		return node;
	}

	// Best question to ask.
	private int bestQuestion(List<List<String>> cars, Set <Integer> questions, int mode){
		int bestQ = -1;
		if(mode == 1){
			double inGain = -1;
			for(int q: questions){
				double gainRtn = infoGain(cars,q);
				if(gainRtn > inGain + 1e-9){
					inGain = gainRtn;
					bestQ = q;
				}
			}	
		}else if(mode == 2){
			double gini = 2;
			for(int q: questions){
				double giniRtn = giniIndex(cars,q);
				if(giniRtn < gini){
					gini = giniRtn;
					bestQ = q;
				}
			}
		}
		// System.out.println("inGain =" + inGain);
		// System.out.println("question = " + bestQ);
		return bestQ;
	}

	// Determine if all the labels are the same.
	private String sameLabels(List<List<String>> cars){
		int classIndex = cars.get(0).size() - 1;
		String label = cars.get(0).get(classIndex);
		for(List<String> l : cars){
			if(!l.get(classIndex).equals(label)) return null;
		}
		return label;
	}

	// Count each labels.
	private int [] orient(List<List<String>> cars){
		int classIndex = cars.get(0).size() - 1;
		int [] counts = new int [labels.size()];
		for(List<String> l: cars){
			for(int i = 0; i < labels.size();i++){
				if(l.get(classIndex).equals(labels.get(i))){
					counts[i]++;
					break;
				}
			}
		}
		return counts;
	}

	// Find the majority label;
	private String majority (List<List<String>> cars){
		int [] counts = orient(cars);
		int max = counts[0];
		String major = labels.get(0);
		for(int i  = 1; i < counts.length;i++){
			if(counts[i] > max){
				max = counts[i];
				major = labels.get(i);
			}
		}
		return major;
	}

	// Sum.
	private double enY (int[] counts){
		int total = 0;
		double entropyY = 0;
		for(int i : counts)
			total+= i;
		for(int i : counts){
			if(i > 0){
				double prob = (double) i / (double) total;
				entropyY -= prob*Math.log(prob)/Math.log(2);
			}
		}
		return entropyY;
	}

	// Calculate the information gain.
	private double infoGain(List <List<String>> cars, int feature){
		int classIndex = cars.get(0).size() - 1;
		int [] count = orient(cars);
		double entropyY = enY(count);
		int choices = attributeValues.get(feature).size();
		int [][] values = new int [choices][labels.size() + 1];
		for(int i = 0; i < cars.size();i++){
			String attValue = cars.get(i).get(feature);
			for(int j = 0; j < choices;j++){
				if(attributeValues.get(feature).get(j).equals(attValue)){
					values[j][0]++;
					for(int k = 0; k < labels.size();k++){
						if(cars.get(i).get(classIndex).equals(labels.get(k)))
						values[j][k+1]++;
					}
				}
			}
		}
		double [] entropyXi = new double [choices];
		for(int i = 0; i < choices; i++){
			for(int j = 1;j <= labels.size();j++){
				if(values[i][j] > 0){
					double prob = (double) values[i][j]/(double) values[i][0];
					entropyXi[i] -= prob*Math.log(prob)/Math.log(2);
				}
			}
		}
		int total = 0;
		for(int i = 0; i < choices;i++)
			total += values[i][0];
		double entropyYX = 0;
		for(int i = 0; i < choices;i++)
			entropyYX += (double) values[i][0]/(double) total *entropyXi[i];
		return entropyY - entropyYX;
	}

	// Gini Index
	private double giniIndex(List<List<String>> cars, int feature){
		int classIndex = cars.get(0).size() - 1;
		int choices = attributeValues.get(feature).size();
		int [][] values = new int [choices][labels.size() + 1];
		for(int i = 0; i < cars.size();i++){
			String attValue = cars.get(i).get(feature);
			for(int j = 0; j < choices;j++){
				if(attributeValues.get(feature).get(j).equals(attValue)){
					values[j][0]++;
					for(int k = 0; k < labels.size();k++){
						if(cars.get(i).get(classIndex).equals(labels.get(k)))
						values[j][k+1]++;
					}
				}
			}
		}
		double [] gini = new double [choices];
		for(int i = 0; i < choices; i++)
			gini[i] = 1;
		for(int i = 0; i < choices; i++){
			for(int j = 1; j <= labels.size();j++){
				if(values[i][j] > 0){
					double ratio = (double) values[i][j]/(double) values[i][0];
					gini[i] -= ratio*ratio;
				}
			}
		}
		int total = 0;
		for(int i = 0; i < choices; i++)
			total += values[i][0];
		double giniRtn = 0;
		for(int i = 0; i < choices; i++)
			giniRtn += (double) values[i][0]/(double) total *gini[i];
		return giniRtn;
	}

	// subCars
	private List<List<List<String>>> getSubCars (List<List<String>> cars, int q){
		int choices = attributeValues.get(q).size();
		List<List<List<String>>> subCars = new ArrayList<List<List<String>>> ();
		for(int i = 0; i < choices; i++)
			subCars.add(new ArrayList<List<String>> ());
		for(int i = 0; i < cars.size();i++){
			for(int j = 0; j < choices;j++){
				if(attributeValues.get(q).get(j).equals(cars.get(i).get(q)))
					subCars.get(j).add(cars.get(i));
			}
		}
		return subCars;
	}

	/*
	// print
	public void print(){
		printTreeNode(root,0);
	}

	// Print TreeNode

	public void printTreeNode(DecTreeNode p,int k){
		StringBuilder sb = new StringBuilder ();
        for(int i = 0; i < k;i++)
            sb.append("     ");
		if(p.equals(root)){
			sb.append("ROOT");
		}else{
			sb.append(p.parentAttributeValue);
		}

		if(p.terminal){
			sb.append("(" + p.quality + ")");
            if(p.pure) sb.append(" Pure");
            else sb.append(" Impure");
			System.out.println(sb.toString());
		}else{
			sb.append("{" + attributes[p.attribute] + "?}");
			System.out.println(sb.toString());
			for(DecTreeNode child: p.children)
				printTreeNode(child,k+1);
		}
	}
	*/

	// Classify
	public String classify(List <String> car, DecTreeNode root){
		DecTreeNode node = root;
		boolean found;
		while(!node.terminal){
			found = false;
			for(DecTreeNode child : node.children){
				if(child.parentAttributeValue.equals(car.get(node.attribute))){
					node = child;
					found = true;
					break;
				}
			}
			if(!found) break;
		}
		return node.quality;
	}
	// Classify
	public String classify(List <String> car){
		return classify(car,root);
	}
}