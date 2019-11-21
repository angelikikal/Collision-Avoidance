//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class knn {
//    private int Z_COORDINATE_INDEX=2;
//    private int X_COORDINATE_INDEX=0;
//    private int Y_COORDINATE_INDEX=1;
//    float[] TrainingData= {5, 10,15,1};
//    float[] TrainingData2= {3,5,13,2};
//    float[] TrainingData3= {100,100,150,1};
//    float[] dataPoint= {6,6,6};
//    private int size;
//    public List<float[]> trainingData = new ArrayList<>();
//
//    private double sizeDouble;
//    private double root;
//    private List<Float> distances=new ArrayList<Float>();
//    private List<float[]> CLASS1=new ArrayList<float[]>();
//    private List<float[]> CLASS2=new ArrayList<float[]>();;
//    private List<Float> distancesClone=new ArrayList<Float>();;
//    // KNN GIA TRAINING DATA SET APO 3 SAMPLES  , GIA CLASSIFICATION 2 CLASSEWN
//    //EUKLEIDIA APOSTASH TROPOS #1
//    public Float getDistanceBetween(float[] dataElement1,float[] dataElement2) {
//        int x1 =  Math.round(dataElement1[X_COORDINATE_INDEX]   ) ;
//        int y1 =  Math.round(dataElement1[Y_COORDINATE_INDEX]   ) ;
//        int z1 =  Math.round(dataElement1[Z_COORDINATE_INDEX ] ) ;
//        int x2 =   Math.round(dataElement2[X_COORDINATE_INDEX]   ) ;
//        int y2 =  Math.round(dataElement2[Y_COORDINATE_INDEX]   ) ;
//        int z2 =  Math.round(dataElement2[Z_COORDINATE_INDEX]   ) ;
//
//        int term1 = (x2 - x1) * (x2 - x1);
//        int term2 = (y2 - y1) * (y2 - y1);
//        int term3 = (z2 - z1) * (z2 - z1);
//        int sum = term1 + term2 + term3;
//        String convertedSum = Integer.toString(sum);
//        double convertedToDoubleSum = Double.parseDouble(convertedSum);
//        double distance = Math.abs (Math.sqrt (convertedToDoubleSum ) );
//        String convertedDistance = Double.toString(distance);
//        System.out.println("h apostash einai :"+convertedDistance);
//        return Float.parseFloat(convertedDistance);
//    }
//    //EUKLEIDIA APOSTASH
//    public static double calculateDistance(double[] array1, double[] array2)
//    {
//        double Sum = 0.0;
//        for(int i=0;i<array1.length;i++) {
//            Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
//        }
//        System.out.println("eukleidia apostash :"+Math.sqrt(Sum));
//        return Math.sqrt(Sum);
//    }
//
//    public void FindClass(double [][] trainning , double[] test)
//    {
//
//        trainingData.add(TrainingData);
//        trainingData.add(TrainingData2);
//        trainingData.add(TrainingData3);
//        for (int index = 0; index < trainingData.size(); index++) {
//           // float distance = getDistanceBetween(dataPoint, trainingData.get(index));
//            double distance =calculateDistance(dataPoint,trainingData.get(index));
//            System.out.println("H apostash gia to sample :"+index+" einai "+distance);
//            distances.add((float) distance); //lista me apostaseis pou tha ginei sort
//            distancesClone.add((float) distance); //dhmiourgoume enan klwno poy den tha ginei sort gia na vroume tis theseis
//        }
//        //sort tis apostaseis
//        Collections.sort(distances);
//        //Mono kai mono gia na ta doume sti konsola ! ~ proairetiko
//        for(int i=0;i<distances.size();i++)
//        {
//            System.out.println("apostashhh : "+distances.get(i));
//        }
//        //Ypologismos K
//        //int K = determineK() ;
//        int K=7;
//        //Analoga me to K pairnoume tis K prwtes apostaseis apo tin sorted List
//        List<Float >shortestDistances= distances.subList( 0 , K  ) ;
//        //Gia kathe apostash ths shortestdistances
//        for ( float element : shortestDistances ) {
//            //vriskoume thn thesi ths ston athikto klwno pou ftiaksame
//            Integer indexOnClone = distancesClone.indexOf(element);
//            //wste na paroume to sample pou antistoixei sthn apostash
//            float[] nearestNeighbour = trainingData.get(indexOnClone);
//            //an anhkei sthn klash 1...
//            if (nearestNeighbour [ 3] == 1) {
//                CLASS1.add( nearestNeighbour ) ;
//            }
//            //an anhkei sthn klash 2...
//            else if (nearestNeighbour [ 3] == 2) {
//                CLASS2.add( nearestNeighbour ) ;
//            }
//        }
//        if ( CLASS1.size() > CLASS2 .size() ){
//            // return CLASS1
//            System.out.println("class 1 is the winner");
//        }
//        else {
//            System.out.println("Class 2 is the winner");
//            //   return CLASS2
//
//        }
//    }
//}