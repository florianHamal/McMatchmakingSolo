package at.flori4n.mcmatchmakingsolo;


public class Manager {
    private static Manager instance;
    private State lastState;

    public void setState(State state){
        if (lastState!=null)lastState.postAction();
        lastState = state;
        state.preaction();
        state.action();
    }

    public static Manager getInstance(){
        if (instance == null)instance=new Manager();
        return instance;
    }

    private Manager(){}

}
