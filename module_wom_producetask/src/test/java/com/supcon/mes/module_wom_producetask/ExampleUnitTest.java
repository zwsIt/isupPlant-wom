package com.supcon.mes.module_wom_producetask;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String str="incode=010101002,batchno=,batchno2=,packqty=1,packs=100,purcode=20072800004,orderno=30,specs=10";
        String[] arr=str.split(",");
        String incode=arr[0].replace("incode=","");
        String batchno=arr[1].replace("batchno=","");
        String batchno2=arr[2].replace("batchno2=","");
        String packqty=arr[3].replace("packqty=","");
        String packs=arr[4].replace("packs=","");
        String purcode=arr[5].replace("purcode=","");
        String orderno=arr[6].replace("orderno=","");
        String specs=arr[7].replace("specs=","");
        System.out.println("orderno:"+incode);
        System.out.println("batchno:"+batchno);
        System.out.println("batchno2:"+batchno2);
        System.out.println("packqty:"+packqty);
        System.out.println("packs:"+packs);
        System.out.println("purcode:"+purcode);
        System.out.println("orderno:"+orderno);
        System.out.println("specs:"+specs);

    }
}