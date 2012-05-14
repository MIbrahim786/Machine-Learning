import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class FirstProg {
    
    int attr; 
    // ArrayList<Vector> list2 = new ArrayList<Vector>();
    
    Vector v[];         // All the possible values are stored in a vector. v is an array of dimension attribute.
    String attr_name[]; //In this array it stores names of all attributes
    
    
    class Data {        // class to represent a data consisting of attr values of attributes.
        public int attr_values[];  // Values of attributes will be stored in this array. 
        
        public Data(int attr) 
        {
            attr_values = new int[attr];
        }
    };
    
    
	
    class TNode {      // This class represents a node in the decomposition tree
        // it deal with one node at a time 
        //it takes parent node 
        
        
        public Vector set;         // The set of data points if this is a leaf node
        public TNode parent_node; // The root has parent_node==null. Parent to this node
        public TNode child_node[];// It referes to child_node if this is not leaf node.
        public int decompAttr;  // It will divide the set of data point if its not leaf node
        public int decompValue;		// the attribute-value that is used to divide the parent_node node
        public double entropy;		// The entropy of data points if this node is a leaf node
        
        public TNode()// constructor
        {
            set = new Vector();
        }
        
    };
    
    TNode root = new TNode();   /*  The root of the decomposition tree  */
    // it creates the root
    
    
    public int getAttributeValue(final int num_attr, final String symbol) {    
        // This function returns integer corresponding to symbolic val of attr.
		
        int index = v[num_attr].indexOf(symbol);
		
        if (index < 0) {
            v[num_attr].addElement(symbol);
            return v[num_attr].size() -1;
        }            
        return index;
    }
    
    
    
    public int []getAllValues(final Vector set, final int num_attr) {  
        /*It returns the values of specified num_attr in the data set */             
		
        Vector values = new Vector();
        int num = set.size();
        
        
        for (int i=0; i< num; i++) {
            Data point = (Data)set.elementAt(i);
            String symbol = (String)v[num_attr].elementAt(point.attr_values[num_attr] );
			
            int index = values.indexOf(symbol);
            if (index < 0) {
                values.addElement(symbol);
            }
        }
        
        int array[] = new int[values.size()];
        
        for (int j=0; j< array.length; j++) {
            
            String symbol = (String)values.elementAt(j);
            array[j] = v[num_attr].indexOf(symbol);
        }
        values = null;
        return array;
	}
    
    
	/*  Returns a subset of data, in which the value of the specfied attribute of all data points is the specified value  */
    public Vector getSubset(final Vector set, final int num_attr, final int value) { 
		
        Vector subset = new Vector();
        int num = set.size();
		
        for (int j=0; j< num; j++) {
            Data point = (Data)set.elementAt(j);
            if (point.attr_values[num_attr] == value) subset.addElement(point);
        }
        return subset;
        
	}
    
    
	/*  Calculates the entropy of the set of data points.
     The entropy is calculated using the values of the output attribute which is the last element in the array attribtues
     */
    public double calculateEntropy(Vector set) {
        
        double sum = 0;
        int num_attr = attr-1;
        int num_data = set.size();
        int num_val = v[num_attr].size();
		
        
        if (num_data == 0) return 0;                  
        for (int i=0; i< num_val; i++) 
        {
            int count=0;
            for (int j=0; j< num_data; j++) 
            {
                Data point = (Data)set.elementAt(j);
                if (point.attr_values[num_attr] == i) count++;
            }
			
            double stats = 1.*count/num_data;
            if (count > 0) 
                sum += -stats*Math.log(stats);
        }         
        return sum;
    }
    
    
	/*  This function checks if the specified attribute is used to decompose the data set
     in any of the parent_nodes of the specfied node in the decomposition tree.
     Recursively checks the specified node as well as all parent_nodes
     */
    public boolean DecompTree(final TNode node, final int num_attr) {
        
        if (node.child_node != null) {
			
            if (node.decompAttr == num_attr )
                return true;
        }
        
        if (node.parent_node == null) 
            return false;
		
        return DecompTree(node.parent_node, num_attr);
    }
    
	
    public void decomposeNode(TNode node) {  // This function decomposes the specified node according to ID3
        
        double best_Entr=0; 
        int numinputattr_values = attr-1;          
        int sel_attr=0;
        int num_data = node.set.size();
        boolean div_sel=false;//
        
        node.entropy = calculateEntropy(node.set);
        
        if (node.entropy == 0) return;
        
        for (int i=0; i< numinputattr_values; i++) {
			
            int num_val = v[i].size();
            if ( DecompTree(node, i) ) continue;
            
            double avg_entropy = 0;
            
            for (int j=0; j< num_val; j++) 
            {
                Vector subset = getSubset(node.set, i, j);
                if (subset.size() == 0) continue;
                double subentropy = calculateEntropy(subset);
                avg_entropy += subentropy * subset.size();  // Weighted sum
            }
            
            avg_entropy = avg_entropy / num_data;   // Taking the weighted average
            
            
            if (div_sel == false) {
                div_sel = true;
                best_Entr = avg_entropy;
                sel_attr = i;
            } else {
                
                if (avg_entropy < best_Entr) {
                    div_sel = true;
                    best_Entr = avg_entropy;
                    sel_attr = i;
                }
            }
            
        }
        
        if (div_sel == false) return;
        
        /*divide the dataset using the selected attribute*/
        
        int num_val = v[sel_attr].size();
        node.decompAttr = sel_attr;
        node.child_node = new TNode [num_val];
        
        for (int j=0; j< num_val; j++) {
            node.child_node[j] = new TNode();
            node.child_node[j].parent_node = node;
            node.child_node[j].set = getSubset(node.set, sel_attr, j);
            node.child_node[j].decompValue = j;
        }
        
        
        for (int j=0; j< num_val; j++) {    
            decomposeNode(node.child_node[j]);
            
        }
        node.set = null;
        
        
    }//end function
    
    
    /* First line of training set should contain names of all the attributes.*/
    
    
    public int readData(String fname) throws IOException {
        
        FileInputStream in = null;
        
        try 
        {
            File myFile = new File(fname);  //input file
            in = new FileInputStream(myFile);
            
        } catch ( Exception e) {
			
            System.out.println( "File not found " + fname + "\n" + e);
            return 0;
            
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(in) );
        
        String input;
        
        while(true) {
            
            input = reader.readLine();
            
            if (input == null) {
                System.out.println( " Empty training set file" + fname + "\n");
                return 0;
            }
            if(input.trim().startsWith("//")) 
                continue;
            if(input.equals("")) 
                continue;
            break;
        }
        
        StringTokenizer st = new StringTokenizer(input);
        attr = st.countTokens();
        if (attr <= 1) {
            System.out.println( "Read line: " + input);
            System.out.println( "No attribute found");
            System.out.println( "Line can't be empty");
            
            return 0;
        }
        
        v = new Vector[attr];
        
        for (int i=0; i < attr; i++) 
            v[i] = new Vector();
        attr_name = new String[attr];
        
        for (int i=0; i < attr; i++) 
        {
            attr_name[i]  = st.nextToken();
        }
        
        
        while(true) {
            
            input = reader.readLine();
            if (input == null) 
                break;
			
            if (input.trim().startsWith("//")) //ignore the comment line
                continue;
            if (input.trim().startsWith("\t")) 
                continue;
            
            
            if (input.equals("")) 
                continue;
            
            st = new StringTokenizer(input);
			
            int numtokens = st.countTokens();
            if (numtokens != attr) {
                
                System.out.println( "Read " + root.set.size() + " Training sets");
                System.out.println( "Last line read: " + input);
                System.out.println( "Expecting " + attr  + " attr_values");
				
                return 0;
            }
            
            
            Data point = new Data(attr);
            
            for (int i=0; i < attr; i++) 
            {
                point.attr_values[i]  = getAttributeValue(i, st.nextToken() );
                
            }
            root.set.addElement(point);
            
        }
        
        reader.close();
        
        int completeTable=0;
        int counter =0;
        int mycount[] = new int [v.length];
        for (int j=0; j<v.length-1; j++){
            
            for (int i=0; i<v[j].size(); i++)
            {
                counter++;	
            }
            mycount[j] = counter;
            if (completeTable<1)
            {
                completeTable=mycount[j];
            }
            else {
                completeTable=completeTable*mycount[j];
            }
            counter=0;
            
            
        }
        
        System.out.println("\n\n SIZE OF THE COMPLETE TABLE = " +completeTable + "\n\n");
        
        
        return 1;
        
   	}//End function
    
    
	//This function prints the decision tree in the form of rules.
    
    public void decisionTree(TNode node, Writer output, String tab) {
        
        
        int outputattr = attr-1;
        
        try
        {
            
            if (node.child_node == null) 
            {
                int values[] = getAllValues(node.set, outputattr);
                
                if (values.length == 1) 
                {
                    System.out.println(tab + "\t" + attr_name[outputattr] + " = \"" + v[outputattr].elementAt(values[0]) + "\";");
                    output.write(tab + "\t" + attr_name[outputattr] + " = \"" + v[outputattr].elementAt(values[0]) + "\";");
                    return;
                }
                
                System.out.print(tab + "\t" + attr_name[outputattr] + " = {");
                output.write(tab + "\t" + attr_name[outputattr] + " = {");
                
                for (int i=0; i < values.length; i++) 
                {
                    System.out.print("\"" + v[outputattr].elementAt(values[i]) + "\" ");
                    output.write("\"" + v[outputattr].elementAt(values[i]) + "\" ");
                    
                    if ( i != values.length-1 )
                    {
                        System.out.print( " , " );
                        output.write( " , " );
                    }
                    
                }
                
                System.out.println( " };");
                output.write( " };");
                
                return;
                
            }
            
            int num_val = node.child_node.length;
            
            for (int i=0; i < num_val; i++) 
            {
                
                System.out.println(tab + "if( " + attr_name[node.decompAttr] + " == \"" +
                                   v[node.decompAttr].elementAt(i) + "\") {" );
                output.write (tab + "if( " + attr_name[node.decompAttr] + " == \"" +
                              v[node.decompAttr].elementAt(i) + "\") {" );
                
                
                decisionTree(node.child_node[i], output, tab + "\t");
                if (i != num_val-1) 
                {System.out.print(tab +  "} else ");
                    
                    output.write(tab +  "} else ");
                }
                
                else
                {
                    System.out.println(tab +  "}");
                    output.write(tab +  "}");
                }
                
                
                
            }
            
        }
        catch (IOException e)
        {
            System.out.println(" Invalid ");		
        }
	}
    
    
    public void createDecisionTree() {    // Create Decision tree
        
        decomposeNode(root);
        
        try
        {
            
            Writer output= null;
            File file = new File("ComputeDecisiontree.txt"); // compute tree in a file
            output = new BufferedWriter(new FileWriter(file));
            decisionTree(root,output, ""); //print tree if else clause
            
            output.close();
        }
        catch (IOException e)
        {
            System.out.println(" Please check the file ");		
        }
        
    }
    
    
    public static void main(String[] args) throws IOException {
        
        int filename;
        int s;
        
        
        filename = args.length;
        
        while(filename != 1) {
            System.out.println("  Please give the training set name:  ");
            return;
        }
        
        FirstProg me = new FirstProg();
        
        s = me.readData(args[0]);
        while(s <= 0) return;
        
        me.createDecisionTree();
        
   	}
   	
}

