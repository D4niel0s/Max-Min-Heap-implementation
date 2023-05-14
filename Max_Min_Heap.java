import java.util.Scanner; //Import scanner for user input in constructor

public class Max_Min_Heap{
    public static final int MAX_SIZE = 512; //In the forums, it was said that it is okay to bound the heap's length at 512.
    
    //Object's fields are public - hurts encapsulation, but makes implementation easier.
    public int []heapVals;
    public int heapSize;
    
    public static void main(String[] args){
        int FLAG = 0;
        Scanner scan = new Scanner(System.in);
        int response;
        System.out.println("-----Welcome to maman 13 - Daniel Volkov-----\n\n\n");

        System.out.println("Enter values for heap: (all in one line, separated by spaces)");
        Max_Min_Heap HEAP = new Max_Min_Heap();
        Build_Heap(HEAP);

        System.out.print("The heap (after correction):\t");
        PrintHeap(HEAP);

        while(FLAG == 0){
            System.out.println("\nChoose the function you want:");
            System.out.println("1. Extract maximum from the heap");
            System.out.println("2. Extract minimum from the heap");
            System.out.println("3. delete a key from the heap");
            System.out.println("4. Insert a key to the heap");
            System.out.println("5. Use the given heap to sort it's keys (deletes heap)");
            System.out.println("6. build the heap again (overrides current values)");
            System.out.println("7. Exit");
            response = scan.nextInt();

            switch(response){
                case 1:
                    System.out.println("The heap's max is:\t" + Heap_Extract_Max(HEAP));
                    System.out.print("The new heap:\t");
                    PrintHeap(HEAP);
                    break;

                case 2:
                    System.out.println("The heap's min is:\t" + Heap_Extract_Min(HEAP));
                    System.out.print("The new heap:\t");
                    PrintHeap(HEAP);
                    break;

                case 3:
                    System.out.print("Enter index for removal:\t");
                    response = scan.nextInt();
                    Heap_Delete(HEAP,response);
                    System.out.print("The heap after removal:\t");
                    PrintHeap(HEAP);
                    break;

                case 4:
                    System.out.print("Enter key for insertion:\t");
                    response = scan.nextInt();
                    Heap_Insert(HEAP,response);
                    System.out.print("The heap after insertion:\t");
                    PrintHeap(HEAP);
                    break;

                case 5:
                    int []SORTED = new int[HEAP.heapSize];
                    HeapSort(HEAP,SORTED);
                    System.out.print("The sorted heap: (heap does not exist anymore)\t");

                    System.out.print("[");
                    for(int j=0;j<SORTED.length;j++){
                        System.out.print(SORTED[j]);
                        if(j != SORTED.length - 1){
                            System.out.print(",");
                        }
                    }
                    System.out.println("]");
                    break;

                case 6:
                    HEAP = new Max_Min_Heap();
                    Build_Heap(HEAP);

                    System.out.print("Heap built. New heap (after correction):\t");
                    PrintHeap(HEAP);
                    break;

                case 7:
                    FLAG = 1; //Break out the menu and end the program
                    break;

                default:
                    System.out.println("Invalid number.");
                    break;
            }
        }
    }


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

