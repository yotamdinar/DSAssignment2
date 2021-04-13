public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int pointer;
    private int minIndex;
    private int maxIndex;
    private int backtrackIndex;
    private Boolean backtrackInsert;

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        pointer = 0;
        minIndex = -1;
        maxIndex = -1;
        backtrackIndex = -1;
        backtrackInsert=null;
    }

    @Override
    public Integer get(int index) {
        return arr[index];
    }

    @Override
    public Integer search(int k) {
        for (int i = 0; i < pointer; i++) {
            if (arr[i] == k)
                return i;
        }
        return -1;
    }

    @Override
    public void insert(Integer x) {
        if (search(x) == -1) {
            if (!checkSize())
                throw new RuntimeException("Set storage is full");
            arr[pointer] = x;
            pointer++;
            backtrackInsert=Boolean.TRUE;
            backtrackIndex = pointer - 1;
            handleMinMaxInsert();
        }
    }

    @Override
    public void delete(Integer index) {
        if (!isLegalIndex(index))
            throw new IndexOutOfBoundsException("Can not preform delete operation");
        stack.push(arr[index]);
        backtrackIndex = index;
        arr[index] = arr[pointer - 1];
        pointer--;
        backtrackInsert=Boolean.FALSE;
        if (index == minIndex)
            handleMinDelete(index);
        if (index == maxIndex)
            handleMaxDelete(index);
    }

    @Override
    public Integer minimum() {
        if (minIndex!=-1)
            return arr[minIndex];
        else
            throw new RuntimeException("Set is empty");
    }

    @Override
    public Integer maximum() {
        if (maxIndex!=-1)
            return arr[maxIndex];
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
        if (backtrackInsert == Boolean.FALSE) { //backtrack a delete operation
            int item = (Integer) stack.pop();
            arr[pointer] = arr[backtrackIndex];
            arr[backtrackIndex] = item;
            pointer++;
            handleMinMaxInsert();
        }
        if (backtrackInsert == Boolean.TRUE) // backtrack an insert operation
            delete(backtrackIndex);
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

    /**
     * @return true if there is room to insert another element to the set.
     */
    public boolean checkSize() {
        return pointer < arr.length;
    }

    /**
     * @param index: the index to be checked
     * @return true if index  is legal (inside array legal boundaries), or false otherwise.
     */
    public boolean isLegalIndex(int index) {
        return (index < pointer && index >= 0);
    }

    /**
     * the function updates attributes minIndex, maxIndex after insertion to the Set.
     */
    public void handleMinMaxInsert() {
        int newElementIndex = pointer - 1;
        //determine minIndex after insertion
        if (minIndex != -1) {
            if (arr[newElementIndex] < arr[minIndex]) //determine if minIndex should be updated
                minIndex = newElementIndex;
            if (arr[newElementIndex] > arr[maxIndex]) //determine if maxIndex should be updated
                maxIndex = newElementIndex;
        }
        else {
            // of course maxIndex is also -1 at this point
            minIndex = newElementIndex;
            maxIndex = newElementIndex;
        }
    }

    public void handleMinDelete(int index) {
        if (pointer == 1)
            minIndex = -1;
        else {
            minIndex=0;
            for (int i = 0; i < pointer; i++) {
                if (arr[i] < arr[minIndex])
                    minIndex = i;

            }
        }
    }

    public void handleMaxDelete(int index) {
        if (pointer == 1) {
            maxIndex = -1;
        } else {
            maxIndex = 0;
            for (int i = 0; i < pointer; i++) {
                if (arr[i] > arr[maxIndex])
                    maxIndex = i;
            }
        }
    }

    public static void main(String[] args) {
        BacktrackingArray array = new BacktrackingArray(new Stack(), 10);
    }
}

