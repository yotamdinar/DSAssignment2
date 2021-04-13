public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        int step = 0;
        for (int i = 0; i < arr.length; i++) { // beginning of the step
            if (step == forward) {
                for (int j = 0; j < back; j++)
                    i = (int)myStack.pop();
                step = 0;
            }
            if (arr[i] == x)
                return i;
            myStack.push(i);
            step++;
            // end of step
        }
        return -1;
    }

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
        int low = 0;
        int high = arr.length - 1;
        while (high >= low) {
            int mid = (low + high) / 2;
            int inconsistencies = Consistency.isConsistent(arr);
            while (inconsistencies > 0 & !myStack.isEmpty()) {
                int lastMid = (Integer) myStack.pop();
                if (mid > lastMid)
                    low = 2 * lastMid - high;
                else
                    high = 2 * lastMid - low;
                mid = lastMid;
                inconsistencies--;

            }
            if (arr[mid] > x) {
                high = mid - 1;
            } else if (arr[mid] < x)
                low = mid + 1;
            else
                return mid;
            myStack.push(mid);
        }
        return -1;
    }
}
