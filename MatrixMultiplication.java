import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class MatrixMultiplication {

    public static void main(String[] args) {
        //output file name
        String fn = "out.txt";
        
        //Change these values to vary the matrix sizes
        int m = 16017;
        int n = 13;
        int o = 10017;
        
        //start time for timers
        long seqStartTime;
        long parStratTime;
        
        //end time for timers
        long seqEndTime;
        long parEndTime;
        
        //input matrices
        int a[][] = new int[m][n];
        int b[][] = new int[n][o];

        //output matrix
        int c[][] = new int[m][o];

        int d[][] = new int[m][o];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = j % 10;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < o; j++) {
                b[i][j] = j % 10;
            }
        }


        //multiply matrices sequentially
        seqStartTime = System.nanoTime();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < o; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        seqEndTime =  System.nanoTime() - seqStartTime;

        //start parallel timer
        parStratTime = System.nanoTime();

        // create ParallelMatrix
        ParallelMatrix pm[] = new ParallelMatrix[m];

        for (int i = 0; i < m; i++) {
            pm[i] = new ParallelMatrix(a, b, d, i);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < m; i++) {
            executorService.execute(pm[i]);
        }

        executorService.shutdown();

        try {
            boolean tasksEnded = executorService.awaitTermination(1, TimeUnit.MINUTES);

            if (tasksEnded) {
           
            } else {
                System.out.println("timeout");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        parEndTime = System.nanoTime() - parStratTime;


        System.out.println("\nSequential time: " + seqEndTime / 1000000.0 + " milliseconds.");
        System.out.println("Parallel time: " + parEndTime / 1000000.0 + " milliseconds");
        
        try {
            FileWriter fw = new FileWriter(fn, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("Size: "+m+" x "+n+", "+n+" x "+o
                    +", Sequential execution time: "+seqEndTime/1000000.0
                    +", Parallel execution time: "+parEndTime/1000000.0);
            pw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
