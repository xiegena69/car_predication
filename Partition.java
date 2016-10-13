import java.util.*;
import java.io.*;

public class Partition{
	// Main Function
	public static void main(String[] args) {
		// Determine if the command is correct
		if(args.length != 3){
			System.out.println("usage: java Partition <FileName> <TrainFileName> <TestFilename>");
			System.exit(-1);
		}
		// Partitioning Process
		BufferedReader in;
		Random rng;
		FileWriter train,test;
		try{
			in = new BufferedReader(new FileReader(args[0]));
			rng = new Random();
			train = new FileWriter(args[1]);
			test = new FileWriter(args[2]);
			// Read from the given file
			while(in.ready()){
				String line = in.readLine();
				// 0.8 Training data, 0.2 test data
				int randomInt = rng.nextInt(100);
				if(randomInt < 80){
					train.write(line);
					train.write(System.lineSeparator());
				}else{
					test.write(line);
					test.write(System.lineSeparator());
				}
			}
			in.close();
			train.close();
			test.close();
		}catch(Exception e){
			System.out.println("File " + args[0] +" does not exist.");
			//e.printStackTrace();
			System.exit(-1);
		}
	}
}
