public class Warmup2 {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {

        int i = 0;
        int ans = -1;
        while (i < arr.length - forward+1){
            ans = forwardSearch(arr, x, i, forward, myStack);
            backward(arr, x,back, myStack);
            i = i + forward - back;
        }
        return ans;
    }

    /**serach for x in 'forward' indexes starting with 'startIndex'
     * in other words, doing 'forward' search steps
     */
    public static int forwardSearch(int[] arr, int x, int startIndexs, int forward, Stack mystack){
        for ( int i = startIndexs; i < arr.length && i < startIndexs + forward; i++){
            mystack.push(arr[i]);
            if (arr[i] == x){ //this line is a step
                return i;
            }
        }
        return -1;
    }

    /**return 'back' steps backwards
     */
    public static void backward(int[] arr, int x, int back, Stack mystack){
        for (int i = 0; i < back ; i++) {
            mystack.pop();
        }
    }

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
        return conBinSearchRecursive(arr, x, myStack, 0, arr.length-1);
        }


    public static int conBinSearchRecursive(int[] arr, int x, Stack myStack, int low, int high) {
        return -1;
    }

}
