package at.flori4n.mcmatchmakingsolo;

public interface State {
    public void preaction();
    public void action();
    public void postAction();
}
