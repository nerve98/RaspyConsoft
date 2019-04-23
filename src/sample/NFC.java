package sample;

import java.io.File;
import java.util.Date;

public class NFC implements Runnable{

    //private long oldUser=0;
    //private Date newUser;

    public NFC(){
        //File lib=new File("out/production/Raspy/sample/Reader.so");
        //System.out.println(lib.getAbsolutePath());
        //System.load(lib.getAbsolutePath());

    }

    public static native int[] readNfc();



    @Override
    public void run() {
        System.out.println("Sono nel run");
        Runtime.getRuntime().load("/home/pi/Desktop/proo v2/Raspy/out/production/Raspy/sample/Reader.so");
        while(true) {
            int[] result;
            try {
                result = NFC.readNfc();
            }
            catch(Exception e){
                e.printStackTrace();
                continue;
            }

            if(!Controller.isYouCanPassBadge()){
                continue;
            }


            System.out.println("\n");
            System.out.println("result1: ");
            for(int i=0;i<result.length;i++){
                System.out.print(result[i]);
            }

            String result2 = "";
            for (int i = 0; i < result.length; i++) {

                if (result[i] < 16)
                    result2 += 0;
                result2 += Integer.toHexString(result[i]);

            }
            System.out.println("result2: " + result2);
            //if(result2==Lista.lastUID.getSomeVariable())
            Lista.lastUID.setSomeVariable(result2);
        }
    }
}
