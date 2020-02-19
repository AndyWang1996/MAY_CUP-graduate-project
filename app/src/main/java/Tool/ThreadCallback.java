package Tool;

public abstract class ThreadCallback {

    public CallBack callBack;

    private void setCallBack(CallBack back){
        this.callBack = back;
    }

    public interface CallBack{
        void ThreadStratListener();
        void ThreadFinishListener();
    }

}
