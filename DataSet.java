import java.util.*;

public class DataSet {
	private String [][] attributes = {{"vhigh","high","med","low"},{"vhigh","high","med","low"},{"2","3","4","5more"},{"2","4","more"},{"small","med","big"},{"low","med","high"}};
	public List <String> labels = null;
	public List <List<String>> attributeValues = null;
	public List <List<String>> cars = null;


	public void addCar(String line,int oneHot){
		// Split the line into attributes and class
		String [] split = line.split(",");

		// Add label
		if(labels == null) labels = new ArrayList <String> ();
		if(!labels.contains(split[split.length-1]))
			labels.add(split[split.length-1]);

		if(oneHot == 0) normalData(split);
		else oneHotData(split);
		
	}

	// Ordinary Data Set
	private void normalData(String [] split){
		// Add to the data base
		if(cars == null) cars = new ArrayList<List<String>> ();
		ArrayList <String> car = new ArrayList<String> (Arrays.asList(split));
		cars.add(car);

		// Add attributes
		if(attributeValues == null){
			attributeValues = new ArrayList<List<String>> ();
			for(int i = 0; i < split.length-1;i++)
				attributeValues.add(new ArrayList<String> ());
		}

		for(int i = 0; i < split.length-1;i++){
			if(!attributeValues.get(i).contains(split[i]))
				attributeValues.get(i).add(split[i]);
		}
	}

	// One hot Data set
	private void oneHotData(String [] split){
		// Add to the data base
		if(cars == null) cars = new ArrayList<List<String>> ();
		ArrayList <String> car = new ArrayList<String> ();

		for(int i = 0; i < attributes.length;i++){
			for(int j = 0; j < attributes[i].length;j++){
				if(split[i].equals(attributes[i][j]))
					car.add("Yes");
				else
					car.add("No");
			}
		}
		car.add(split[6]);
		cars.add(car);

		// Add attributes
		if(attributeValues == null)
			setAttribute();
	}

	// Set attribute for one hot encoding
	private void setAttribute(){
		attributeValues = new ArrayList<List<String>> ();
		for(int i = 0; i < 21; i++){
			ArrayList <String> tmp = new ArrayList <String> ();
			tmp.add("Yes");
			tmp.add("No");
			attributeValues.add(tmp);
		}
	}
}