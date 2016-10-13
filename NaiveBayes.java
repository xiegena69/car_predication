import java.util.*;
import java.io.*;

public class NaiveBayes{
	String [] classtype = {"unacc", "acc", "good", "vgood"};
	public List <List<String>> attributeValues;
	int [] countClass = new int [4];
	// [classType][Attribute][Attribute Value]
	int [][][] counts;
	int total = 0;

	// Count the number of appearance for each class, attribute and attribute value
	NaiveBayes(DataSet trainset){
		this.attributeValues = trainset.attributeValues;
		total = trainset.cars.size();

		int numAttri = attributeValues.size();
		int maxNumVal = 0;
		for(int i = 0; i < attributeValues.size();i++){
			int len = attributeValues.get(i).size();
			if(len > maxNumVal) maxNumVal = len;
		}

		counts = new int [4][numAttri][maxNumVal];

		int classIndex = trainset.cars.get(0).size() - 1;

		for(List<String> l: trainset.cars){
			for(int i = 0; i < 4; i++){
				if(l.get(classIndex).equals(classtype[i])){
					countClass[i]++;
					for(int j = 0; j < l.size()-1;j++){
						for(int k = 0; k < attributeValues.size();k++){
							if(attributeValues.get(j).get(k).equals(l.get(j))){
								counts[i][j][k]++;
								break;
							}
						}
					}
					break;
				}
			}
		}
	}

	// Given a data, classify it into a class
	public String classify(List <String> car){
		if(total == 0) return null;
		double [] logCandProb = new double [4];

		// initialize logCandProb
		for(int i = 0; i < 4; i++)
			logCandProb[i] = Math.log(((double) countClass[i])/((double) total)); 
		// Attributes
		for(int j = 0; j < car.size() - 1; j++){
			// Attribute values
			for(int k = 0; k < attributeValues.get(j).size(); k++){
				if(attributeValues.get(j).get(k).equals(car.get(j))){
					for(int i = 0; i < 4; i++){
						double currProb = (double)counts[i][j][k]/(double)countClass[i];
						// Give absent attribute value a small probability
						if(currProb == 0) currProb = 0.001;
						currProb = Math.log(currProb);
						logCandProb[i] += currProb;
					}
					break;
				}
			}
		}

		String rtn = classtype[0];
		double maxProb = logCandProb[0];

		// Compare which class type has the highest probability
		for(int i = 1; i < 4; i++){
			if(logCandProb[i] > maxProb){
				maxProb = logCandProb[i];
				rtn = classtype[i];
			}
		}
		return rtn;
	}
}
	