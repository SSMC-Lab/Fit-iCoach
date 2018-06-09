package fruitbasket.com.bodyfit.helper;

/**
 * 这个类在程序中没有用到
 */
public class FileHelper {
    private static FileHelper fileHelper=new FileHelper();

    private FileHelper(){}

    public static FileHelper getInstance(){
        return fileHelper;
    }
}
