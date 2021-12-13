package compression;




public class Waveform {

    public static void main(String args[]){

    }


    // the current idea to store the number would be Math.sin((var1+1) * (input+329) / (var2+1)) >= 0.0 ? 1 : 0 with integer division
    // where var1 would multiply input, and var 2 would then divide the product
    // to convert this to binary var1 would be the first five bits, and the following 3 bits would be var2
    public boolean[] toFunction(boolean[] binary){ //convert binary to function binary
        if(binary.length!=8){
            throw new IllegalArgumentException("Incorrect length");
        }
        boolean[] function = new boolean[8];

        return function;
    }
    public boolean[] fromFunction(boolean[] function){ // convert the function binary back into binary
        if(function.length!=8){
            throw new IllegalArgumentException("Incorrect length");
        }
        boolean[] binary = new boolean[8];
        int var1=0;
        int var2=0;
        for(int i=0;i<5;i++){
            var1+=function[i] ? Math.pow(2,i) : 0;
        }
        for(int i=5;i<8;i++){
            var2+=function[i] ? Math.pow(2,i) : 0;
        }
        for (int i = 0; i < 8; i++) {
            binary[i]=Math.sin((var1+1) * (i+329) / (var2+1)) >= 0;
        }
        return binary;
    }

}
