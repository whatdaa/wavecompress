package compression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.MathContext;
import java.util.HashSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import ch.obermuhlner.math.big.*;
public class Testing {
    static HashMap<Integer,int[]> vars = new HashMap<>();
    static String[] binary = new String[256];
    static AtomicInteger largestn = new AtomicInteger(0);
    static AtomicInteger largesta = new AtomicInteger(0);
    static float largestb =0;
    //    static AtomicInteger largestc = new AtomicInteger(0);
//    static AtomicInteger largestd = new AtomicInteger(0);
//    static AtomicInteger largestspread = new AtomicInteger(0);
    static AtomicInteger largestsize = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(12);
        Runnable thr;
        ArrayList<Future> futures = new ArrayList<>();
        int min= 1;
        int max = 2;
        for(int pow=min;pow<max;pow++) {
            for (int a = 1 << (pow-1); a < 1 << (pow); a++) {
                thr= new Task(a,pow,largestsize.get());
                futures.add(pool.submit(thr));
            }
        }
        int ssize = futures.size();
        System.out.println(ssize+ " Initial Threads  \n" ); // estimate = (1 << max-(max-min+1))
        while(futures.size()!=0){
            futures.removeIf(Future::isDone);
            System.out.println(futures.size()+ " Threads left \n");
            //System.out.println("roughly "+ BigDecimal.valueOf(297528130221121800.26D*futures.size()).toBigInteger() + " operations left to do");
            System.out.println("percentage done: "+100*(double)(ssize-futures.size())/((double)ssize));
            Thread.sleep(10000);
        }
        System.out.println("current largest a: " + largesta.get());
        System.out.println("current largest b: " + largestb);
        //System.out.println("current largest c: " + largestc.get());
        //System.out.println("current largest d: " + largestd.get());
        System.out.println("current largest n: " + largestn.get());
        //System.out.println("current largest spread: " + largestspread.get());
        System.out.println("largest size being: " + largestsize.get());
        pool.shutdown();
//        for(int i=0;i<128;i++){
//            if(!nums.contains(i)){
//                System.out.println("does not contain: "+i);
//            }
//            else{
//                System.out.println("does contain: "+i);
//            }
//        }
//        System.out.println("size of set: "+nums.size());
//        for(String a : binary){
//            if(a!=null) {
//                System.out.println(a+" = "+Integer.parseInt(a,2)+" with i, g of:"+ Arrays.toString(vars.get(Integer.parseInt(a,2))));
//            }
//        }
    }

    static class Task implements Runnable {
        int a=0;
        int pow=0;
        static int loclargestsize;

        public Task(int a, int power, int largestsize){
            this.a=a;
            pow=power;
            loclargestsize = largestsize;
        }
        public static void main(String[] args) throws IOException {
            int sintmp;
            int nsintmp;
            int costmp;
            int ncostmp;
            HashSet<Integer> nums= new HashSet<>();
            HashMap<Integer, Integer> map = new HashMap<>();
            HashMap<Integer, Integer> dmap = new HashMap<>();
            File Logs = new File("Logs.txt");
            Logs.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(Logs, true));
            for(int i=0;i<256;i++){
                dmap.put(i,0);
            }
            int largestsize=253;
            boolean first=true;
            final BigDecimal MIN = new BigDecimal("0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
            for(float a = 1.0f; a < 4; a=Math.nextUp(a)) {
                BigDecimal num1 = BigDecimal.ONE;
                BigDecimal num2 = BigDecimal.ONE;
                BigDecimal b = BigDecimal.ZERO;
                //BigDecimal MIN = DefaultBigDecimalMath.divide(BigDecimal.ONE,BigDecimal.valueOf(Double.MAX_VALUE));
                //BigDecimal MIN = BigDecimal.valueOf(Double.MIN_VALUE);
                b=b.add(MIN);

                for ( ; b.compareTo(BigDecimal.valueOf(4))<=0; b=b.add(MIN)) {
                    if(first){
                        first=false;
                        b=new BigDecimal("0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000152684");
                        b=b.add(MIN);
                    }
                    nums.clear();
                    map= new HashMap<>(dmap);
                    for (int i = 1; i <= (1 << 8); i += 1) {
                        sintmp = 0;
                        nsintmp = 0;
                        costmp = 0;
                        ncostmp = 0;
                        for (int j = 1; j < 9; j++) {
                            //(j*a*1.0) / ((i*b*1.0) )
                            num1 = DefaultBigDecimalMath.multiply(BigDecimal.valueOf(a), BigDecimal.valueOf(j));
                            num2 = DefaultBigDecimalMath.multiply(b, BigDecimal.valueOf(i));
                            BigDecimal quotient = DefaultBigDecimalMath.divide(num1, num2);
                            quotient=reduceZeros(quotient);
                            // the dependency i am using for more accurate big decimal calculation for some reason dislikes
                            // doing trig operations with big numbers
                            num1=DefaultBigDecimalMath.sin(quotient);
                            num2=DefaultBigDecimalMath.cos(quotient);
                            if ((num1.compareTo(BigDecimal.ZERO) > -1)) {
                                sintmp += 1 << j;
                            }
                            if ((num2.compareTo(BigDecimal.ZERO) > -1)) {
                                costmp += 1 << j;
                            }
                            if ((num1.negate().compareTo(BigDecimal.ZERO) > 0)) {
                                nsintmp += 1 << j;
                            }
                            if ((num2.negate().compareTo(BigDecimal.ZERO) > 0)) {
                                ncostmp += 1 << j;
                            }
                        }
                        nums.add(sintmp/2);
                        map.put(sintmp/2,map.getOrDefault(sintmp/2,0)+1);
                        nums.add(costmp/2);
                        map.put(costmp/2,map.getOrDefault(costmp/2,0)+1);
                        nums.add(nsintmp/2);
                        map.put(nsintmp/2,map.getOrDefault(nsintmp/2,0)+1);
                        nums.add(ncostmp/2);
                        map.put(ncostmp/2,map.getOrDefault(ncostmp/2,0)+1);
                    }
                    HashMap<Integer, Integer> finalMap = map;
                    if (nums.size() >= largestsize) {
                        largestsize = nums.size();
                        System.out.println("set: " + nums.toString());
                        map.forEach((key,value)-> System.out.println(key+" has been found "+value+" times"));
                        System.out.println("largest size: " + largestsize);
                        System.out.println("largest a:" + new BigDecimal(a).toPlainString());
                        System.out.println("largest b:" + b.toPlainString());
                        writer.append("Latest LARGEST b: ").append(b.toPlainString()).append(" with a hashmap of: ").append(map.keySet().stream()
                                .map(key -> key + "=" + finalMap.get(key))
                                .collect(Collectors.joining(", ", "{", "}")));
                    }
                    else{
                        writer.append("Latest b: ").append(b.toPlainString()).append(" with a hashmap of: ").append(map.keySet().stream()
                                .map(key -> key + "=" + finalMap.get(key))
                                .collect(Collectors.joining(", ", "{", "}")));
                    }
                    writer.newLine();
                    writer.flush();
                }
            }
        }
        static BigDecimal reduceZeros(BigDecimal a){
            BigDecimal out = a.stripTrailingZeros();
            if(out.compareTo(BigDecimal.ONE)>0){
//                while(out.remainder(BigDecimal.TEN).intValue() == 0) {
//                    out = out.divide(BigDecimal.TEN);
//                }
                out=out.movePointLeft(287); // the extra amount of zeroes
                out=out.stripTrailingZeros();
            }
            else{
                System.out.println(out.toPlainString());
                return out;
            }
            return out;
        }
        public void run(){
            int loclargestn = 0;
            int loclargesta = 0;
            float loclargestb = 0;
//            int loclargestc = 0;
//            int loclargestd = 0;
//            int loclargestspread = 0;
            int min = 1 << (pow-1);
            int max = 1 << (pow);
            int tmp;
            for (float b = min; b < max; b=Math.nextUp(b)) {
//                for (int c = min; c < max; c++) {
//                    for (int d = min; d < max; d++) {
                for (int n = min; n < max; n++) {
//                            for (int spread = 0; spread < 9; spread++) {
                    tmp = THEfunction(a, b, 1, 1, n, 1);
                    if(tmp>loclargestsize) {
                        loclargestsize= tmp;
                        loclargestn = n;
                        loclargesta = a;
                        loclargestb=b;
//                                    loclargestc=c;
//                                    loclargestd=d;
//                                    loclargestspread=spread;
//                if(largestsize.get==256){
//                    for(String ab : binary){
//                        if(ab!=null) {
//                            System.out.println(ab+" = "+Integer.parseInt(ab,2)+" with i, g of:"+ Arrays.toString(vars.get(Integer.parseInt(ab,2))));
//                        }
//                    }
//                }
//                                }
                    }
                }
//                    }
//                }
            }
            tmp = largestsize.get();
            while(loclargestsize>tmp) {
                if (largestsize.compareAndSet(tmp,loclargestsize)) {
                    largestsize.set(loclargestsize);
                    largestn.set(loclargestn);
                    largesta.set(loclargesta);
                    largestb=loclargestb;
//                    largestc.set(loclargestc);
//                    largestd.set(loclargestd);
//                    largestspread.set(loclargestspread);

                    System.out.println("current largest a: " + loclargesta);
                    System.out.println("current largest b: " + loclargestb);
                    //System.out.println("current largest c: " + loclargestc);
                    //System.out.println("current largest d: " + loclargestd);
                    System.out.println("current largest n: " + loclargestn);
                    //System.out.println("current largest spread: " + loclargestspread);
                    System.out.println("largest size being: " + loclargestsize);
                    System.out.println("");
                }
                else{
                    tmp=largestsize.get();
                }
            }

        }
        public static int THEfunction(int a,float b,int c,int d, int n,int spread){
            int tmp;
            HashSet<Integer> nums= new HashSet<>();
//            for (int i = 0; i < Math.pow(2,spread); i += 1) { //2^3
//                for (int g = 0; g < Math.pow(2,8-spread); g += 1) { // 2^5
//                    tmp=0;
//                    for (int j = 0; j < 8; j++) {
//                        if((Math.sin(((g+1+a)*b) * (j+n) / ((i+1+c)*d)) >= 0)){
//                            tmp += 1 << j;
//                        }
//                        // start 375 end 382,g=1 <66 +2 g*b,i=1 <9 +=1, no pi a=1 b=1, 256 unique waveforms
//                        // formula would thus be Math.sin((var1+1) * (input+329) / var2*1.0) >= 0.0 ? 1 : 0 with integer division
//                    }
//                    nums.add(tmp);
//                }
//            }
            for (int i = 0; i < (1 << 8) ; i += 1) {
                tmp=0;
                for (int j = 1; j < 9; j++) {
                    if((Math.sin((j+n)*a / ((i+1.0)*b)) >= 0)){
                        tmp += 1 << j;
                    }
                }
                nums.add(tmp/2);
            }
            //System.out.println("hashsets: " + nums.toString());
            return nums.size();
        }
    }
}
