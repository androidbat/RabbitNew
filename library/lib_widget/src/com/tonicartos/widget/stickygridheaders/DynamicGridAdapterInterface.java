package com.tonicartos.widget.stickygridheaders;
public interface DynamicGridAdapterInterface {

    /**
     * Determines how to reorder items dragged from <code>originalPosition</code> to <code>newPosition</code>
     */
    int reorderItems(int originalPosition, int newPosition);
    int appEnd(int originalPosition, int newPosition);

    /**
     * @return return columns number for GridView. Need for compatibility
     * (@link android.widget.GridView#getNumColumns() requires api 11)
     */
    int getColumnCount();

    /**
     * Determines whether the item in the specified <code>position</code> can be reordered.
     */
    boolean canReorder(int position);
    int getHeadId(int position);

}
