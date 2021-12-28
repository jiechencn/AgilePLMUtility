package zigzagsoft.agile.privviewer;

/**
 * Created by jijichen on 7/4/2014.
 */
public class Criteria extends OneNode {
    public Criteria(int id, String title, boolean enabled) {

        super(id, PVConstant.NODETYPE_CRITERIA, title, enabled);
    }
}