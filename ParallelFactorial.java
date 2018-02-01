public class ParallelFactorial implements Runnable {

    private int output = 1;
    private int[] array;
    private int start;
    private int end;

    public ParallelFactorial(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    //calls computeFactorial and writes that value back into the array
    public void run() {
        for (int i = start; i <= end; i++) {
            //reset output variable
            output = 1;
            array[i] = computeFactorial(array[i]);
        }
    }

    //computes the factorial
    public int computeFactorial(int input) {
        for (int i = 1; i <= input; i++) {
            output = output * i;
        }
        return output;
    }
}
