package test_executors;

public class TestExecutorFactory {

    private TestExecutorFactory(){

    }

    public static final TestExecutor getTestExcutor(String executorType){
        switch (executorType){
            case "Invalid":
                return new InvalidTestExecutor(executorType);
            case "Valid":
                return new ValidTestExecutor(executorType);
            case "Success":
                return new SuccessTestExecutor(executorType);
        }
        return null;
    }
}
