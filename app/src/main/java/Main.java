// exit code :
// 1 : No .env file (IOException)
// 2 : AWTException
// 3 : IOException
// 4 : TesseractException
// 5 : Miss reading of page number (ArrayIndexOutOfBoundsException)

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            new Overlay();
        }
    }
}
