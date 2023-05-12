/**
 * The Max_Min_Heap implements a max-min-heap. <br>
 * Includes a heap keys array, and a heap size tracker.
 *
 * @author Daniel Volkov
 */
public class Max_Min_Heap{
    public final int MAX_SIZE = 512; //In the forums, it was said that it is okay to bound the heap's length at 512.
    
    //Object's fields are public - hurts encapsulation, but makes implementation easier.
    public int []heapVals = new int[MAX_SIZE];
    public int heapSize;
    
    public Max_Min_Heap(int []vals){
        this.heapVals = vals;
        this.heapSize = vals.length;
    }

    public static int Depth(int i){
        return (int)(Math.log(i+1)/Math.log(2)); //The depth of a given index is it's base 2 log. This line gets the base 2 log using logarithmic identities.
    }
    public static int Left(int i){
        return 2*i + 1;
    }
    public static int Right(int i){
        return 2*i + 2;
    }
    public static int Parent(int i){
        return (int)((i-1)/2);
    }

    public static void Build_Heap(Max_Min_Heap A){
        for (int i = (int)((A.heapSize + 1) / 2); i >= 0; i--){
            Max_Min_Heap.Heapify(A,i);
        }
    }

    public static void Heapify(Max_Min_Heap A, int i){
        if( i<0 || i >= A.heapSize){ //Check for illegal given index.
            return
        }
        if((Depth(i)%2) == 1){
            Max_Min_Heap.Heapify_min(A,i);
        }else{
            Max_Min_Heap.Heapify_max(A,i);
        }
    }
    
    private static void Heapify_min(Max_Min_Heap A, int i){
        if(Max_Min_Heap.Left(i) < A.heapSize || Max_Min_Heap.Right(i) < A.heapSize){
            int min_grand = Max_Min_Heap.smallest_grand(A,i);
            if (Max_Min_Heap.isGrandChild(A,min_grand,i)){ //The smallest element is the current item's grandson.
                if(A.heapVals[min_grand] < A.heapVals[i]){ //The minimal grandson will be the minimum of the whole sub-heap, if we dont have a smaller value.
                    Max_Min_Heap.swap(A, min_grand, i);

                    if(A.heapVals[min_grand] > A.heapVals[Max_Min_Heap.Parent(min_grand)]){ //Fix the possible issues with the swap between an element and it's grandson.
                        Max_Min_Heap.swap(A, min_grand, Max_Min_Heap.Parent(min_grand));
                    }
                    Max_Min_Heap.Heapify(A, min_grand); //Continue fixing the heap from the grandson.
                }
            }
            else{ //The smallest element is the current item's son. (means there are no grandsons according to max-min-tree definition - because his sons are the maximums of their sub-heaps)
                if(A.heapVals[min_grand] < A.heapVals[i]){
                    Max_Min_Heap.swap(A, min_grand, i);
                }
            }
        }
    }

    private static void Heapify_max(Max_Min_Heap A, int i){
        if(Max_Min_Heap.Left(i) < A.heapSize || Max_Min_Heap.Right(i) < A.heapSize){
            int max_grand = Max_Min_Heap.biggest_grand(A,i);
            if (Max_Min_Heap.isGrandChild(A,max_grand,i)){ //The biggest element is the current item's grandson.
                if(A.heapVals[max_grand] > A.heapVals[i]){ //The maximal grandson will be the maximum of the whole sub-heap, if we dont have a bigger value.
                    Max_Min_Heap.swap(A, max_grand, i);

                    if(A.heapVals[max_grand] < A.heapVals[Max_Min_Heap.Parent(max_grand)]){ //Fix the possible issue with the swap between an element and it's grandson.
                        Max_Min_Heap.swap(A, max_grand, Max_Min_Heap.Parent(max_grand));
                    }
                    Max_Min_Heap.Heapify(A, max_grand); //Continue fixing the heap from the grandson.
                }
            }
            else{ //The biggest element is the current item's son. (means there are no grandsons according to max-min-tree definition - because his sons are the minimums of their sub-heaps)
                if(A.heapVals[max_grand] > A.heapVals[i]){
                    Max_Min_Heap.swap(A, max_grand, i);
                }
            }
        }
    }

    private static int smallest_grand(Max_Min_Heap A,int i){
        int min = i;
        if((Left(i) < A.heapSize) && (A.heapVals[Left(i)] < A.heapVals[min])){
            min = Left(i);
        }
        if((Right(i) < A.heapSize) && (A.heapVals[Right(i)] < A.heapVals[min])){
            min = Right(i);
        }
        
        for(int j= 4*i + 3;j <= 4*i+6;j++){
            if(j >= A.heapSize){
                break;
            }
            if(A.heapVals[j] < A.heapVals[min]){
                min = j;
            }
        }
        return min;
    }
    private static int biggest_grand(Max_Min_Heap A,int i){
        int max = i;
        if((Left(i) < A.heapSize) && (A.heapVals[Left(i)] > A.heapVals[max])){
            max = Left(i);
        }
        if((Right(i) < A.heapSize) && (A.heapVals[Right(i)] > A.heapVals[max])){
            max = Right(i);
        }
        
        for(int j= 4*i + 3;j <= 4*i+6;j++){
            if(j >= A.heapSize){
                break;
            }
            if(A.heapVals[j] > A.heapVals[max]){
                max = j;
            }
        }
        return max;
    }
    

    private static boolean isGrandChild(Max_Min_Heap A, int q,int i){
        return ((int)((q-3)/4) == i); 
    }
    private static void swap(Max_Min_Heap A, int i,int j){
        int temp = A.heapVals[i];
        A.heapVals[i] = A.heapVals[j];
        A.heapVals[j] = temp;
    }
}