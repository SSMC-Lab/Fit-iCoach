package fruitbasket.com.bodyfit.data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;


import fruitbasket.com.bodyfit.MyApplication;

/**
 * Created by cielwu on 2016/6/2.
 */
public class StorageData {

    private  static boolean firstFlag=false;
    private String tag="storageData";
    private OutputStream outData=null;
    private OutputStream outTime=null;
    private double time;
    private long start,end;
    private boolean isfirst=true;

    private String savePath="";
    private File saveFile;
    private OutputStream out;


    public StorageData(){
        try {
            String dir_path=Environment.getExternalStorageDirectory().getAbsolutePath();
            File file=new File(dir_path);
            if(!file.exists())
                file.mkdir();
            File fileData=new File(dir_path+File.separator+"Data.txt");
            if(!fileData.exists())
                fileData.createNewFile();
            File fileTime=new File(dir_path+File.separator+"Time.txt");
            if(!fileTime.exists())
                fileTime.createNewFile();
            outData=new FileOutputStream(fileData);
            outTime=new FileOutputStream(fileTime);
            outTime.write(("开始\t结束\t持续\t类型\tDTW\n").getBytes() );
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public StorageData(String fileName){
        createDocument();
        createFile(fileName);
//        start=System.currentTimeMillis();
        try {
            out=getOutputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void createDocument(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
            File saveDocument = new File(sdCardDir,"/BodyFitModel/");
/*            if (!saveDocument.exists()) {
                saveDocument.mkdir();
            }*/
            savePath=saveDocument.getPath();
        }
    }

    private void createFile(String fileName){
        do {
            saveFile=new File(savePath,"Data"+fileName+".txt"); //amount+".txt"
//            amount++;
        }while(saveFile.exists());
    }



    public void outputData(DataUnit data) throws IOException {

        if(data==null) return;

        String temp;
        if(isfirst){
            start=System.currentTimeMillis();
            isfirst=false;
        }
        end=System.currentTimeMillis();
        time=(end-start)/1000.0;//0.123456789 12.3456789
       /* temp=time+" "+(int)((data.getAx()*100))/100.0+" "+(int)((data.getAy()*100))/100.0+" "+(int)((data.getAz()*100))/100.0
                            +" "+(int)((data.getGx()*100))/100.0 +" "+(int)((data.getGy()*100))/100.0+" "+(int)((data.getGz()*100))/100.0
                            +" "+(int)((data.getMx()*100))/100.0+" "+(int)((data.getMy()*100))/100.0+" "+(int)((data.getMz()*100))/100.0
                            +" "+(int)((data.getP1()*100))/100.0+" "+(int)((data.getP2()*100))/100.0+" "+(int)((data.getP3()*100))/100.0+"\r";

        out.write(temp.getBytes());*/
        temp=time+" "+data.getAx()+" "+data.getAy()+" "+data.getAz()
                +" "+data.getGx() +" "+data.getGy()+" "+data.getGz()
                +" "+data.getMx()+" "+data.getMy()+" "+data.getMz()
                +" "+data.getP1()+" "+data.getP2()+" "+data.getP3()+"\r";
        //outData.write(temp.getBytes());
        outData.write(temp.getBytes());
    }

    public void outputBeginTime(double begin) {//写入动作的起始时间
        String temp=new DecimalFormat("0.000").format(begin)+"\t";
        try {
            outTime.write(temp.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void outputEndTime(double end) {  //写入动作的结束时间
        String temp=new DecimalFormat("0.000").format(end)+"\t";
        try {
            outTime.write(temp.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void outputDuration(double duration) {  //写入动作的结束时间
        String temp=new DecimalFormat("0.000").format(duration)+"\t";
        try {
            outTime.write(temp.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void outputType(int type){
        String temp=type+"\t";
        try {
            outTime.write(temp.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void outputDTW(double DTW){
        String temp=(int)DTW+"\n";
        try {
            outTime.write(temp.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void outputData(DataSet data)  {  //写入一次来的数据
        Log.e(tag,"outputData(DataSet data)");
        if(data==null) return;

        double ax[],ay[],az[],gx[],gy[],gz[],mx[],my[],mz[],p1[],p2[],p3[],t[];
        t=data.getTime();
        ax=data.getAxSet();
        ay=data.getAySet();
        az=data.getAzSet();
        gx=data.getGxSet();
        gy=data.getGySet();
        gz=data.getGzSet();
        mx=data.getMxSet();
        my=data.getMySet();
        mz=data.getMzSet();
        p1=data.getP1Set();
        p2=data.getP2Set();
        p3=data.getP3Set();

        String temp;
        if(isfirst){
            start=System.currentTimeMillis();
            isfirst=false;
        }


        int size=data.size();

        for(int i=0;i<size;i++){
//            Log.e(tag,"size="+size+" i="+i);
            end=System.currentTimeMillis();
            time=(end-start)/1000.0;

          /*  temp=time+" "+((int)(ax[i]*100))/100.0+" "+((int)(ay[i]*100))/100.0+" "+((int)(az[i]*100))/100.0+" "
                            +((int)(gx[i]*100))/100.0+" "+((int)(gy[i]*100))/100.0+" "+((int)(gz[i]*100))/100.0+" "
                            +((int)(mx[i]*100))/100.0+" "+((int)(my[i]*100))/100.0+" "+((int)(mz[i]*100))/100.0+" "
                            +((int)(p1[i]*100))/100.0+" "+((int)(p2[i]*100))/100.0+" "+((int)(p3[i]*100))/100.0+"\r";

            out.write(temp.getBytes());*/
            temp=t[i]+" "+ax[i]+" "+ay[i]+" "+az[i]+" "
                    +gx[i]+" "+gy[i]+" "+gz[i]+" "
                    +mx[i]+" "+my[i]+" "+mz[i]+" "
                    +p1[i]+" "+p2[i]+" "+p3[i]+"\r";
            try {
                outData.write(temp.getBytes());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void outputDoubleDataArray(double data[]) throws IOException {
        String temp="";
        int i,len;
        len=data.length;

        for(i=0;i<len;i++)
            //temp=temp+((int)(data[i]*100))/100.0+" ";
            temp=temp+data[i]+" ";
        temp+="\r\r\r";

        outData.write(temp.getBytes());

    }


    public void closeOutputStream(){
        try {
            outData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OutputStream getOutputStream() throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(saveFile,true);
        return out;
    }

}
