import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Str {
    void BubbleSort(double[] d,double[] l)
    {
        int n = d.length;
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                if(d[j]>d[j+1]){
                    double temp1=d[j];
                    double temp2=l[j];
                    d[j]=d[j+1];
                    l[j]=l[j+1];
                    d[j+1]=temp1;
                    l[j+1]=temp2;
                }
            }
        }
    }


    public boolean find(double[] d,double f){
        int j=0;
        for(int i=0;i<d.length;i++){
            if(d[i]==f) {
                j = 1;
                break;
            }
        }
        if(j==1){
            return true;
        }else{
            return false;
        }
    }

    public double[][] SmallestDistances(double[][] d,int rows,int columns,int num){
        double all[] = new double[(rows-1)*columns];
        for(int i = 1;i<rows;i++){
            for(int j = 0;j<columns;j++){
                all[(i-1)*j] = d[i][j];
            }
        }
        Arrays.sort(all);
        double labels[] = new double[num];
        double smallest[] = new double[num];
        for(int k=0;k<num;k++){
            for(int i=1;i<rows;i++){
                for(int j=0;j<columns;j++){
                    if(all[k]==d[i][j]){
                        labels[k] = d[1][j];
                        smallest[k] = d[i][j];
                    }
                }
            }
        }
        double[][] merged = new double[num][2];
        for(int i=0;i<num;i++){
            merged[i][0] = labels[i];
            merged[i][1] = smallest[i];
        }
        return merged;
    }

//    public Double[][] SmallestDistances(double[][] d, int rows, int columns, int num) {
//        double small = 1000000.0;
//        List<Double> L = new ArrayList<Double>();
//        List<Integer> I = new ArrayList<Integer>();
//        List<Integer> J = new ArrayList<Integer>();
//        int ismall = 1000000;
//        int jsmall = 1000000;
//        for (int k = 0; k < num; k++) {
//            for (int i = 0; i < rows; i++) {
//                for (int j = 1; j < columns; j++) {
//                    if (d[i][j] < small) {
//                        if (L.contains(d[i][j]) == false) {
//                            small = d[i][j];
//                            ismall = i;
//                            jsmall = j;
//                        } else {
//                            small = 1000000.0;
//                        }
//                    }
//                }
//            }
//            if (L.isEmpty()) {
//                L.add(small);
//            }
//            if (L.contains(small) == false) {
//                L.add(small);
//                I.add(ismall);
//                J.add(jsmall);
//            }
//        }
//        for(int i=0;i<L.size();i++){
//            System.out.println(L.get(i));
//        }
//        for(int i=0;i<I.size();i++){
//            System.out.println(I.get(i));
//        }
//        Double[][] S_L = new Double[7][];
//        S_L[0] = new Double[num];
//        S_L[1] = new Double[num];
//        for (int i = 0; i < num; i++) {
//            S_L[i][1] = L.get(i);
//            L.remove(i);
//            System.out.println("inside");
//            S_L[i][0] = d[I.get(i)][0];
//        }
//        return S_L;
//    }
//

    public static double calculateDistance(double[] array1, double[] array2)
    {
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
            Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        //System.out.println("eukleidia apostash :"+Math.sqrt(Sum));
        return Math.sqrt(Sum);
    }
    int Classification(double[] d,double[] l, int k) {
        double w_open=0.0;
        double w_closed=0.0;
        int votes_open=0;
        int votes_closed=0;
        for(int i=0;i<k;i++){
            if(l[i]==0.0){
                w_closed = w_closed + (1/d[i]);
                votes_closed++;
            }else if(l[i]==1.0){
                w_open = w_open + (1/d[i]);
                votes_open++;
            }
        }
        if((votes_closed*w_closed)<(votes_open*w_open)){
            return 1;
        }else{
            return 0;
        }
    }


    double Correct_Percentage(double sum_of_csv, double sumofcorrect){

        Double onomacsv = new Double(0.0);


        double correct_perc =sumofcorrect/sum_of_csv;
        return correct_perc*100.0;

    }

}