    public static void PrintHeap(Max_Min_Heap A){
        for(int i=0;i<A.heapSize;i++){
            if(i==0){
                System.out.print("[");
            }
            System.out.print(A.heapVals[i]);
            if(i != A.heapSize - 1){
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    //Returns the depth of a given node with index i
    public static int Depth(int i){
        return (int)(Math.log(i+1)/Math.log(2)); //The depth of a given index is it's base 2 log. This line gets the base 2 log using logarithmic identities.
    }

    //Returns the index of i's left son (indexes start from 0 so we add one)
    public static int Left(int i){
        return 2*i + 1;
    }

    //Returns the index of i's right son (indexes start from 0 so we add two)
    public static int Right(int i){
        return 2*i + 2;
    }

    //Returns the index of i's parent. (indexes start from 0 so we subtract one before dividing)
    public static int Parent(int i){
        return (int)((i-1)/2);
    }

    //Returns the index of the grandparent of i (indexes of the grand-children of a node with index i are: 4i+3 - 4i+6)
    public static int GrandP(int i){
        return (int)Math.floor(((i-3)/4.0));
    }

    //Checks if a given node i has a grandparent
    public static boolean hasGP(int i){
        return (GrandP(i) >= 0);
    }

    //Checks if q is a grand-child of i
    private static boolean isGrandChild(Max_Min_Heap A, int q,int i){
        return GrandP(q) == i; 
    }

    //Swaps two indexes from the heap A's heapVals array.
    private static void swap(Max_Min_Heap A, int i,int j){
        int temp = A.heapVals[i];
        A.heapVals[i] = A.heapVals[j];
        A.heapVals[j] = temp;
    }


    //Sorts the heap's keys, and stores the information in the "sorted" array. deletes heap, by extracting the maximum repeatedly.
    public static void HeapSort(Max_Min_Heap A, int []sorted){
        for(int i=0; i<sorted.length ; i++){
            sorted[sorted.length - i - 1] = Heap_Extract_Max(A); //Insert the max to the array from the end, and remove it from the heap.
        }
    }

    //Deletes an elememt from the heap. i - the index of the element.
    public static void Heap_Delete(Max_Min_Heap A,int i){
        swap(A, A.heapSize-1, i); //Swap desired index with last leaf
        A.heapSize--; //"Delete" last leaf

        Heapify(A,i); //Fix the heap.
    }

    //Insert a key to the heap.
    public static void Heap_Insert(Max_Min_Heap A,int key){ //Doesn't allocate new memory for the new element. assumes memory exists. (said to be OK in the forums)(A.heapVals is bounded at 512 elements)
        A.heapSize++; //"Create" new node at the end of the heap.
        A.heapVals[A.heapSize-1] = key; //Copy value into the new node.

        bubbleUp(A, A.heapSize-1); //Fix the heap by bubbling the new node up.
    }

    //Equivilent to heapify, but works upwards from the leaf.
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

    //Sub-routine - bubbles up a min leveled node, comparing it to it's grandparents.
    private static void bubbleUp_Min(Max_Min_Heap A,int i){
        if (!hasGP(i)){return;}

        if(A.heapVals[GrandP(i)] > A.heapVals[i]){
            swap(A, GrandP(i), i); //If the grandparent is a new minimum, swap the two keys.
            bubbleUp_Min(A, GrandP(i)); //Continue from the grandfather up
        }
    }

    //Sub-routine - bubbles up a max leveled node, comparing it to it's grandparents.
    private static void bubbleUp_Max(Max_Min_Heap A,int i){
        if(!hasGP(i)){return;}

        if(A.heapVals[GrandP(i)] < A.heapVals[i]){
            swap(A, GrandP(i), i); //If the grandparent is a new maximum, swap the two keys.
            bubbleUp_Max(A, GrandP(i)); //Continue from the grandfather up
        }
    }

    //Extract the minimum from a heap
    public static int Heap_Extract_Min(Max_Min_Heap A){
        int minInd = Math.min(A.heapVals[1],A.heapVals[2]) == A.heapVals[1] ? 1 : 2; //Get the index of the min element. (min element is one of the sons of the root)
        int minVal = A.heapVals[minInd]; //Save minimum Val.

        swap(A,minInd,A.heapSize-1); //Swap the minimum with the last leaf.
        A.heapSize--; //"Remove" last leaf from heap.

        Heapify(A,minInd); //Fix sub-heap rooted in the former minimum's index.

        return minVal; //Return former minimum.
        
    }

    //Extract the maximum from a heap
    public static int Heap_Extract_Max(Max_Min_Heap A){
        int max = A.heapVals[0];
        swap(A, 0, A.heapSize-1); //Swap root with last leaf.
        A.heapSize--; //"Delete" last leaf. (We don't really delete it we just decrease heapSize so it's not "included" in the heap)

        Heapify(A,0); //The root may violate the max-min heap's attributes. (the sub-heap below the root is ok' beacause we did not change it)

        return max; //Return the removed maximum.
    }

    //Correct the heapVals array stored in the Max_Min_Heap object, to be a valid max-min-heap.
    public static void Build_Heap(Max_Min_Heap A){
        for (int i = (int)((A.heapSize + 1) / 2); i >= 0; i--){
            Heapify(A,i); //This is the same algorithm from the book - with a modified heapify method.
        }
    }

    //Fixes the heap, starting at index i, and working downwards.
    public static void Heapify(Max_Min_Heap A, int i){
        if( i<0 || i >= A.heapSize){ //Check for illegal given index.
            return;
        }
        if((Depth(i)%2) == 1){ //Envoke corresponding function - that matches the depth of the node.
            Heapify_min(A,i);
        }else{
            Heapify_max(A,i);
        }
    }
    
    //Sub-routine - corrects mistakes with a node's parents, and continues fixing the heap from the smallest grand-child.
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

    //Sub-routine - corrects mistakes with a node's parents, and continues fixing the heap from the biggest grand-child.
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

    //Returns the smallest son/grandson of the current node (index i).
    private static int smallest_grand(Max_Min_Heap A,int i){
        int min = i;

        //Check each son if he's the minimum.
        if((Left(i) < A.heapSize) && (A.heapVals[Left(i)] < A.heapVals[min])){
            min = Left(i);
        }
        if((Right(i) < A.heapSize) && (A.heapVals[Right(i)] < A.heapVals[min])){
            min = Right(i);
        }
        
        //Traverse the grandchildren, and take the smallest. (classic min algorithm)
        for(int j= 4*i + 3;j <= 4*i+6;j++){
            if(j >= A.heapSize){
                break;
            }
            if(A.heapVals[j] < A.heapVals[min]){
                min = j;
            }
        }
        return min; //Return the minimal son/grandson index.
    }

    //Returns the biggest son/grandson of the current node (index i).
    private static int biggest_grand(Max_Min_Heap A,int i){
        int max = i;

        //Check each son if he's the maximum.
        if((Left(i) < A.heapSize) && (A.heapVals[Left(i)] > A.heapVals[max])){
            max = Left(i);
        }
        if((Right(i) < A.heapSize) && (A.heapVals[Right(i)] > A.heapVals[max])){
            max = Right(i);
        }
        
        //Traverse the grandchildren, and take the biggest. (classic max algorithm)
        for(int j= 4*i + 3;j <= 4*i+6;j++){
            if(j >= A.heapSize){
                break;
            }
            if(A.heapVals[j] > A.heapVals[max]){
                max = j;
            }
        }
        return max; //Return the maximal son/grandson index.
    }
}