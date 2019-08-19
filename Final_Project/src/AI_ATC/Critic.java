package AI_ATC;

import ATC_Core.Aircraft;

import java.util.List;

/**
 * Created by saija on 2017-04-11.
 */
public interface Critic {
    public List<Aircraft> getConflictAircrafts(Node node);
    public int evaluateSate(Node node);
}
