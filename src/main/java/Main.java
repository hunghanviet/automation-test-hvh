import controllers.TestController;
import víews.TestFrame;

public class Main {
    public static void main(String[] args) {
        TestController mainFrameController = new TestController();
        TestFrame mainFrame = new TestFrame(mainFrameController);
        mainFrame.setVisible(true);
    }
}