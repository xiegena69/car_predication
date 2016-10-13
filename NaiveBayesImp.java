import java.util.*;
import java.io.*;


public class NaiveBayesImp{
	// Main Function
	public static void main(String[] args) {
		// Determine if the command is correct
		if(args.length != 2){
			System.out.println("usage: java NaiveBayesImp <Train> <Test>");
			System.exit(-1);
		}

		// Create a data space
		DataSet trainset = createDataSet(args[0],0);
		DataSet testset = createDataSet(args[1],0);
		DataSet trainOneHot = createDataSet(args[0],1);
		DataSet testOneHot = createDataSet(args[1],1);

		NaiveBayes nb = new NaiveBayes(trainset);
		NaiveBayes nbOneHot = new NaiveBayes(trainOneHot);


		// Printing
		System.out.println();
		System.out.println("##### ##### ##### ##### ##### ##### ##### ##### ##### #####");
		System.out.println("#####                   Naive Bayes                   #####");
		System.out.println("##### ##### ##### ##### ##### ##### ##### ##### ##### #####");

		System.out.println();
		System.out.println("Ordinary Naive Bayes");
		System.out.print("Train ");
		print(nb,trainset);
		System.out.print("Test");
		print(nb,testset);

		System.out.println();
		System.out.println("One Hot Encoding Naive Bayes");
		System.out.print("Train ");
		print(nbOneHot,trainOneHot);
		System.out.print("Test");
		print(nbOneHot,testOneHot);

	} 

	// Convert from test file format to DataSet format.
	private static DataSet createDataSet(String file,int oneHot){
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
			System.out.println("File " + file + " does not exist.");
			//e.printStackTrace();
			System.exit(-1);
		}
		return set;
	}

	// print result
	private static void print(NaiveBayes nb, DataSet testset){
		String [] classtype = {"unacc", "acc", "good", "vgood"};
		int [][] matrix = new int[4][4];
		double accuracy = 0;

		int classIndex = testset.cars.get(0).size()-1;
		for(List <String> l:testset.cars){
			String classifyResult = nb.classify(l);
			for(int i = 0; i < 4; i++){
				if(l.get(classIndex).equals(classtype[i])){
					for(int j = 0; j < 4; j++){
						if(classifyResult.equals(classtype[j])){
							matrix[i][j]++;
							if(i == j) accuracy++;
							break;
						}
					}
					break;
				}
			}
		}

		System.out.println("Accuracy =" + accuracy/(double) testset.cars.size());

		System.out.println("***** ***** Confusion Matrix ***** *****");
		System.out.println("	unacc	accu	good	vgood");
		for(int i = 0; i < 4; i++){
			System.out.print(classtype[i] + "	");
			for(int j = 0; j < 4; j++)
				System.out.print(matrix[i][j] + "	");
			System.out.println();
		}
	}
}
