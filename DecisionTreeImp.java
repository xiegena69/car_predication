import java.util.*;
import java.io.*;

public class DecisionTreeImp{
	// Main Function
	public static void main(String[] args) {
		// Determine if the command is correct
		if(args.length != 2){
			System.out.println("usage: java DecisionTreeImp <TrainFileName> <TestFilename>");
			System.exit(-1);
		}

		// Normal Decision Tree
		DataSet trainSet = createDataSet(args[0],0);
		DataSet testSet = createDataSet(args[1],0);
		// Entropy
		DecisionTree treeE = new DecisionTree(trainSet,1);
		// Gini
		DecisionTree treeG = new DecisionTree(trainSet,2);

		// One Hot Encoding
		DataSet trainOneHot = createDataSet(args[0],1);
		DataSet testOneHot = createDataSet(args[1],1);
		// Entropy
		DecisionTree oneHotE = new DecisionTree(trainOneHot,1);
		// Gini
		DecisionTree oneHotG = new DecisionTree(trainOneHot,2);

		// Printing
		System.out.println();
		System.out.println("##### ##### ##### ##### ##### ##### ##### ##### ##### #####");
		System.out.println("#####                  Decision Tree                  #####");
		System.out.println("##### ##### ##### ##### ##### ##### ##### ##### ##### #####");

		System.out.println();
		System.out.println("Normal Decision Tree by Entropy");
		System.out.println("Train Accuracy = " + accuracy(trainSet,treeE));
		System.out.println("Test Accuracy = " + accuracy(testSet,treeE));

		System.out.println();
		System.out.println("Normal Decision Tree by Gini");
		System.out.println("Train Accuracy = " + accuracy(trainSet,treeG));
		System.out.println("Test Accuracy = " + accuracy(testSet,treeG));

		System.out.println();
		System.out.println("One Hot Encoding by Entropy");
		System.out.println("Train Accuracy = " + accuracy(trainOneHot,oneHotE));
		System.out.println("Test Accuracy = " + accuracy(testOneHot,oneHotE));

		System.out.println();
		System.out.println("One Hot Encoding by Gini");
		System.out.println("Train Accuracy = " + accuracy(trainOneHot,oneHotG));
		System.out.println("Test Accuracy = " + accuracy(testOneHot,oneHotG));

	}

	// Convert from test file format to DataSet format.
	private static DataSet createDataSet(String file, int oneHot){
		DataSet set = new DataSet();
		BufferedReader in;
		try{
			in = new BufferedReader(new FileReader(file));
			while(in.ready()){
				String line = in.readLine();
				set.addCar(line,oneHot);
			}
			in.close();
		}catch(Exception e){
			System.out.println("File " + file +" does not exist.");
			//e.printStackTrace();
			System.exit(-1);
		}
		return set;
	}

	// Calculate the accuracy for Decision Tree
	private static double accuracy(DataSet set, DecisionTree tree){
		int correct = 0;
		for(List<String> l : set.cars){
			if(l.get(l.size()-1).equals(tree.classify(l))) correct++;
		}
		return (double) correct/(double) set.cars.size();
	}
}
