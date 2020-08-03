package com.supcon.mes.module_wom_producetask;

import android.content.Context;



import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleInstrumentedTest {
    public static void main(String []args){
        String str="incode=010101002,batchno=,batchno2=,packqty=1,packs=100,purcode=20072800004,orderno=30,";
        String[] arr=str.split(",");
        for (int i = 0; i <arr.length ; i++) {
            System.out.println(arr[i]);
        }
    }

}
