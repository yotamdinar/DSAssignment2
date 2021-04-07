import javax.imageio.stream.ImageInputStream;

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    private int pointer;
    private int backtrackIndex;
    private Boolean backtrackInsert;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        pointer = 0;
        backtrackIndex = -1;
        backtrackInsert = null;
    }

    @Override
    public Integer get(int index) {
        return arr[index];
    }

    @Override
    public Integer search(int k) {
        int index = findIndexToInsert(k);
        if (arr[index] == k)
            return index;
        else
            return -1;
    }

    @Override
    public void insert(Integer x) {
        if (pointer == 0) {
            arr[pointer] = x;
            backtrackIndex=0;
            backtrackInsert=true;
            pointer++;
        }
         else if (search(x) == -1) {
            if (!checkSize())
                throw new RuntimeException("Set storage is full");
            int index = findIndexToInsert(x);
            backtrackIndex = index;
            backtrackInsert=true;
            for (int i = pointer; i > index; i--) {
                arr[i] = arr[i - 1];
            }
            arr[index] = x;
            pointer++;
        }
    }

    @Override
    public void delete(Integer index) {
        if (!isLegalIndex(index))
            throw new IndexOutOfBoundsException("Can not preform delete operation");
        stack.push(arr[index]);
        backtrackInsert=false;
        for (int i=index;i<pointer-1;i++){
            arr[i] = arr[i+1];
        }
        pointer--;
    }

    @Override
    public Integer minimum() {
        if (pointer>0)
            return arr[0];
        else
            throw new RuntimeException("Set is empty");
    }

    @Override
    public Integer maximum() {
        if (pointer>0)
            return arr[pointer-1];
        else
            throw new RuntimeException("Set is empty");
    }

    @Override
    public Integer successor(Integer index) {
        if (!isLegalIndex(index))
            throw new IndexOutOfBoundsException();
        if (index + 1 >= pointer)
            throw new IllegalArgumentException("No successor available");
        return arr[index + 1];
    }

    @Override
    public Integer predecessor(Integer index) {
        if (!isLegalIndex(index))
            throw new IndexOutOfBoundsException();
        if (index == 0 | index >= pointer) // index = pointer will not lead to an error but logically wrong - no predecessor
            // to an element that does not exist.
            throw new IllegalArgumentException("No predecessor available");
        return arr[index - 1];
    }

    @Override
    public void backtrack() {
        if (backtrackInsert == null) // no operation to backtrack
            return;
        if (backtrackInsert == Boolean.TRUE) { // backtrack an insert operation
            delete(backtrackIndex);
            stack.clear();
        }
        if (backtrackInsert == Boolean.FALSE && !stack.isEmpty()) { //backtrack a delete operation
            int item = (Integer) stack.pop();
            insert(item);
        }
        backtrackInsert=null;
        stack.clear();
        backtrackIndex = -1;
    }

    @Override
    public void retrack() {
        /////////////////////////////////////
        // Do not implement anything here! //
        /////////////////////////////////////
    }

    @Override
    public void print() {
        String output = "";
        for (int i = 0; i < pointer; i++) {
            output += (arr[i] + " ");
        }
        if (output.length()>0)
            System.out.println(output.substring(0, output.length() - 1));
        else
            System.out.println("");
    }

    public boolean checkSize() {
        return pointer < arr.length;
    }

    public int findIndexToInsert(int k) {
        int left = 0;
        int right = pointer - 1;
        int mid = 0;
        while (left <= right) {
            mid = (left + right) / 2;
            if (arr[mid] < k)
                left = mid + 1;
            else if (arr[mid] > k)
                right = mid - 1;
            else
                return mid;
        }
        if (mid<pointer && arr[mid]<k)
            mid++;
        return mid;
    }
    public boolean isLegalIndex(int index) {
        return (index < pointer && index >= 0);
    }

    public static void main(String[] args) {
        BacktrackingSortedArray array = new BacktrackingSortedArray(new Stack(), 10);
    }

}