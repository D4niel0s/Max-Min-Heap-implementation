/**
 * The Max_Min_Heap implements a max-min-heap. <br>
 * Includes a heap keys array, and a heap size tracker.
 *
 * @author Daniel Volkov
 */
import java.util.Scanner;
public class Max_Min_Heap{
    public static final int MAX_SIZE = 512; //In the forums, it was said that it is okay to bound the heap's length at 512.
    
    //Object's fields are public - hurts encapsulation, but makes implementation easier.
    public int []heapVals;
    public int heapSize;
    
    public Max_Min_Heap(){
        this.heapVals = new int[MAX_SIZE];
        Scanner scan = new Scanner(System.in);
        String vals = scan.nextLine();
        
        String []values = vals.split(" ",-1);
        for (int j=0; j<values.length; j++){
            this.heapVals[j] = Integer.parseInt(values[j]);
        }
        
        this.heapSize = values.length;
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
    public static int GrandP(int i){
        return (int)((i-3)/4);
    }
    public static boolean hasGP(int i){
        return GrandP(i) >= 0;
    }

    public static void Heap_Delete(Max_Min_Heap A,int i){
        swap(A, A.heapSize-1, i);
        A.heapSize--;

        Heapify(A,i);
    }

    public static void Heap_Insert(Max_Min_Heap A,int key){ //Doesn't allocate new memory for the new element. assumes memory exists. (said to be OK in the forums)(A.heapVals is bounded at 512 elements)
        A.heapSize++; //"Create" new node at the end of the heap.
        A.heapVals[A.heapSize-1] = key; //Copy value into the new node.

        bubbleUp(A, A.heapSize-1); //Fix the heap by bubbling the new node up.
    }

    private static void bubbleUp(Max_Min_Heap A,int i){
        if(i > 0){
            if(Depth(i) % 2 == 1){ //The current node's parent should be the max of the sub-heap
                if(A.heapVals[Parent(i)] < A.heapVals[i] ){ //Parent violates heap attributes
                    swap(A, Parent(i), i); //Fix parent's violation
                    bubbleUp_Max(A, Parent(i)); //Bubble the (now valid) parent up
                }else{
                    bubbleUp_Min(A, i); //Parent and current node ok, bubble current node up
                }
            }else{
                if(A.heapVals[Parent(i)] > A.heapVals[i]){ //The current node's parent should be the min of the sub-heap
                    swap(A, Parent(i), i); //Same procedure here, analogous to previous situation
                    bubbleUp_Min(A, Parent(i));
                }else{
                    bubbleUp_Max(A, i);
                }
            }
        }
    }

    private static void bubbleUp_Min(Max_Min_Heap A,int i){
        if (!hasGP(i)){return;}

        if(A.heapVals[GrandP(i)] > A.heapVals[i]){
            swap(A, GrandP(i), i);
            bubbleUp_Min(A, GrandP(i));
        }
    }
    private static void bubbleUp_Max(Max_Min_Heap A,int i){
        if(!hasGP(i)){return;}

        if(A.heapVals[GrandP(i)] < A.heapVals[i]){
            swap(A, GrandP(i), i);
            bubbleUp_Max(A, GrandP(i));
        }
    }

    public static int Heap_Extract_Min(Max_Min_Heap A){
        int minInd = Math.min(A.heapVals[1],A.heapVals[2]) == A.heapVals[1] ? 1 : 2; //Get the index of the min element. (min element is one of the sons of the root)
        int minVal = A.heapVals[minInd]; //Save minimum Val.

        swap(A,minInd,A.heapSize-1); //Swap the minimum with the last leaf.
        A.heapSize--; //"Remove" last leaf from heap.

        Heapify(A,minInd); //Fix sub-heap rooted in the former minimum's index.

        return minVal; //Return former minimum.
        
    }

    public static int Heap_Extract_Max(Max_Min_Heap A){
        int max = A.heapVals[0];
        swap(A, 0, A.heapSize-1); //Swap root with last leaf.
        A.heapSize--; //"Delete" last leaf. (We don't really delete it we just decrease heapSize so it's not "included" in the heap)

        Heapify(A,0); //The root may violate the max-min heap's attributes. (the sub-heap below the root is ok' beacause we did not change it)

        return max; //Return the removed maximum.
    }

    public static void Build_Heap(Max_Min_Heap A){
        for (int i = (int)((A.heapSize + 1) / 2); i >= 0; i--){
            Heapify(A,i);
        }
    }

    public static void Heapify(Max_Min_Heap A, int i){
        if( i<0 || i >= A.heapSize){ //Check for illegal given index.
            return;
        }
        if((Depth(i)%2) == 1){
            Heapify_min(A,i);
        }else{
            Heapify_max(A,i);
        }
    }
    
    private static void Heapify_min(Max_Min_Heap A, int i){
        if(Left(i) < A.heapSize || Right(i) < A.heapSize){
            int min_grand = smallest_grand(A,i);
            if (isGrandChild(A,min_grand,i)){ //The smallest element is the current item's grandson.
                if(A.heapVals[min_grand] < A.heapVals[i]){ //The minimal grandson will be the minimum of the whole sub-heap, if we dont have a smaller value.
                    swap(A, min_grand, i);

                    if(A.heapVals[min_grand] > A.heapVals[Parent(min_grand)]){ //Fix the possible issues with the swap between an element and it's grandson.
                        swap(A, min_grand, Parent(min_grand));
                    }
                    Heapify(A, min_grand); //Continue fixing the heap from the grandson.
                }
            }
            else{ //The smallest element is the current item's son. (means there are no grandsons according to max-min-tree definition - because his sons are the maximums of their sub-heaps)
                if(A.heapVals[min_grand] < A.heapVals[i]){
                    swap(A, min_grand, i);
                }
            }
        }
    }

    private static void Heapify_max(Max_Min_Heap A, int i){
        if(Left(i) < A.heapSize || Right(i) < A.heapSize){
            int max_grand = biggest_grand(A,i);
            if (isGrandChild(A,max_grand,i)){ //The biggest element is the current item's grandson.
                if(A.heapVals[max_grand] > A.heapVals[i]){ //The maximal grandson will be the maximum of the whole sub-heap, if we dont have a bigger value.
                    swap(A, max_grand, i);

                    if(A.heapVals[max_grand] < A.heapVals[Parent(max_grand)]){ //Fix the possible issue with the swap between an element and it's grandson.
                        swap(A, max_grand, Parent(max_grand));
                    }
                    Heapify(A, max_grand); //Continue fixing the heap from the grandson.
                }
            }
            else{ //The biggest element is the current item's son. (means there are no grandsons according to max-min-tree definition - because his sons are the minimums of their sub-heaps)
                if(A.heapVals[max_grand] > A.heapVals[i]){
                    swap(A, max_grand, i);
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